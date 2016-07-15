package com.ylitormatech.sensinglog.data.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

/**
 * Created by Perttu Vanharanta on 1.6.2016.
 */


// OBSOLETE: ApiKeyEntity sisältää jo kaiken tarvittavan!!!!


@Table (name="SENSOR")
@Entity
public class SensorEntity {

    private Long dataid;
    //private ApiKeyEntity apikey;

    String APIKey;

    private Integer apiKeyId;

    Double value;

    @Id
    @GeneratedValue(generator="myGenerator")
    @GenericGenerator(name="myGenerator", strategy="foreign", parameters= @Parameter(value="apikey", name = "property"))
    public Long getId() {
        return dataid;
    }
    public void setId(Long dataid) {
        this.dataid = dataid;
    }

    public String getAPIKey() {
        return APIKey;
    }
    public void setAPIKey(String APIKey) {
        this.APIKey = APIKey;
    }


    public Double getValue() {
        return value;
    }
    public void setValue(Double value) {
        this.value = value;
    }

}
