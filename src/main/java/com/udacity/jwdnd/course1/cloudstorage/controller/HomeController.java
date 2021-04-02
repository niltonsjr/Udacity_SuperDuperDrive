package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.DTO.FileDTO;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final UserService userService;
    private final NoteService noteService;
    private final CredentialService credentialService;
    private final EncryptionService encryptionService;
    private final FileService fileService;

    public HomeController(UserService userService, NoteService noteService, CredentialService credentialService, EncryptionService encryptionService, FileService fileService) {
        this.userService = userService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
        this.fileService = fileService;
    }

    @GetMapping()
    public String viewHome(@ModelAttribute("fileDTO") FileDTO fileDTO, Model model, Authentication auth) throws Exception {
        User user = this.userService.getUserByUsername(auth.getName());
        int userId = user.getUserId();
        model.addAttribute("credentials", credentialService.getAllCredentials(userId));
        model.addAttribute("notes", noteService.getAllNotes(userId));
        model.addAttribute("files", fileService.getAllFiles(userId));
        model.addAttribute("encryptionService", encryptionService);
        return "home";
    }


}
