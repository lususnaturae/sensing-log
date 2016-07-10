package com.ylitormatech.sensinglog.data.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


/**
 * Created by mika on 9.6.2016.
 */

// OBSOLETE: See SensorMessage for MongoDB handling...

@Table(name="SENSORDATA")
@Entity
public class SensorDataEntity {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private Integer sensorid;


    private Long created;      // Time stamp for data creation (from sensor).

    private String data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSensorid() {
        return sensorid;
    }

    public void setSensorid(Integer sensorid) {
        this.sensorid = sensorid;
    }



    public Long getCreated() {
        return created;
    }
    public void setCreated(Long created) {
        this.created = created;
    }

    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
}
