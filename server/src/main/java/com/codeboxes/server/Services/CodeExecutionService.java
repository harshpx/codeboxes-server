package com.codeboxes.server.Services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.batch.v1.Job;
import io.fabric8.kubernetes.api.model.batch.v1.JobBuilder;
import io.fabric8.kubernetes.api.model.batch.v1.JobSpecBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.codeboxes.server.DTOs.CodeExecution.CodeExecutionRequest;
import com.codeboxes.server.DTOs.CodeExecution.CodeExecutionResponse;

@Service
@Slf4j
public class CodeExecutionService {
  private final String environment;
  private final KubernetesClient kubernetesClient;
  public CodeExecutionService(@Value("${env}") String environment, KubernetesClient kubernetesClient) {
    this.environment = environment;
    this.kubernetesClient = kubernetesClient;
  }
  public CodeExecutionResponse executeCode(CodeExecutionRequest request) throws IOException, InterruptedException {
    // validate request
    if (request.getCode() == null || request.getLanguage() == null) {
      throw new RuntimeException("Invalid request: Check payload");
    }

    // encode code and input into base64
    String encodedCode = Base64.getEncoder().encodeToString(request.getCode().getBytes());
    String encodedInput = Base64.getEncoder()
        .encodeToString(request.getInput() != null ? request.getInput().getBytes() : new byte[0]);
    String language = request.getLanguage();

    if (environment.equals("DEV")) {
      log.info("codeExecution_in_DEV_env");
      // requires docker container (code-runner:latest)
      // execute code-runner docker container using Java ProcessBuilder
      return executeDocker(language, encodedCode, encodedInput);
    } else if (environment.equals("PROD")) {
      log.info("codeExecution_in_PROD_env");
      // requires kubernetes env with proper job configs
      // spawn short-lived jobs using kubernetes API
      return executeKubernetes(language, encodedCode, encodedInput);
    }
    return new CodeExecutionResponse("Environment not recognised", true);
  }

  private CodeExecutionResponse executeDocker(String language, String encodedCode, String encodedInput) throws IOException, InterruptedException {
    // execute code-runner docker container using Java ProcessBuilder
    ProcessBuilder processBuilder = new ProcessBuilder(
            "docker", "run", "--rm",
            "-e", "LANGUAGE=" + language,
            "-e", "CODE_B64=" + encodedCode,
            "-e", "INPUT_B64=" + encodedInput,
            "code-runner:latest");

    // start the process
    Process process = processBuilder.start();

    // read output and error streams
    BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    StringBuilder outputSb = new StringBuilder();
    String line;
    while ((line = outputReader.readLine()) != null) {
      outputSb.append(line).append("\n");
    }
    BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
    StringBuilder errorSb = new StringBuilder();
    while ((line = errorReader.readLine()) != null) {
      errorSb.append(line).append("\n");
    }

    // exit process
    int exitCode = process.waitFor();

    // return response
    if (exitCode != 0 || !errorSb.isEmpty()) {
      log.info("codeExecution_docker_image_execution_completed_with:FAILURE");
      return new CodeExecutionResponse(!errorSb.isEmpty() ? errorSb.toString() : outputSb.toString(), true);
    }
    log.info("codeExecution_docker_image_execution_completed_with:SUCCESS");
    return new CodeExecutionResponse(outputSb.toString(), false);
  }
  private CodeExecutionResponse executeKubernetes(String language, String encodedCode, String encodedInput) throws InterruptedException {
    // spawn short-lived jobs using kubernetes API
    String jobName = "code-runner-" + language + UUID.randomUUID().toString().substring(0, 8);
    Job job = new JobBuilder()
            .withNewMetadata()
            .withName(jobName)
            .withLabels(Map.of("app", "code-runner"))
            .endMetadata()
            .withSpec(new JobSpecBuilder()
                    .withBackoffLimit(0)
                    .withActiveDeadlineSeconds(10L)
                    .withTemplate(new PodTemplateSpecBuilder()
                            .withNewMetadata()
                            .withLabels(Map.of("app", "code-runner"))
                            .endMetadata()
                            .withNewSpec()
                              .withRestartPolicy("Never")
                              .withAutomountServiceAccountToken(false)
                              .withHostNetwork(false)
                              .addNewContainer()
                                .withName("code-runner")
                                .withImage("ghcr.io/harshpx/code-runner:latest")
                                .withImagePullPolicy("Always")
                                .withEnv(
                                        new EnvVarBuilder().withName("LANGUAGE").withValue(language).build(),
                                        new EnvVarBuilder().withName("CODE_B64").withValue(encodedCode).build(),
                                        new EnvVarBuilder().withName("INPUT_B64").withValue(encodedInput).build()
                                )
                                .withResources(new ResourceRequirementsBuilder()
                                        .addToRequests("cpu", new Quantity("100m"))
                                        .addToRequests("memory", new Quantity("64Mi"))
                                        .addToLimits("cpu", new Quantity("500m"))
                                        .addToLimits("memory", new Quantity("128Mi"))
                                        .build()
                                )
                                .withSecurityContext(new SecurityContextBuilder()
                                        .withRunAsNonRoot(true)
                                        .withAllowPrivilegeEscalation(false)
                                        .withCapabilities(new CapabilitiesBuilder()
                                                .withDrop("ALL")
                                                .build()
                                        )
                                        .build()
                                )
                              .endContainer()
                              .withImagePullSecrets(
                                      new LocalObjectReferenceBuilder()
                                              .withName("ghcr-secret")
                                              .build()
                              )
                            .endSpec()
                            .build()
                    )
                    .build()
            )
            .build();

    kubernetesClient.batch().v1().jobs().inNamespace("default").resource(job).create();

    try {
      waitForJob(jobName);
      Pod pod = kubernetesClient.pods()
              .inNamespace("default")
              .withLabel("job-name", jobName)
              .list()
              .getItems()
              .stream()
              .findFirst()
              .orElseThrow(() -> new RuntimeException("Runner pod not found"));

      String logs = kubernetesClient.pods()
              .inNamespace("default")
              .withName(pod.getMetadata().getName())
              .getLog();

      Job completedJob = kubernetesClient.batch()
              .v1()
              .jobs()
              .inNamespace("default")
              .withName(jobName)
              .get();

      boolean failed = completedJob.getStatus().getSucceeded() == null ||
              completedJob.getStatus().getSucceeded() == 0;

      log.info("codeExecution_kubernetes_job_execution_completed_with:{}", failed ? "FAILURE" : "SUCCESS");

      return new CodeExecutionResponse(logs, failed);

    } finally {
      kubernetesClient.batch()
              .v1()
              .jobs()
              .inNamespace("default")
              .withName(jobName)
              .delete();
    }
  }

  private void waitForJob(String jobName) throws InterruptedException {

    long timeout = System.currentTimeMillis() + 15_000;

    while (System.currentTimeMillis() < timeout) {

      Job job = kubernetesClient.batch()
              .v1()
              .jobs()
              .inNamespace("default")
              .withName(jobName)
              .get();

      if (job == null || job.getStatus() == null) {
        Thread.sleep(200);
        continue;
      }

      if (job.getStatus().getSucceeded() != null
              && job.getStatus().getSucceeded() > 0) {
        return;
      }

      if (job.getStatus().getFailed() != null
              && job.getStatus().getFailed() > 0) {
        return;
      }

      Thread.sleep(200);
    }

    throw new RuntimeException("Code execution timed out");
  }
}
