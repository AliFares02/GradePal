package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.user.ApplicationUser;
import com.example.demo.user.LoginResponseDTO;
import com.example.demo.user.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional // all methods are treated as a single transaction, if a method fails entire
               // transaction is canceled
public class AuthenticationService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private TokenService tokenService;

  public ResponseEntity<?> registerUser(String username, String password) {
    try {
      ApplicationUser userExists = userRepository.findByUsername(username).orElse(null);

      if (userExists != null) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(username + " already taken.");
      }

      String encodedPassword = passwordEncoder.encode(password);
      ApplicationUser newUser = userRepository.save(new ApplicationUser(0, username, encodedPassword));

      return ResponseEntity.ok(newUser);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during registration");
    }
  }

  public ResponseEntity<LoginResponseDTO> loginUser(String username, String password) {

    try {
      // Authentication is a generic authentication token/object and
      // UsernamePasswordAuthenticationToken is a more specific version of
      // Authentication
      // line below generates authentication token based on the username and password
      // and authenticate() it. Authentication token is separate from jwt token. Auth
      // token represents credentials and UsernamePasswordAuthenticationToken holds
      // the username and password which so when we use 'auth' elsewhere we can
      // extract username and password from it.
      Authentication auth = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(username, password));

      String token = tokenService.generateJwt(auth);

      ApplicationUser user = userRepository.findByUsername(username)
          .orElseThrow(() -> new UsernameNotFoundException("User not found"));

      return ResponseEntity.ok(new LoginResponseDTO(user, token));

    } catch (AuthenticationException e) {
      String errorMessage = "Authentication failed: Invalid username or password";
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponseDTO(null, errorMessage));
    }
  }

}
