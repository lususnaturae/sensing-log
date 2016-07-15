package com.ylitormatech.sensinglog.service.impl;

import com.ylitormatech.sensinglog.data.entity.ApiKeyEntity;
import com.ylitormatech.sensinglog.data.repository.ApiKeyRepository;
import com.ylitormatech.sensinglog.service.ApiKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Perttu Vanharanta on 1.6.2016.
 */
@Service
public class ApiKeyServiceImpl implements ApiKeyService{

    @Autowired
    ApiKeyRepository apiKeyRepository;

    @Transactional(readOnly = false)
    public void createApiKey(String message) {
        ApiKeyEntity apiKeyEntity = new ApiKeyEntity();

        // First parse out the message from json:



        //apiKeyEntity.setApikey(key);
        apiKeyEntity.setActivated(true);









        apiKeyRepository.add(apiKeyEntity);
    }

    @Override
    public List<String> getApiKeys() {
        return null;
    }

    @Transactional(readOnly = false)
    public void updateApiKey(String key, boolean isActivated){
        ApiKeyEntity apiKeyEntity = apiKeyRepository.findEntity(key);
        apiKeyEntity.setActivated(isActivated);
        apiKeyRepository.update(apiKeyEntity);
    }

    @Transactional(readOnly = false)
    public void removeApiKey(String key){
        apiKeyRepository.remove(key);
    }

    @Transactional(readOnly = true)
    public boolean testAPIKey(String key) {
        return false;
    }

    @Transactional(readOnly = true)
    public boolean getAPIKeyId(String key){
        return apiKeyRepository.find(key);
    }

    @Transactional(readOnly = true)
    public Integer getSensorIdByApiKey(String key) {
        // Returns sensor database id if found, otherwise zero value.
        int iRet = 0;

        if (!key.isEmpty()) {
            iRet = apiKeyRepository.findSensorIdByApiKey(key);
        }
        return iRet;
    }

    @Transactional(readOnly = true)
    public Integer checkSensorValidityByApiKey(String key) {
        // Check if sensor can be found, if it is 'active'.
        // Query returns the sensor database id if valid, otherwise zero value.
        int iRet = 0;

        if (!key.isEmpty()) {
            iRet = apiKeyRepository.checkSensorValidity(key);
        }
        return iRet;
    }
}
