package com.ylitormatech.sensinglog.data.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Perttu Vanharanta on 1.6.2016.
 */
@Table (name="sensor")
@Entity
public class ApiKeyEntity {

    @Id
    @GeneratedValue
    @Column(name="APIKEY_ID", unique = true, nullable = false)
    private Integer apikeyId;

    @Column(name="APIKEY", unique = true, nullable = false, length = 100)
    private String apikey;

    @Column(name="SENSOR_ID", unique = true, nullable = false)
    private Integer sensorId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sensordata_map", catalog="sensorlog", joinColumns = {
        @JoinColumn(name = "APIKEY_ID", nullable = false, updatable = false)},
        inverseJoinColumns = { @JoinColumn(name="DATATYPE_ID", nullable = false,
        updatable = false) })
    private Set<SensorDataTypeEntity> datatypes;

    private boolean isActivated;

    public ApiKeyEntity() {
        datatypes = new HashSet<SensorDataTypeEntity>(0);
        isActivated = false;
    }

    public ApiKeyEntity(String apikey, Integer sensorId ) {
        this.apikey = apikey;
        this.sensorId = sensorId;
        datatypes = new HashSet<SensorDataTypeEntity>(0);
        isActivated = false;
    }

    public Integer getApikeyId() {
        return apikeyId;
    }

    public void setApikeyId(Integer apikeyId) {
        this.apikeyId = apikeyId;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public Integer getSensorId() {
        return sensorId;
    }

    public void setSensorId(Integer sensorId) {
        this.sensorId = sensorId;
    }

    public Set<SensorDataTypeEntity> getDatatypes() {
        return datatypes;
    }

    public void setDatatypes(Set<SensorDataTypeEntity> datatypes) {
        this.datatypes = datatypes;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }

    /*public boolean isType(String dtype) {
        boolean fRet = false;
        for ( String type : datatypes ) {
            if (dtype.equals(type)) {
                fRet = true;
                break;
            }
        }
        return fRet;
    }*/
}
