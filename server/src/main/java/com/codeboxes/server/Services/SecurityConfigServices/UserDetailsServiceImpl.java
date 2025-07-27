package com.codeboxes.server.Services.SecurityConfigServices;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.codeboxes.server.Collections.User;
import com.codeboxes.server.Repositories.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UserRepository repository;

  @Override
  public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
    Optional<User> user = repository.findByUsername(identifier).or(() -> repository.findByEmail(identifier));
    if (user.isPresent()) {
      return new UserDetailsImpl(user.get());
    } else {
      throw new UsernameNotFoundException("User not found with identifier: " + identifier);
    }
  }
}
