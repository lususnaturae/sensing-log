package com.ylitormatech.sensinglog.data.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Perttu Vanharanta on 1.6.2016.
 */
@Table
@Entity
public class ApiKeyEntity {

    @Id
    @GeneratedValue
    Integer id;

    String apikey;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }
}
