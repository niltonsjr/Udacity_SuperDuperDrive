package com.udacity.jwdnd.course1.cloudstorage.controller.exceptions;

import com.udacity.jwdnd.course1.cloudstorage.model.ResultMessage;
import com.udacity.jwdnd.course1.cloudstorage.model.enums.ResultMessageType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

// https://www.baeldung.com/spring-maxuploadsizeexceeded

@ControllerAdvice
public class FileUploadExceptionAdvice {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(MaxUploadSizeExceededException exc, Model model) {
        ResultMessage message = new ResultMessage();
        message.setMessageType(ResultMessageType.ERROR);
        message.setMessage("The file couldn't be bigger than 5MB.");
        model.addAttribute("resultMessage", message);
        return "/result";
    }
}