package com.ylitormatech.sensinglog.service.impl;

import com.ylitormatech.sensinglog.data.entity.ApiKeyEntity;
import com.ylitormatech.sensinglog.data.repository.ApiKeyRepository;
import com.ylitormatech.sensinglog.service.ApiKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Perttu Vanharanta on 1.6.2016.
 */
@Service
public class ApiKeyServiceImpl implements ApiKeyService{

    @Autowired
    ApiKeyRepository apiKeyRepository;

    @Transactional(readOnly = false)
    public void createApiKey(String key) {
        ApiKeyEntity apiKeyEntity = new ApiKeyEntity();
        apiKeyEntity.setApikey(key);
        apiKeyRepository.add(apiKeyEntity);
    }

    @Transactional(readOnly = false)
    public boolean getAPIKeyId(String id){
        ApiKeyEntity apiKeyEntity;
        apiKeyEntity = apiKeyRepository.find(id);
        if(apiKeyEntity != null){
            return true;
        }
        return false;
    }
}
