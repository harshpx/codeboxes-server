package com.codeboxes.server.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codeboxes.server.Collections.Code;
import com.codeboxes.server.Collections.User;
import com.codeboxes.server.Exceptions.EntityNotFoundException;
import com.codeboxes.server.Repositories.CodeRepository;
import com.codeboxes.server.Repositories.UserRepository;
import com.codeboxes.server.Services.SecurityConfigServices.UserDetailsImpl;

@Service
public class CodeService {
  @Autowired
  private CodeRepository codeRepository;

  @Autowired
  private UserRepository userRepository;

  public List<Code> getAllCodes() {
    return codeRepository.findAll();
  }

  public List<Code> getCodesByUser(UserDetailsImpl userDetails) {
    String userId = userDetails.getUser().getId();
    User authorizedUser = userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("User not found"));
    return codeRepository.findByCreatedBy(authorizedUser.getId());
  }

  public Code getCodeById(String code) {
    return codeRepository.findById(code).orElseThrow(() -> new RuntimeException("Code not found with id: " + code));
  }

  @Transactional
  public Code saveCode(Code code) {
    Optional<User> user = userRepository.findById(code.getCreatedBy());
    if (user.isPresent()) {
      return codeRepository.save(code);
    } else {
      throw new RuntimeException("User not found with id: " + code.getCreatedBy());
    }
  }

  @Transactional
  public void deleteCode(String codeId) {
    codeRepository.deleteById(codeId);
  }
}
