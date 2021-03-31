package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.DTO.CredentialDTO;
import com.udacity.jwdnd.course1.cloudstorage.model.ResultMessage;
import com.udacity.jwdnd.course1.cloudstorage.model.enums.ResultMessageType;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("home/credentials")
public class CredentialController {

    private final EncryptionService encryptionService;
    private final CredentialService credentialService;
    private final UserService userService;

    public CredentialController(EncryptionService encryptionService, CredentialService credentialService, UserService userService) {
        this.encryptionService = encryptionService;
        this.credentialService = credentialService;
        this.userService = userService;
    }

    /*
    @ModelAttribute
    public CredentialDTO getCredentialDTO() {
        return new CredentialDTO();
    }*/

  /*
    @GetMapping
    public String getCredentialList(@ModelAttribute("credentialDTO") CredentialDTO credentialDTO, Model model, Authentication auth) {
        String username = auth.getName();
        User user = userService.getUserByUsername(username);
        int userId = user.getUserId();

        model.addAttribute("credential.username", username);
        model.addAttribute("credential.password", credentialDTO.getPassword());

        return "/home";
    }*/

    @PostMapping(value = "/add")
    public String addCredential(@ModelAttribute("credentialDTO") CredentialDTO credentialDTO, Model model, Authentication auth) {
        ResultMessage message = new ResultMessage();
        if (credentialDTO.getCredentialId() == null) {
            credentialService.insertCredential(credentialDTO, auth);
            message.setMessageType(ResultMessageType.SUCCESS);
            message.setMessage("Credential added successfully.");
        } else {
            credentialService.updateCredential(credentialDTO);
            message.setMessageType(ResultMessageType.SUCCESS);
            message.setMessage("Credential updated successfully.");
        }
        model.addAttribute("resultMessage", message);
        return "/result";
    }

    @GetMapping(value = "/delete/{credentialId}")
    public String deleteCredential(@PathVariable("credentialId") int credentialId, Model model) {
        int deleted = credentialService.deleteCredential(credentialId);
        ResultMessage message = new ResultMessage();
        if (deleted < 1) {
            message.setMessageType(ResultMessageType.ERROR);
            message.setMessage("There was an error deleting de credential.");
        }
        else {
            message.setMessageType(ResultMessageType.SUCCESS);
            message.setMessage("The credential was deleted successfully.");
        }
        model.addAttribute("resultMessage", message);
        return "/result";

    }


}
