package com.tech.gateway.ServiceImplementation;

import com.tech.gateway.model.OtpTable;
import com.tech.gateway.repositories.OtpRepository;
import com.tech.gateway.services.EmailService.OtpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpServiceImplementation implements OtpService {
    private static final Logger log = LoggerFactory.getLogger(OtpServiceImplementation.class);
    @Autowired
    private OtpRepository otpRepository;
    

//    @Autowired
//    private EmailServiceImpl emailService;
    public String generateOtp(String email)  {

    try{
        // Generate a random OTP
        String otp = String.valueOf(new Random().nextInt(999999));
        // Set OTP expiration time
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5);

        revokeAllOtps(email);

        // Save OTP in the database
        OtpTable otpEntity = new OtpTable();
        otpEntity.setOtp(otp);
        otpEntity.setEmail(email);
        otpEntity.setExpiryDateTime(expiryTime);

        otpRepository.save(otpEntity);
        // Send OTP via email
//        emailService.sendEmail(email, "Your OTP", "Your OTP is: " + otp);
        return "success";

    }catch(Exception ex){
        log.info(ex.toString());
        return ex.getMessage();
    }
    }




    public boolean validateOtp(String email, String otp) {
        Optional<OtpTable> otpEntity = otpRepository.findByEmailAndOtp(email, otp);
        if(otpEntity.isPresent() && otpEntity.get().getExpiryDateTime().isAfter(LocalDateTime.now())){
           OtpTable data= otpEntity.get();
           data.setVerified(true);
           otpRepository.save(data);
            return true;
        }
    return false;
    }

    public void  revokeAllOtps(String email){
      Optional<List<OtpTable>> otpItems=  otpRepository.findByEmail(email);
//      if(otpItems.isEmpty()){
//          return;
//      }
      if(otpItems.isPresent()){
          List<OtpTable> gotItems=otpItems.get();
          try{
              List<OtpTable> newItems=   gotItems.stream().filter((item)-> item.getEmail().equals(email) && item.getExpiryDateTime()
                      .isBefore(LocalDateTime.now())).toList();
              newItems .forEach((item) -> {
                  otpRepository.deleteById(item.getId());
              });
              gotItems.removeAll(newItems);
              otpRepository.saveAll(gotItems);
          }catch (Exception ex){
              log.info(ex.toString());
              throw ex;
          }

      }



    }

}
