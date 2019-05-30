package Backendpkg;

import Backendpkg.Consumption;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-04-03T19:42:14")
@StaticMetamodel(Food.class)
public class Food_ { 

    public static volatile CollectionAttribute<Food, Consumption> consumptionCollection;
    public static volatile SingularAttribute<Food, BigDecimal> calorieamount;
    public static volatile SingularAttribute<Food, BigDecimal> servingamount;
    public static volatile SingularAttribute<Food, Integer> foodid;
    public static volatile SingularAttribute<Food, String> name;
    public static volatile SingularAttribute<Food, BigDecimal> fat;
    public static volatile SingularAttribute<Food, String> category;
    public static volatile SingularAttribute<Food, String> servingunit;

}