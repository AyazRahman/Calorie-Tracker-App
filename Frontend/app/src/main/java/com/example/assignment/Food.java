package com.example.assignment;

import java.math.BigDecimal;

public class Food {
    private Integer foodid;
    private String name;
    private String category;
    private BigDecimal calorieamount;
    private String servingunit;
    private BigDecimal servingamount;
    private BigDecimal fat;

    Food() {
    }

    public Integer getFoodid() {
        return foodid;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getCalorieamount() {
        return calorieamount;
    }

    public BigDecimal getServingamount() {
        return servingamount;
    }

    public BigDecimal getFat() {
        return fat;
    }

    public String getServingunit() {
        return servingunit;
    }

    public void setFoodid(Integer foodid) {
        this.foodid = foodid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCalorieamount(BigDecimal calorieamount) {
        this.calorieamount = calorieamount;
    }

    public void setFat(BigDecimal fat) {
        this.fat = fat;
    }

    public void setServingamount(BigDecimal servingamount) {
        this.servingamount = servingamount;
    }

    public void setServingunit(String servingunit) {
        this.servingunit = servingunit;
    }
}
