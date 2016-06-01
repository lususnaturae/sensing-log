package com.ylitormatech.sensinglog.service.data;

import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Created by Perttu Vanharanta on 31.5.2016.
 */
public class Calendar {

    Integer id;
    String name;

    @DateTimeFormat(pattern = "yyyy.MM.dd")
    DateTime dt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DateTime getDt() {
        return dt;
    }

    public void setDt(DateTime dt) {
        this.dt = dt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
