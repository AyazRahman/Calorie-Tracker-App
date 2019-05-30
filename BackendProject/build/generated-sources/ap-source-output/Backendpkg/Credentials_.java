package Backendpkg;

import Backendpkg.Users;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-04-03T19:42:14")
@StaticMetamodel(Credentials.class)
public class Credentials_ { 

    public static volatile SingularAttribute<Credentials, Date> signupdate;
    public static volatile SingularAttribute<Credentials, String> passwordhash;
    public static volatile SingularAttribute<Credentials, Users> userid;
    public static volatile SingularAttribute<Credentials, String> username;

}