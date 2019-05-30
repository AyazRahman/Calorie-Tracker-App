package com.example.assignment;

import java.math.BigDecimal;
import java.util.Date;

public class Report {
    private Integer reportid;
    private Date date;
    private BigDecimal caloriesconsumed;
    private BigDecimal caloriesburned;
    private int stepstaken;
    private BigDecimal caloriegoal;
    private Users userid;

    Report() {
    }

    public Integer getReportid() {
        return this.reportid;
    }

    public Date getDate() {
        return this.date;
    }

    public BigDecimal getCaloriegoal() {
        return caloriegoal;
    }

    public BigDecimal getCaloriesburned() {
        return caloriesburned;
    }

    public BigDecimal getCaloriesconsumed() {
        return caloriesconsumed;
    }

    public int getStepstaken() {
        return stepstaken;
    }

    public Users getUserid() {
        return userid;
    }

    public void setCaloriegoal(BigDecimal caloriegoal) {
        this.caloriegoal = caloriegoal;
    }

    public void setCaloriesburned(BigDecimal caloriesburned) {
        this.caloriesburned = caloriesburned;
    }

    public void setCaloriesconsumed(BigDecimal caloriesconsumed) {
        this.caloriesconsumed = caloriesconsumed;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setReportid(Integer reportid) {
        this.reportid = reportid;
    }

    public void setStepstaken(int stepstaken) {
        this.stepstaken = stepstaken;
    }

    public void setUserid(Users userid) {
        this.userid = userid;
    }
}
