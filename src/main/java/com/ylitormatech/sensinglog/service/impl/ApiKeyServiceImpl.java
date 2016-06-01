package com.ylitormatech.sensinglog.service.impl;

import com.ylitormatech.sensinglog.data.repository.ApiKeyRepository;
import com.ylitormatech.sensinglog.service.ApiKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Perttu Vanharanta on 1.6.2016.
 */
@Service
public class ApiKeyServiceImpl implements ApiKeyService{

    @Autowired
    ApiKeyRepository apiKeyRepository;

    public void createApiKey() {
        /*
        * Put received apikey database
        */
    }

    public boolean getAPIKeyId(String id) {
        /*
        * Find apikey from database. If found return true else false
        */
        return true;
    }
}
