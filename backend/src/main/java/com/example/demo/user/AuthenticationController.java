package com.example.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.user.ApplicationUser;
import com.example.demo.user.LoginResponseDTO;
import com.example.demo.user.RegistrationDTO;
import com.example.demo.services.AuthenticationService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

  @Autowired
  private AuthenticationService authenticationService;

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@RequestBody RegistrationDTO body) {
    return authenticationService.registerUser(body.getUsername(), body.getPassword());
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody RegistrationDTO body) {
    return authenticationService.loginUser(body.getUsername(), body.getPassword());
  }
}
