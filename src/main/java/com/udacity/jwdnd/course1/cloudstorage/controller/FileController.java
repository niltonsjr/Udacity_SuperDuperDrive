package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.DTO.FileDTO;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.ResultMessage;
import com.udacity.jwdnd.course1.cloudstorage.model.enums.ResultMessageType;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import com.udacity.jwdnd.course1.cloudstorage.services.UtilService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping(value = "home/files")
public class FileController {

    private final UserService userService;
    private final FileService fileService;
    private final UtilService utilService;

    public FileController(UserService userService, FileService fileService, UtilService utilService) {
        this.userService = userService;
        this.fileService = fileService;
        this.utilService = utilService;
    }

    @PostMapping(value = "/add")
    public String uploadFile(@ModelAttribute("file") FileDTO fileDTO, Model model, Authentication auth) throws IOException {
        ResultMessage message = new ResultMessage();

        if (fileDTO.getFile().isEmpty()) {
            message.setMessageType(ResultMessageType.ERROR);
            message.setMessage("File should not be empty. Please select a file.");
            model.addAttribute("resultMessage", message);
            return "/result";
        }

        if (fileService.isFilenameAvailable(fileDTO.getFile().getOriginalFilename())) {
            int result = fileService.insertFile(fileDTO, auth);
            if (result < 1) {
                message.setMessageType(ResultMessageType.ERROR);
                message.setMessage("There was an error uploading the file.");
            } else {
                message.setMessageType(ResultMessageType.SUCCESS);
                message.setMessage("The file was uploaded successfully.");
            }
        } else {
            message.setMessageType(ResultMessageType.ERROR);
            message.setMessage("There is an existing file with the same name. Please change the file name.");
        }
        model.addAttribute("resultMessage", message);
        return "/result";
    }

    @GetMapping(value = "/delete/{fileId}")
    public String deleteFile(@PathVariable("fileId") int fileId, Model model, Authentication auth) {
        ResultMessage message = new ResultMessage();

        File file = fileService.getFileById(fileId);

        if (utilService.checkSameUser(file.getUserId(), auth)) {
            int result = fileService.deleteFile(fileId);
            if (result < 1) {
                message.setMessageType(ResultMessageType.ERROR);
                message.setMessage("There was an error deleting the file.");
            } else {
                message.setMessageType(ResultMessageType.SUCCESS);
                message.setMessage("The file was deleted successfully.");
            }
        } else {
            message.setMessageType(ResultMessageType.ERROR);
            message.setMessage("You can't delete another user's file.");
        }

        model.addAttribute("resultMessage", message);
        return "/result";
    }

    @GetMapping("/view/{fileId}")
    public ResponseEntity<ByteArrayResource> viewFile(@PathVariable("fileId") int fileId, Model model) {
        File file = fileService.getFileById(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline: filename=\"" + file.getFileName() + "\"")
                .body(new ByteArrayResource(file.getFileData()));
    }
}
