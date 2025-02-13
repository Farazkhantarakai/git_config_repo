package com.tech.gateway.ServiceImplementation;


import com.tech.gateway.model.MyUser;
import com.tech.gateway.repositories.UserRepository;
import com.tech.gateway.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ServiceImplementation implements UserService {

    private UserRepository userRepository;


    @Override
    public Optional<MyUser> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    //this will store user to repository
    @Override
    public void save(MyUser user) {
 userRepository.save(user);
    }
}
