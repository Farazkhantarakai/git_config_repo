package com.tech.gateway.repositories;

import com.tech.gateway.model.OtpTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OtpTable, Long> {
    Optional<OtpTable> findByEmailAndOtp(String email, String otp);

  Optional<List<OtpTable>>  findByEmail(String email);


}