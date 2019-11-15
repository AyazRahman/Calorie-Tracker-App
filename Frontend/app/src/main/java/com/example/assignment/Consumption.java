package com.example.assignment;

import java.math.BigDecimal;
import java.util.Date;

public class Consumption {
    private Integer consumptionid;
    private Date date;
    private BigDecimal quantity;
    private Food foodid;
    private Users userid;

    Consumption() {
    }

    public Integer getConsumptionid() {
        return consumptionid;
    }

    public Date getDate() {
        return date;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public Food getFoodid() {
        return foodid;
    }

    public Users getUserid() {
        return userid;
    }

    public void setConsumptionid(Integer consumptionid) {
        this.consumptionid = consumptionid;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public void setFoodid(Food foodid) {
        this.foodid = foodid;
    }

    public void setUserid(Users userid) {
        this.userid = userid;
    }
}
