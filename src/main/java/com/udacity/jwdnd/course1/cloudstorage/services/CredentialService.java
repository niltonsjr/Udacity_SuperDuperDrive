package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.DTO.CredentialDTO;
import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {

    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;
    private final UserService userService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService, UserService userService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
        this.userService = userService;
    }

    public int insertCredential(CredentialDTO credentialDTO, Authentication auth) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[16];
        secureRandom.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credentialDTO.getPassword(), encodedKey);
        //Add in new Credential
        User user = userService.getUserByUsername(auth.getName());
        int userId = user.getUserId();
        return credentialMapper.insertCredential(new Credential(null, credentialDTO.getUrl(), credentialDTO.getUsername(), encodedKey, encryptedPassword, userId));
    }

    public List<Credential> getAllCredentials(Integer userId) {
        return credentialMapper.getAllCredentials(userId);
    }

    public Credential getCredentialById(Integer credentialId) {
        return credentialMapper.getCredentialById(credentialId);
    }

    public int updateCredential(CredentialDTO credentialDTO) {
        Credential credential = credentialMapper.getCredentialById(credentialDTO.getCredentialId());
        String encryptedPassword = encryptionService.encryptValue(credentialDTO.getPassword(), credential.getKey());
        credential.setUsername(credentialDTO.getUsername());
        credential.setPassword(encryptedPassword);
        credential.setUrl(credentialDTO.getUrl());
        return credentialMapper.updateCredential(credential);
    }

    public int deleteCredential(Integer credentialId) {
        return credentialMapper.deleteCredential(credentialId);
    }
}
