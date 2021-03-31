package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.DTO.CredentialDTO;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("home/credential")
public class CredentialController {

    private final EncryptionService encryptionService;
    private final CredentialService credentialService;
    private final UserService userService;

    public CredentialController(EncryptionService encryptionService, CredentialService credentialService, UserService userService) {
        this.encryptionService = encryptionService;
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @ModelAttribute
    public CredentialDTO getCredentialDTO() {
        return new CredentialDTO();
    }

    @GetMapping
    public String getCredentialList(@ModelAttribute("credentialDTO") CredentialDTO credential, Model model, Authentication auth) {
        String username = auth.getName();
        User user = userService.getUserByUsername(username);
        int userId = user.getUserId();

        model.addAttribute("credential.username", username);
        model.addAttribute("credential.password", credential.getPassword());

        return "/home";
    }

    @PostMapping(value = "/add")
    public String addCredential(@ModelAttribute("credentialDTO") CredentialDTO credential, Model model, Authentication auth) {
        String message;
        if (credential.getCredentialId() == null) {
            credentialService.insertCredential(credential, auth);
            model.addAttribute("updateSuccess", true);
        } else {
            credentialService.updateCredential(credential);
            message = "Credential updated successfully";
            model.addAttribute("updateSuccess", message);
        }
        return "/result";
    }

    @GetMapping(value ="/delete/{credentialId}")
    public String deleteCredential(@PathVariable int credentialId, Model model) {
        int deleted = credentialService.deleteCredential(credentialId);
        String message = null;
        if (deleted < 1) {
            message = "There was an error deleting de credential";
        }
        if (message == null) {
            model.addAttribute("updateSuccess", true);
        }
        else {
            model.addAttribute("updateFailed", message);
        }
        return "/result";

    }


}
