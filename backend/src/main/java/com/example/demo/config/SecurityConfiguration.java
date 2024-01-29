package com.example.demo.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import com.example.demo.utils.RSAKeyProperties;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
public class SecurityConfiguration {

  private final RSAKeyProperties keys;

  public SecurityConfiguration(RSAKeyProperties keys) {
    this.keys = keys;
  }

  @Bean
  // returns a BCryptPasswordEncoder to actually encode the password whenever
  // PasswordEncoder is used
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  // responsible for telling us how the authentication will work
  public AuthenticationManager authManager(UserDetailsService detailsService, PasswordEncoder passwordEncoder) {
    // dao uses the custom implementation of UserDetailsService, UserService, which
    // in turn uses custom impl. of UserDetails, ApplicationUser, to retrieve stored
    // vital info such as username and password and dao uses those to authenticate
    // and compare with the values entered by the user
    // DAO ACTS AS THE SECURITY GUARD AND WHEN SOMEONE ENTERS THE BUILDING, DAO
    // CONSULTS USERDETAILSSERVICE TO RETRIEVE THE FILTERED PROFILE OF THE
    // RESIDENT(AKA THE USERDETAILS) WHICH CONTAINS ONLY THE NECCESSARY INFO NEEDED
    // FOR AUTHENTICATION INSTEAD OF THE ENTIRE USER PROFILE. DAO THEN COMPARES THE
    // INFORMATION AND GRANTS/DENIES ACCESS
    DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
    daoProvider.setUserDetailsService(detailsService);
    daoProvider.setPasswordEncoder(passwordEncoder);
    return new ProviderManager(daoProvider);
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        // configuring cors in the filterChain allows for defining CORS settings
        // specifically for requests that pass through security filterChain aka requests
        // that need authorization. its done in conjuction with corsConfig define when
        // app has security/authentication else just do it in corsConfig
        .cors(cors -> {
          CorsConfiguration corsConfiguration = new CorsConfiguration();
          corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000"));
          corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
          corsConfiguration.setAllowedHeaders(List.of("*"));
          corsConfiguration.setAllowCredentials(true);
          corsConfiguration.setExposedHeaders(List.of("Authorization"));
          cors.configurationSource(request -> corsConfiguration);
        })
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> {
          auth.requestMatchers("/auth/**").permitAll();
          auth.anyRequest().authenticated();
        })
        .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .build();
  }

  @Bean
  // this will be how we take in our jwt and get the information out of it
  public JwtDecoder jwtDecoder() {
    return NimbusJwtDecoder.withPublicKey(keys.getPublicKey()).build();
  }

  @Bean
  // this will be for converting the information into a jwt
  public JwtEncoder jwtEncoder() {
    JWK jwk = new RSAKey.Builder(keys.getPublicKey()).privateKey(keys.getPrivateKey()).build();
    JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
    return new NimbusJwtEncoder(jwks);
  }

}
