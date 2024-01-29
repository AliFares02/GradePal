package com.example.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.user.ApplicationUser;
import com.example.demo.user.RegistrationDTO;
import com.example.demo.user.UserRepository;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/user")
public class ApplicationUserController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @GetMapping()
  public String helloUser() {
    return "User access level";
  }

  @PatchMapping("/update-user/{userId}")
  @Transactional
  public ResponseEntity<?> updateUser(@PathVariable("userId") Integer userId, @RequestBody RegistrationDTO body,
      Authentication authentication) {
    if (authentication == null || !authentication.isAuthenticated()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: User not authenticated");
    }
    ApplicationUser userToUpdate = userRepository.findById(userId)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    if (userToUpdate.getUsername().equals(authentication.getName())) {
      if (body.getUsername() != null) {
        userToUpdate.setUsername(body.getUsername());
      }
      if (body.getPassword() != null) {
        String hashedPassword = passwordEncoder.encode(body.getPassword());
        userToUpdate.setPassword(hashedPassword);
      }
      ApplicationUser updatedUser = userRepository.save(userToUpdate);
      return ResponseEntity.ok(updatedUser);
    }
    return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: User not authorized to update this profile.");
  }
}
