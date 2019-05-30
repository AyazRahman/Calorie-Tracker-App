package Backendpkg;

import Backendpkg.Food;
import Backendpkg.Users;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-04-03T19:42:14")
@StaticMetamodel(Consumption.class)
public class Consumption_ { 

    public static volatile SingularAttribute<Consumption, Date> date;
    public static volatile SingularAttribute<Consumption, BigDecimal> quantity;
    public static volatile SingularAttribute<Consumption, Integer> consumptionid;
    public static volatile SingularAttribute<Consumption, Food> foodid;
    public static volatile SingularAttribute<Consumption, Users> userid;

}