package com.example.assignment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Users {
    private Integer userid;
    private String name;
    private String surname;
    private String email;
    private Date dob;
    private BigDecimal height;
    private BigDecimal weight;
    private String gender;
    private String address;
    private String postcode;
    private String levelofactivity;
    private Integer stepspermile;

    Users() {
    }

    public Integer getUserid() {
        return this.userid;
    }

    public String getName() {
        return this.name;
    }

    public String getSurname() {
        return this.surname;
    }

    public String getEmail() {
        return this.email;
    }

    public Date getDob() {
        return this.dob;
    }

    public BigDecimal getHeight() {
        return this.height;
    }

    public BigDecimal getWeight() {
        return this.weight;
    }

    public String getGender() {
        return this.gender;
    }

    public String getAddress() {
        return this.address;
    }

    public String getPostcode() {
        return this.postcode;
    }

    public String getLevelofactivity() {
        return this.levelofactivity;
    }

    public Integer getStepspermile() {
        return this.stepspermile;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }


    public void setHeight(BigDecimal height) {
        this.height = height.setScale(2, RoundingMode.CEILING);
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight.setScale(2, RoundingMode.CEILING);
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public void setLevelofactivity(String levelofactivity) {
        this.levelofactivity = levelofactivity;
    }

    public void setStepspermile(Integer stepspermile) {
        this.stepspermile = stepspermile;
    }
}
