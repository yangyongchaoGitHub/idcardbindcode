package com.dataexpo.cbi.pushdata.entry;

import java.io.Serializable;

public class UData implements Serializable {
    private Visitor visitor;
    private String[] attachments;
    private Operator operator;

    public Visitor getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }

    public String[] getAttachments() {
        return attachments;
    }

    public void setAttachments(String[] attachments) {
        this.attachments = attachments;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }
}
