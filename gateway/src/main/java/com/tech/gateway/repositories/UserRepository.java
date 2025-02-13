package com.tech.gateway.repositories;


import com.tech.gateway.model.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository  extends JpaRepository<MyUser, Integer> {
    Optional<MyUser> findByEmail(String email);
//   @Query("")
   Optional<MyUser> findByEmailIgnoreCase(String email);

}
