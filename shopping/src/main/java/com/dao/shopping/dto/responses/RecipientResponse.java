package com.dao.shopping.dto.responses;


public class RecipientResponse {
    private Integer id;
    private String name;
    private String phone;
    private AuditResponse audit;

    public RecipientResponse() {
    }

    public RecipientResponse(Integer id, String name, String phone, AuditResponse audit) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.audit = audit;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public AuditResponse getAudit() {
        return audit;
    }

    public void setAudit(AuditResponse audit) {
        this.audit = audit;
    }
}
