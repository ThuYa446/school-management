package com.astarel.school.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.astarel.school.model.dto.CustomUserDetails;
import com.astarel.school.model.entity.User;
import com.astarel.school.repository.UserRepository;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty()){
            log.error("Email not found: " + email);
            throw new UsernameNotFoundException("could not found email..!!");
        }
        log.info("User Authenticated Successfully..!!!");
        return new CustomUserDetails(user.get());
    }
}
