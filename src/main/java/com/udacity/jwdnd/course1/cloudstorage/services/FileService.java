package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {

    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public int insertFile(File file) {
        return fileMapper.insertFile(file);
    }

    public List<File> getAllFiles(int userId) {
        return fileMapper.getAllFiles(userId);
    }

    public File getFileById(int fileId) {
        return fileMapper.getFileById(fileId);
    }

    public void updateFile(File file) {
        fileMapper.updateFile(file);
    }

    public void deleteFile(int fileId) {
        fileMapper.deleteFile(fileId);
    }
}
