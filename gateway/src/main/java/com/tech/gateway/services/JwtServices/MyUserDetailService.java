package com.tech.gateway.services.JwtServices;


import com.tech.gateway.model.MyUser;
import com.tech.gateway.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Service
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

    Optional<MyUser> user=    userRepository.findByEmail(email);

    if(user.isPresent()) {

   var userObj=user.get();

   return User.builder().username(userObj.getEmail()).password(userObj.getPassword()).roles(getRole(userObj)).build();
    }else {
        throw new UsernameNotFoundException(email);
    }
    }






    private String[]   getRole(MyUser user) {

        if(user.getRoles()==null ) {
            return new String[]{"USER"};
        }else {
         return    user.getRoles().split(",");
        }

    }


}
