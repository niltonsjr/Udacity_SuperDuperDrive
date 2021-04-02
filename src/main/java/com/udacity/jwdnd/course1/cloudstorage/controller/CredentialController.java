package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.DTO.CredentialDTO;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.ResultMessage;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.model.enums.ResultMessageType;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import com.udacity.jwdnd.course1.cloudstorage.services.UtilService;
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
    private final UtilService utilService;


    public CredentialController(EncryptionService encryptionService, CredentialService credentialService, UserService userService, UtilService utilService) {
        this.encryptionService = encryptionService;
        this.credentialService = credentialService;
        this.userService = userService;
        this.utilService = utilService;
    }

    @PostMapping(value = "/add")
    public String addCredential(@ModelAttribute("credentialDTO") CredentialDTO credentialDTO, Model model, Authentication auth) {
        ResultMessage message = new ResultMessage();
        if (credentialDTO.getCredentialId() == null) {
            int result = credentialService.insertCredential(credentialDTO, auth);
            if (result < 1) {
                message.setMessageType(ResultMessageType.ERROR);
                message.setMessage("There was an error inserting the credential.");
            } else {
                message.setMessageType(ResultMessageType.SUCCESS);
                message.setMessage("Credential added successfully.");
            }
        } else {
            Credential credential = credentialService.getCredentialById(credentialDTO.getCredentialId());
            if (utilService.checkSameUser(credential.getUserId(), auth)) {
                int result = credentialService.updateCredential(credentialDTO);
                if (result < 1) {
                    message.setMessageType(ResultMessageType.ERROR);
                    message.setMessage("There was an error updating the credential.");
                } else {
                    message.setMessageType(ResultMessageType.SUCCESS);
                    message.setMessage("Credential updated successfully.");
                }
            }
        }
        model.addAttribute("resultMessage", message);
        return "/result";
    }

    @GetMapping(value = "/delete/{credentialId}")
    public String deleteCredential(@PathVariable("credentialId") int credentialId, Model model, Authentication auth) {
        ResultMessage message = new ResultMessage();

        Credential credential = credentialService.getCredentialById(credentialId);

        if (utilService.checkSameUser(credential.getUserId(), auth)) {
            int deleted = credentialService.deleteCredential(credentialId);
            if (deleted < 1) {
                message.setMessageType(ResultMessageType.ERROR);
                message.setMessage("There was an error deleting de credential.");
            } else {
                message.setMessageType(ResultMessageType.SUCCESS);
                message.setMessage("The credential was deleted successfully.");
            }
        } else {
            message.setMessageType(ResultMessageType.ERROR);
            message.setMessage("You can't delete another user's note.");
        }

        model.addAttribute("resultMessage", message);
        return "/result";
    }
}
