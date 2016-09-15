package org.religion.umbanda.tad.service.vo;

public class FinancialReceiptResultVO {

    private String id;
    private String created;
    private String sent;
    private String status;
    private CollaboratorVO target;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getSent() {
        return sent;
    }

    public void setSent(String sent) {
        this.sent = sent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CollaboratorVO getTarget() {
        return target;
    }

    public void setTarget(CollaboratorVO target) {
        this.target = target;
    }
}
