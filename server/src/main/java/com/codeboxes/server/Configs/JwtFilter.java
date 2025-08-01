package com.codeboxes.server.Configs;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.codeboxes.server.DTOs.CommonResponse;
import com.codeboxes.server.Services.SecurityConfigServices.JwtService;
import com.codeboxes.server.Services.SecurityConfigServices.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {
  @Autowired
  private JwtService jwtService;

  @Autowired
  ApplicationContext context;

  @Autowired
  private ObjectMapper objectMapper;

  @SuppressWarnings("null")
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {

      // Short circuit for public endpoints
      AntPathMatcher pathMatcher = new AntPathMatcher();
      String path = request.getRequestURI();
      for (String publicEndpoint : SecurityConstraints.PUBLIC_ENDPOINTS) {
        if (pathMatcher.match(publicEndpoint, path)) {
          filterChain.doFilter(request, response);
          return;
        }
      }

      // extract the JWT token from the Authorization header
      String authHeader = request.getHeader("Authorization");
      String token = null;
      String username = null;
      if (authHeader != null && authHeader.startsWith("Bearer ")) {
        token = authHeader.substring(7);
        username = jwtService.extractUsername(token);
      } else {
        throw new JwtException("Authorization header is missing or does not start with Bearer");
      }

      // If a valid username is found and unauthenticated
      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = context.getBean(UserDetailsServiceImpl.class).loadUserByUsername(username);

        // Validate the token and set authentication in the context, throw error if
        // invalid
        if (jwtService.validateToken(token, userDetails)) {
          UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
              userDetails, null, userDetails.getAuthorities());
          usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        } else {
          throw new ExpiredJwtException(null, null, "JWT Token is invalid or expired");
        }
      }
      filterChain.doFilter(request, response);
    } catch (UsernameNotFoundException e) {
      CommonResponse<String> errorResponse = new CommonResponse<>(e.getLocalizedMessage(),
          HttpStatus.NOT_FOUND.value());
      response.setStatus(HttpStatus.NOT_FOUND.value());
      response.setContentType("application/json");
      response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    } catch (JwtException e) {
      CommonResponse<String> errorResponse = new CommonResponse<>(e.getLocalizedMessage(),
          HttpStatus.UNAUTHORIZED.value());
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.setContentType("application/json");
      response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    } catch (Exception e) {
      CommonResponse<String> errorResponse = new CommonResponse<>("Unexpected error occured",
          HttpStatus.INTERNAL_SERVER_ERROR.value());
      response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      response.setContentType("application/json");
      response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
  }
}
