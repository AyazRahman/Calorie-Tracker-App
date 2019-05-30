package Backendpkg;

import Backendpkg.Consumption;
import Backendpkg.Credentials;
import Backendpkg.Report;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-04-03T19:42:14")
@StaticMetamodel(Users.class)
public class Users_ { 

    public static volatile SingularAttribute<Users, String> address;
    public static volatile SingularAttribute<Users, String> gender;
    public static volatile CollectionAttribute<Users, Credentials> credentialsCollection;
    public static volatile CollectionAttribute<Users, Consumption> consumptionCollection;
    public static volatile SingularAttribute<Users, Integer> stepspermile;
    public static volatile SingularAttribute<Users, String> postcode;
    public static volatile SingularAttribute<Users, BigDecimal> weight;
    public static volatile SingularAttribute<Users, Integer> userid;
    public static volatile CollectionAttribute<Users, Report> reportCollection;
    public static volatile SingularAttribute<Users, String> surname;
    public static volatile SingularAttribute<Users, Date> dob;
    public static volatile SingularAttribute<Users, String> name;
    public static volatile SingularAttribute<Users, String> levelofactivity;
    public static volatile SingularAttribute<Users, String> email;
    public static volatile SingularAttribute<Users, BigDecimal> height;

}