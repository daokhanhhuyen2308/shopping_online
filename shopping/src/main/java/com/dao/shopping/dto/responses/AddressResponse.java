package com.dao.shopping.dto.responses;


public class AddressResponse{
    private Integer id;
    String houseNumber;
    String street;
    String district;
    String city;
    String zip;
    String country;
    private AuditResponse audit;

    public AddressResponse() {
    }

    public AddressResponse(Integer id, String houseNumber, String street, String district,
                           String city, String zip, String country, AuditResponse audit) {
        this.id = id;
        this.houseNumber = houseNumber;
        this.street = street;
        this.district = district;
        this.city = city;
        this.zip = zip;
        this.country = country;
        this.audit = audit;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public AuditResponse getAudit() {
        return audit;
    }

    public void setAudit(AuditResponse audit) {
        this.audit = audit;
    }
}
