package com.ylitormatech.sensinglog.data.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Perttu Vanharanta on 1.6.2016.
 */
@Table //(name="APIKEY")
@Entity
public class ApiKeyEntity {

    @Id
    @GeneratedValue
    Integer id;

    String apikey;

    //List<String> datatypes;

    boolean isActivate;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    /*
    public List<String> getDatatypes() {
        return datatypes;
    }
    public void setDatatypes(List<String> datatypes) {
        this.datatypes = datatypes;
    }
    */

    public String getApikey() {
        return apikey;
    }
    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public boolean isActivate() {
        return isActivate;
    }
    public void setActivate(boolean activate) {
        isActivate = activate;
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
