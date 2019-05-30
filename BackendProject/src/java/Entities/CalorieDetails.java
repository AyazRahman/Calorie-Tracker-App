/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

/**
 *
 * @author muhta
 */
import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class CalorieDetails {
    double calorieConsumed;
    double calorieBurned;
    double calorieRemaining;
    
    public double getCalorieConsumed(){return this.calorieConsumed;}
    public void setCalorieConsumed(BigDecimal calorieConsumed){this.calorieConsumed=calorieConsumed.doubleValue();}
    
    public double getCalorieBurned(){return this.calorieBurned;}
    public void setCalorieBurned(BigDecimal calorieBurned){this.calorieBurned=calorieBurned.doubleValue();}
    
    public double getCalorieRemaining(){return this.calorieRemaining;}
    public void setCalorieRemaining(BigDecimal calorieRemaining){this.calorieRemaining=calorieRemaining.doubleValue();}
    
    public CalorieDetails(){}
    
    public CalorieDetails(BigDecimal calorieConsumed, BigDecimal calorieBurned, BigDecimal calorieGoal){
        this.calorieConsumed = calorieConsumed.doubleValue();
        this.calorieBurned = calorieBurned.doubleValue();
        this.calorieRemaining = calorieGoal.doubleValue() - (calorieBurned.doubleValue() - calorieConsumed.doubleValue());
    }
}
