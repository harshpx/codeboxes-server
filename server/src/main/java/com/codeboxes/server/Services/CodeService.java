package com.codeboxes.server.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codeboxes.server.Collections.Code;
import com.codeboxes.server.Collections.User;
import com.codeboxes.server.DTOs.Code.SaveCodeRequest;
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
        .orElseThrow(() -> new BadCredentialsException("User not found"));
    return codeRepository.findByCreatedBy(authorizedUser.getId());
  }

  public Code getCodeById(String code) {
    return codeRepository.findById(code)
        .orElseThrow(() -> new EntityNotFoundException("Code not found"));
  }

  @Transactional
  public Code saveCode(SaveCodeRequest request, UserDetailsImpl userDetails) {
    String userId = userDetails.getUser().getId();
    Optional<User> user = userRepository.findById(userId);
    if (user.isPresent()) {
      Code code = new Code(user.get().getId(), request);
      return codeRepository.save(code);
    } else {
      throw new BadCredentialsException("User not found");
    }
  }

  @Transactional
  public void deleteCode(String codeId) {
    codeRepository.deleteById(codeId);
  }
}
