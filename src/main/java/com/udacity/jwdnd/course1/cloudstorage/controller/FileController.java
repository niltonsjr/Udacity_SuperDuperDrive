package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.DTO.FileDTO;
import com.udacity.jwdnd.course1.cloudstorage.model.ResultMessage;
import com.udacity.jwdnd.course1.cloudstorage.model.enums.ResultMessageType;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping(value = "home/files")
public class FileController {

    private final UserService userService;
    private final FileService fileService;

    public FileController(UserService userService, FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }

    @ModelAttribute("fileDTO")
    public FileDTO getFileDTO() {
        return new FileDTO();
    }

    @PostMapping(value = "/add")
    public String uploadFile(@ModelAttribute("file") FileDTO fileDTO, Model model, Authentication auth) throws IOException {
        ResultMessage message = new ResultMessage();

        if (fileDTO.getFile().isEmpty()){
            message.setMessageType(ResultMessageType.ERROR);
            message.setMessage("File should not be empty.");
            model.addAttribute("updateFailed", message);
            return "/result";
        }

        if (fileService.isFilenameAvailable(fileDTO.getFile().getOriginalFilename())) {
            int result = fileService.insertFile(fileDTO, auth);
            if (result < 1) {
                message.setMessageType(ResultMessageType.ERROR);
                message.setMessage("There was an error uploading the file.");
            }
            message.setMessageType(ResultMessageType.SUCCESS);
            message.setMessage("The file was uploaded successfully");
        } else {
            message.setMessageType(ResultMessageType.ERROR);
            message.setMessage("There is an existing file with the same name. Please change the file name.");
        }
        model.addAttribute("resultMessage", message);
        return "/result";
    }
}
