package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.DTO.NoteDTO;
import com.udacity.jwdnd.course1.cloudstorage.model.ResultMessage;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.model.enums.ResultMessageType;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.boot.Banner;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "home/notes")
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    /*
    @GetMapping
    public String getNotesList(@ModelAttribute("noteDTO")NoteDTO noteDTO, Model model, Authentication auth) {
        String username = auth.getName();
        User user = userService.getUserByUsername(username);
        int userId

        return "/home";
    }*/

    @PostMapping("/add")
    public String addNote(@ModelAttribute("noteDTO") NoteDTO noteDTO, Model model, Authentication auth) {
        ResultMessage message = new ResultMessage();
        if (noteDTO.getNoteId() == null) {
            int result = noteService.insertNote(noteDTO, auth);
            if (result < 1) {
                message.setMessageType(ResultMessageType.ERROR);
                message.setMessage("There was an error inserting the note.");
            } else {
                message.setMessageType(ResultMessageType.SUCCESS);
                message.setMessage("Note added successfully");
            }
        } else {
            int result = noteService.updateNote(noteDTO);
            if (result < 1) {
                message.setMessageType(ResultMessageType.ERROR);
                message.setMessage("There was an error updating the note.");
            } else {
                message.setMessageType(ResultMessageType.SUCCESS);
                message.setMessage("Note updated successfully");
            }
        }
        model.addAttribute("resultMessage", message);
        return "/result";
    }
}
