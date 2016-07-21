package com.ylitormatech.sensinglog.data.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Perttu Vanharanta on 1.6.2016.
 */
@Table (name="APIKEY")
@Entity
public class ApiKeyEntity {

    @Id
    @GeneratedValue
    @Column(name="APIKEY_ID", unique = true, nullable = false)
    private Integer apikeyId;

    @Column(name="SENSOR_ID", unique = true, nullable = false)
    private Integer sensorId;

    @Column(name="APIKEY", length = 100)
    private String apikey;

    @Column(name="ACTIVATED", nullable = false)
    private boolean active;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name="APIKEY_DATATYPE",
               joinColumns = @JoinColumn(name="APIKEY_ID", referencedColumnName = "APIKEY_ID"),
               inverseJoinColumns = @JoinColumn(name="DATATYPE_ID", referencedColumnName = "DATATYPE_ID"))
    private Set<SensorDataTypeEntity> datatypes;


    public ApiKeyEntity() {
        datatypes = new HashSet<SensorDataTypeEntity>();
        active = false;
    }

    public ApiKeyEntity(String apikey, Integer sensorId ) {
        this.apikey = apikey;
        this.sensorId = sensorId;
        datatypes = new HashSet<SensorDataTypeEntity>();
        active = false;
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
    public void addDataType(SensorDataTypeEntity datatype) { this.datatypes.add(datatype); }

    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

}
