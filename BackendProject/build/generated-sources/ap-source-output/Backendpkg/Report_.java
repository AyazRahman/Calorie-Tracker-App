package Backendpkg;

import Backendpkg.Users;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-04-03T19:42:14")
@StaticMetamodel(Report.class)
public class Report_ { 

    public static volatile SingularAttribute<Report, Date> date;
    public static volatile SingularAttribute<Report, Integer> reportid;
    public static volatile SingularAttribute<Report, BigDecimal> caloriesburned;
    public static volatile SingularAttribute<Report, BigDecimal> caloriegoal;
    public static volatile SingularAttribute<Report, BigDecimal> caloriesconsumed;
    public static volatile SingularAttribute<Report, Users> userid;
    public static volatile SingularAttribute<Report, Integer> stepstaken;

}