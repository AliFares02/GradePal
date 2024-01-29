package com.example.demo.services;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

  @Autowired
  private JwtEncoder jwtEncoder;

  @Autowired
  private JwtDecoder jwtDecoder;

  // this is for the actual jwt creation
  public String generateJwt(Authentication auth) {
    // get time of creation
    Instant now = Instant.now();

    // the token will consist of these claims, i.e who issued it(the server), when
    // it was issued, whose recieving it. it is then built
    JwtClaimsSet claims = JwtClaimsSet.builder()
        .issuer("self")
        .issuedAt(now)
        .subject(auth.getName())
        .build();

    // .encode builds it and .getTokenValue() spits out the string value that is
    // sent to the client
    return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
  }
}
