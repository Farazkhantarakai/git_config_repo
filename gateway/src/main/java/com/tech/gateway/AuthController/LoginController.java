package com.tech.gateway.AuthController;

import com.tech.gateway.ServiceImplementation.OtpServiceImplementation;
import com.tech.gateway.model.*;
import com.tech.gateway.repositories.TokenRepository;
import com.tech.gateway.repositories.UserRepository;
import com.tech.gateway.services.JwtServices.JwtService;
import com.tech.gateway.services.JwtServices.MyUserDetailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class LoginController {
//this will help us to authenticate the user with name and password
    private final AuthenticationManager authenticationManager;
//    this is for generating the token when user login
    private final JwtService jwtService;
//    this is giving the user details to the generator function
    private final MyUserDetailService userDetailService;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final OtpServiceImplementation otpServiceImplementation;
    private final PasswordEncoder passwordEncoder;

    Logger logs=LoggerFactory.getLogger(LoginController.class);
    LoginController(AuthenticationManager authenticationManager, JwtService jwtService,  MyUserDetailService userDetailsService,TokenRepository tokenRepository,UserRepository userRepository,OtpServiceImplementation otpServiceImplementation,PasswordEncoder passwordEncoder){
        this.authenticationManager=authenticationManager;
        this.jwtService=jwtService;
        this.userDetailService=userDetailsService;
        this.tokenRepository=tokenRepository;
        this.userRepository=userRepository;
        this.otpServiceImplementation=otpServiceImplementation;
        this.passwordEncoder=passwordEncoder;
    }



    @PostMapping("/loginUser")
    public ResponseEntity<?> authenticateAndGetToken(@Valid @RequestBody Login login) {
        Authentication authentication;
        try {
             authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.email(), login.password()));
        }catch (Exception e) {
            logs.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
        UserDetails userDetails = userDetailService.loadUserByUsername(authentication.getName());
            String accessToken = jwtService.generateAccessToken(userDetails);
            String refreshToken = jwtService.refreshToken(userDetails);
            logs.info("Refresh token: {}access token{}", refreshToken, accessToken);
            //this will disable all the access tokens that are generated first and will enable only this
            revokeAll(userDetails.getUsername());
            jwtService.saveToken(accessToken, refreshToken, authentication.getName());
            return ResponseEntity.ok(new AuthenticationResponse(accessToken, refreshToken, "User LoggedIn successfully"));
    }

   public void revokeAll(String userName){
    MyUser myUser= userRepository.findByEmail(userName).get();
    List<Token> tokens=tokenRepository.findByMyUser(myUser);
    if(tokens.isEmpty()){
        return ;
    }
    tokens.forEach(token -> token.setUserLogStatus(false));
tokenRepository.saveAll(tokens);
   }





@PostMapping("/refresh")
    public  ResponseEntity<?>   refreshToken(HttpServletRequest request, HttpServletResponse response) {

    String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
    }
    String token = authHeader.substring(7);
    String userName = jwtService.extractUserName(token);
    MyUser myUser = userRepository.findByEmail(userName).orElseThrow(() -> new RuntimeException("Not found exception"));

    if (jwtService.isValidToken(token, myUser)) {
        UserDetails userDetails = userDetailService.loadUserByUsername(myUser.getEmail());
        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.refreshToken(userDetails);
        logs.info("Refresh token: {}access token{}", refreshToken, accessToken);
        //this will disable all the access tokens that are generated first and will enable only this
        revokeAll(userDetails.getUsername());
        jwtService.saveToken(accessToken, refreshToken, userDetails.getUsername());
        return ResponseEntity.ok(new AuthenticationResponse(accessToken, refreshToken, "Token Refreshed successfully"));
    }


 return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
}
    @PostMapping("/generateOtp")
public ResponseEntity<?>   updatePassword(@Valid @RequestBody String  email) throws Exception{
       Optional<MyUser> myUser= userRepository.findByEmail(email);
       if(!myUser.isPresent()){
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email not found");
       }
       otpServiceImplementation.generateOtp(email);
        return ResponseEntity.ok("Otp created and sended successfully");
    }




    @PostMapping("/verifyEmail")
    public ResponseEntity<?> verifyEmail(@RequestBody String email) {
     
     Optional<MyUser>   myUser=   userRepository.findByEmail(email);

     if(!myUser.isPresent()){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This email does not exist");
     }
     otpServiceImplementation.generateOtp(email);
     return ResponseEntity.ok("Otp sent successfully");
    }


    @PostMapping("/verifyOtpValidate")
    public ResponseEntity<?> validateOtp(@RequestBody String email,@RequestBody String otp) {
        boolean isValid=  otpServiceImplementation.validateOtp(email, otp);
        if(isValid){
        Optional<MyUser> myUser=    userRepository.findByEmail(email);
        if(myUser.isPresent()){
             MyUser result=    myUser.get();
             result.setIsVerified(true);
            userRepository.save(result);
        return ResponseEntity.status(HttpStatus.OK).body("successfully verified");
        }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Some thing went wrong");
    }
    
    

    


@PostMapping("/updateAndValidateOtp")
    public ResponseEntity<?>  validatingAndSendOtp(@Valid @RequestBody ValidateOtp validateOtp){
     boolean isValid=  otpServiceImplementation.validateOtp(validateOtp.email(), validateOtp.otp());
     if(isValid){

         MyUser myUser= userRepository.findByEmail(validateOtp.email()).get();
         myUser.setPassword(passwordEncoder.encode(validateOtp.newPassword()));
         userRepository.save(myUser);
         return ResponseEntity.status(HttpStatus.OK).body("Password updated successfully");
     }

return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }



}
