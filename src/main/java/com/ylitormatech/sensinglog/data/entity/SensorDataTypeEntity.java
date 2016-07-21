package com.ylitormatech.sensinglog.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mika on 14.7.2016.
 */

@Table(name="SENSORDATATYPE")
@Entity
public class SensorDataTypeEntity {

    @Id
    @GeneratedValue
    @Column(name = "DATATYPE_ID", unique = true, nullable = false)
    private Integer dataTypeId;

    @Column(name = "DATATYPE", nullable = false, length = 40)
    private String dataType;

    @ManyToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<ApiKeyEntity> apiKeyEntities;


    public SensorDataTypeEntity() {
        this.apiKeyEntities = new HashSet<ApiKeyEntity>();

    }
    public SensorDataTypeEntity(String dataType) {
        this.dataType = dataType;
        this.apiKeyEntities = new HashSet<ApiKeyEntity>();
    }

    public Integer getDataTypeId() {
        return dataTypeId;
    }
    public void setDataTypeId(Integer dataTypeId) {
        this.dataTypeId = dataTypeId;
    }

    public String getDataType() {
        return dataType;
    }
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Set<ApiKeyEntity> getApiKeyEntities() {
        return apiKeyEntities;
    }
    public void setApiKeyEntities(Set<ApiKeyEntity> apiKeyEntities) {
        this.apiKeyEntities = apiKeyEntities;
    }

}
