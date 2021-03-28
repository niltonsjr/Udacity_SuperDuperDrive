package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {

    private final CredentialMapper credentialMapper;

    public CredentialService(CredentialMapper credentialMapper) {
        this.credentialMapper = credentialMapper;
    }

    public int insertCredential(Credential credential) {
        return credentialMapper.insertCredential(credential);
    }

    public List<Credential> getAllCredentials(int userId) {
        return credentialMapper.getAllCredentials(userId);
    }

    public Credential getCredentialById(int id) {
        return credentialMapper.getCredentialById(id);
    }

    public void updateCredential(Credential credential) {
        credentialMapper.updateCredential(credential);
    }

    public void deleteCredential(int credentialId) {
        credentialMapper.deleteCredential(credentialId);
    }
}
