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
public class SensorLogEntity {
    @Id
    @GeneratedValue
    Integer id;

    String APIKey;

    //Double value;

    // Other possible data members (nice to have / needed):
    //  owner:                  User owning the sensor
    //  allowedUsers:           List of users allowed to follow this sensor
    //  HashMap<S,S> datatypes: list of sensor data types with names in the order
    //                          they are stored in SensorLogDataEntity.
    //  ArrayList<s> datatypes: the same as above, but without names.
    //  String info:            Info about the sensor

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getAPIKey() {
        return APIKey;
    }
    public void setAPIKey(String APIKey) {
        this.APIKey = APIKey;
    }

    /*
    public Double getValue() {
        return value;
    }
    //public void setValue(Double value) {
        this.value = value;
    }
    */
}
