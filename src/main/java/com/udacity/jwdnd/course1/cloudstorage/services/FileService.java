package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.DTO.FileDTO;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    private final FileMapper fileMapper;
    private final UserService userService;

    public FileService(FileMapper fileMapper, UserService userService) {
        this.fileMapper = fileMapper;
        this.userService = userService;
    }

    public int insertFile(FileDTO fileDTO, Authentication auth) throws IOException {
        User user = userService.getUserByUsername(auth.getName());

        String filename = fileDTO.getFile().getOriginalFilename();
        String contentType = fileDTO.getFile().getContentType();
        String fileSize = String.valueOf(fileDTO.getFile().getSize());
        byte[] fileData = fileDTO.getFile().getBytes();

        File file = new File(null, filename, contentType, fileSize, user.getUserId(), fileData);

        return fileMapper.insertFile(file);
    }

    public List<File> getAllFiles(int userId) {
        return fileMapper.getAllFiles(userId);
    }

    public File getFileById(int fileId) {
        return fileMapper.getFileById(fileId);
    }

    public void deleteFile(int fileId) {
        fileMapper.deleteFile(fileId);
    }

    public boolean isFilenameAvailable(String filename) {
        return fileMapper.getByFilename(filename) == null;
    }
}
