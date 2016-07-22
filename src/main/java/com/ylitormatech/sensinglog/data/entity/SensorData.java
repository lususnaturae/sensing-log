package com.ylitormatech.sensinglog.data.entity;

import org.springframework.core.style.ToStringCreator;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by mika on 22.7.2016.
 */

@Document(collection = "sensordata")
public class SensorData {
    //@Id
    //private Long msgId;

    @Id
    //@GeneratedValue(generator = "uuid")
    //@GenericGenerator(name = "uuid", strategy = "uuid2")
    private String msgId;

    @Indexed
    private Integer sensorid;

    private Long timestamp;

    private String datatype;

    private String value;


    /*public Long getMsgId() {
        return msgId;
    }
    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }*/

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public Integer getSensorid() {
        return sensorid;
    }
    public void setSensorid(Integer sensorid) {
        this.sensorid = sensorid;
    }

    public Long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDatatype() {
        return datatype;
    }
    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append("sensorid", this.getSensorid())
                .append("timestamp", this.getTimestamp())
                .append("datatype", this.getDatatype())
                .append("value", this.getValue())
                .toString();
    }
}
