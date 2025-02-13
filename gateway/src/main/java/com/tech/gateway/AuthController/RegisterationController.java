package com.tech.gateway.AuthController;

import com.tech.gateway.ServiceImplementation.OtpServiceImplementation;
import com.tech.gateway.model.MyUser;
import com.tech.gateway.model.RegisterUser;
import com.tech.gateway.model.ResetPassword;
import com.tech.gateway.model.VerifyOtp;
import com.tech.gateway.repositories.UserRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;


@RestController
public class RegisterationController {
    private static final Logger log = LoggerFactory.getLogger(RegisterationController.class);
    private final  PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final OtpServiceImplementation emailServiceImp;


    RegisterationController(PasswordEncoder passwordEncoder,UserRepository userRepository,OtpServiceImplementation emailServiceImp){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.emailServiceImp = emailServiceImp;
    }

   @PostMapping(
           "/registerUser")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterUser user) {
     Optional<MyUser> existingUser=  userRepository.findByEmail(user.email());
     if(existingUser.isPresent()) {
         return new ResponseEntity<>("User already exists",HttpStatus.CONFLICT);
     }
     MyUser newUser=new MyUser();
     newUser.setEmail(user.email());
     newUser.setUserName(user.username());
     newUser.setPassword(passwordEncoder.encode(user.password()));
     newUser.setRoles(user.roles().toUpperCase());
     MyUser createdUser=    userRepository.save(newUser);
     return ResponseEntity.ok(createdUser);
    }

    @PostMapping("/resetPassword")
  public ResponseEntity<?>  resetPassword(@Valid @RequestBody ResetPassword resetPassword){


      Optional<MyUser> existingUser=  userRepository.findByEmail(resetPassword.email());
      if(existingUser.isPresent()) {
//       MyUser myUser=user.get();

       String otp=   emailServiceImp.generateOtp(resetPassword.email());

       if(otp.contains("success") )
       {
           return new ResponseEntity<>("OTP Sent Successfully",HttpStatus.OK);
       }else{
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("OTP Sent Failed because  "+otp);
       }
      }


        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid email");
  }


  @PostMapping("/verifyOtp")
  public  ResponseEntity<?> verifyUserOtp(@Valid @RequestBody VerifyOtp verifyOtp){
     boolean result=   emailServiceImp.validateOtp(verifyOtp.email(), verifyOtp.otp());
     if(result){
         return ResponseEntity.ok("OTP verified");
     }
        return ResponseEntity.badRequest().body("Invalid otp");
  }


}


