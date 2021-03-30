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
@RequestMapping("/credential")
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

    // DECRYPT password by to show decrypted password when open up modal on frontend:
    // does NOT return thymeleaf template because we return decrypted password as HTTP response value:
    @GetMapping("/home/credential/decrypt-password/{credentialId}")
    public ResponseEntity<String> decryptPassword(@PathVariable("credentialId") int credentialId) throws IOException {
        Credential credential = this.credentialService.getCredentialById(credentialId);
        // use .decryptValue in EncryptionService to decrypt password:
        String decryptedPassword = this.encryptionService.decryptValue(credential.getPassword(), credential.getKey());
        System.out.println("Decrypted password " + decryptedPassword);

        // Reference: https://www.baeldung.com/spring-response-entity
        // return the decrypted password back as HTTP response:
        return ResponseEntity.ok(decryptedPassword);
    }

}
