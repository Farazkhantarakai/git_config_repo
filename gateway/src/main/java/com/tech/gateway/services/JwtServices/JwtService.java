package com.tech.gateway.services.JwtServices;



import com.tech.gateway.model.MyUser;
import com.tech.gateway.model.Token;
import com.tech.gateway.repositories.TokenRepository;
import com.tech.gateway.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;

import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class JwtService {
    // private static final Logger log = LoggerFactory.getLogger(JwtService.class);
    private final Long  VALIDITY= TimeUnit.MINUTES.toMillis(30);
    private final Long  refreshValidity=TimeUnit.MINUTES.toMillis(45);


  @Autowired
    private UserRepository userRepository;
  @Autowired
  private TokenRepository tokenRepository;

  // @Autowired
  // private MyUserDetailService userDetailService;

    public String generateToken(UserDetails userDetails, Long validity){
        Map<String, String> claims=new HashMap<>();
return Jwts.builder().claims(claims).subject(userDetails.getUsername())
        .issuedAt(Date.from(Instant.now()))
        .expiration(Date.from(Instant.now().plusMillis(validity)))
        .signWith(generateKey())
        .compact();
}


public String generateAccessToken(UserDetails userDetails){
return generateToken(userDetails,VALIDITY);
}

public String refreshToken(UserDetails userDetails){
       return generateToken(userDetails,refreshValidity);
    }



private SecretKey generateKey(){
     String SECRET = "4E10479890520A89FE7B53177E9BC39B647A0438EBB1AFD71541231FC252152C362CAE369E9EB8076E3FE300EFB49D877B8489D482269AED013DACB06F1A077D";
   
   
    byte[] decodeKey = Base64.getDecoder().decode(SECRET)   ;
    return Keys.hmacShaKeyFor(decodeKey);
}
    public String extractUserName(String jwt) {
    Claims claims= getClaim(jwt);
    return claims.getSubject();
}


    private Claims getClaim(String jwt){
    return Jwts.parser()
            .verifyWith(generateKey())
            .build()
            .parseSignedClaims(jwt)
            .getPayload();
    }


    public boolean isTokenValid(String jwt) {
//        log.info("jwt{}", jwt);
    Claims claims=getClaim(jwt);
  return   claims.getExpiration().after(Date.from(Instant.now()));
    }


    public   void   saveToken(String accessToken,String refreshToken,String name){
      MyUser myUser= userRepository.findByEmail(name).get();
      Token token=new Token();
      token.setMyUser(myUser);
      token.setRefreshToken(refreshToken);
      token.setAccessToken(accessToken);
      token.setUserLogStatus(true);
      tokenRepository.save(token);
    }


    public boolean isValidToken(String token, MyUser myUser) {
        String email=extractUserName(token);
        boolean validRefreshToken=tokenRepository.findByRefreshToken(token).map(Token::getUserLogStatus).orElse(false);
return email.equals(myUser.getEmail()) && isTokenValid(token) && validRefreshToken;
    }
}
