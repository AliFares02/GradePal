package com.example.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
// UserDetailsService responsible for invoking the vital methods of the
// UserDetails such as retrieving username and password
public class ApplicationUserService implements UserDetailsService {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    System.out.println("In the user details service");

    return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User is invalid"));
    // returning a ApplicationUser(UserDetails) object after initial authentication
    // enables the spring security to easily authenticate the user using the
    // returned UserDetails object for subsequent requests
  }

}
