package com.dataexpo.cbi.pushdata.entry;

import java.io.Serializable;

public class Datas implements Serializable {
    private String cipherFactor = "cbvf";
    private String cipherCheck = "CCB91D51A15A9BAF834C25F2B9EA0253";
    private Integer ts;
    private UData data;

    public String getCipherFactor() {
        return cipherFactor;
    }

    public void setCipherFactor(String cipherFactor) {
        this.cipherFactor = cipherFactor;
    }

    public String getCipherCheck() {
        return cipherCheck;
    }

    public void setCipherCheck(String cipherCheck) {
        this.cipherCheck = cipherCheck;
    }

    public Integer getTs() {
        return ts;
    }

    public void setTs(Integer ts) {
        this.ts = ts;
    }

    public UData getData() {
        return data;
    }

    public void setData(UData data) {
        this.data = data;
    }
}
