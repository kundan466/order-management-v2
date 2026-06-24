package com.order_management.demo.Utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.order_management.demo.entity.User;

@Component
public class CurrentUserDetails {

    public User getUserDetails(){
        Authentication authentication =SecurityContextHolder
            .getContext()
            .getAuthentication();

    User loggedInUser =(User) authentication.getPrincipal();
    return loggedInUser;

    }
    
}
