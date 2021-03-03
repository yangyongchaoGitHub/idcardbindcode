package com.dataexpo.cbi.pojo;

import java.io.Serializable;

public class PdaUserInfo implements Serializable {
    private Integer eid;

    private Integer uid;

    private String eucode;

    private String name;

    private String idcard;

    public Integer getEid() {
        return eid;
    }

    public void setEid(Integer eid) {
        this.eid = eid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getEucode() {
        return eucode;
    }

    public void setEucode(String eucode) {
        this.eucode = eucode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }
}
