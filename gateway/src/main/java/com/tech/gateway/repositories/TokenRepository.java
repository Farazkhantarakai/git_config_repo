package com.tech.gateway.repositories;


import com.tech.gateway.model.MyUser;
import com.tech.gateway.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

  Optional<Token> findByRefreshToken(String token);

  Optional<Token> findByAccessToken(String token);

  List<Token> findByMyUser(MyUser myUser) ;


}
