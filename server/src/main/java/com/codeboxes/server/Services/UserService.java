package com.codeboxes.server.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codeboxes.server.Collections.User;
import com.codeboxes.server.Collections.Code;
import com.codeboxes.server.DTOs.Auth.AuthenticatedUserResponse;
import com.codeboxes.server.DTOs.Auth.LoginUserRequest;
import com.codeboxes.server.DTOs.Auth.PatchUserRequest;
import com.codeboxes.server.DTOs.Auth.RegisterUserRequest;
import com.codeboxes.server.Exceptions.EntityNotFoundException;
import com.codeboxes.server.Repositories.CodeRepository;
import com.codeboxes.server.Repositories.UserRepository;
import com.codeboxes.server.Services.SecurityConfigServices.JwtService;
import com.codeboxes.server.Services.SecurityConfigServices.UserDetailsImpl;

@Service
public class UserService {
  @Autowired
  private UserRepository repository;

  @Autowired
  private CodeRepository codeRepository;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtService jwtService;

  @Transactional
  public AuthenticatedUserResponse registerUser(RegisterUserRequest request) {
    String username = request.getUsername();
    String email = request.getEmail();
    String hashedPassword = new BCryptPasswordEncoder(10).encode(request.getPassword());
    User newUser = new User(username, email, hashedPassword);
    repository.save(newUser);
    return this.authenticateUser(new LoginUserRequest(username, request.getPassword()));
  }

  public AuthenticatedUserResponse authenticateUser(LoginUserRequest request) {
    String identifier = request.getIdentifier();
    String password = request.getPassword();

    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(identifier, password));

    if (!authentication.isAuthenticated()) {
      throw new RuntimeException("Authentication failed, invalid credentials provided");
    }

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    User authenticatedUser = userDetails.getUser();
    String jwtToken = jwtService.generateToken(authenticatedUser);
    return new AuthenticatedUserResponse(authenticatedUser, jwtToken);
  }

  @Transactional
  public AuthenticatedUserResponse patchUpdateUser(PatchUserRequest request, UserDetailsImpl userDetails) {
    String userId = userDetails.getUser().getId();
    User existingUser = repository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
    if (request.getUsername() == null && request.getEmail() == null && request.getPassword() == null) {
      throw new IllegalArgumentException("No fields to update provided");
    }
    if (request.getUsername() != null) {
      existingUser.setUsername(request.getUsername());
    }
    if (request.getEmail() != null) {
      existingUser.setEmail(request.getEmail());
    }
    if (request.getPassword() != null) {
      String hashedPassward = new BCryptPasswordEncoder(10).encode(request.getPassword());
      existingUser.setPassword(hashedPassward);
    }
    repository.save(existingUser);
    return new AuthenticatedUserResponse(existingUser, jwtService.generateToken(existingUser));
  }

  @Transactional
  public void deleteUser(UserDetailsImpl userDetails) {
    String userId = userDetails.getUser().getId();
    User existingUser = repository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    List<Code> codesByUser = codeRepository.findByCreatedBy(existingUser.getId());
    codeRepository.deleteAll(codesByUser);
    repository.delete(existingUser);
  }

  public boolean checkUsernameAvailability(String username) {
    Optional<User> user = repository.findByUsername(username);
    return user.isEmpty();
  }

  public boolean checkEmailAvailability(String email) {
    Optional<User> user = repository.findByEmail(email);
    return user.isEmpty();
  }

  public AuthenticatedUserResponse getAuthorizedUser(UserDetailsImpl userDetails) {
    String authorizedUserId = userDetails.getUser().getId();
    User authorizedUser = repository.findById(authorizedUserId)
        .orElseThrow(() -> new EntityNotFoundException("User not found"));
    String token = jwtService.generateToken(authorizedUser);
    return new AuthenticatedUserResponse(authorizedUser, token);
  }
}
