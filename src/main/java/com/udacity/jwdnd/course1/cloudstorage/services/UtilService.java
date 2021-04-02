package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UtilService {

    private UserService userService;

    public UtilService(UserService userService) {
        this.userService = userService;
    }

    public boolean checkSameUser(int userId, Authentication auth){
        User user =  userService.getUserByUsername(auth.getName());
        if (userId == user.getUserId()) {
            return true;
        }
        return false;
    }
}
