/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Backendpkg;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author muhta
 */
@Entity
@Table(name = "USERS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u")
    , @NamedQuery(name = "Users.findByUserid", query = "SELECT u FROM Users u WHERE u.userid = :userid")
    , @NamedQuery(name = "Users.findByName", query = "SELECT u FROM Users u WHERE u.name = :name")
    , @NamedQuery(name = "Users.findBySurname", query = "SELECT u FROM Users u WHERE u.surname = :surname")
    , @NamedQuery(name = "Users.findByEmail", query = "SELECT u FROM Users u WHERE u.email = :email")
    , @NamedQuery(name = "Users.findByDob", query = "SELECT u FROM Users u WHERE u.dob = :dob")
    , @NamedQuery(name = "Users.findByHeight", query = "SELECT u FROM Users u WHERE u.height = :height")
    , @NamedQuery(name = "Users.findByWeight", query = "SELECT u FROM Users u WHERE u.weight = :weight")
    , @NamedQuery(name = "Users.findByGender", query = "SELECT u FROM Users u WHERE u.gender = :gender")
    , @NamedQuery(name = "Users.findByAddress", query = "SELECT u FROM Users u WHERE u.address = :address")
    , @NamedQuery(name = "Users.findByPostcode", query = "SELECT u FROM Users u WHERE u.postcode = :postcode")
    , @NamedQuery(name = "Users.findByLevelofactivity", query = "SELECT u FROM Users u WHERE u.levelofactivity = :levelofactivity")
    , @NamedQuery(name = "Users.findByStepspermile", query = "SELECT u FROM Users u WHERE u.stepspermile = :stepspermile")})
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "USERID")
    private Integer userid;
    @Size(max = 50)
    @Column(name = "NAME")
    private String name;
    @Size(max = 50)
    @Column(name = "SURNAME")
    private String surname;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 100)
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "DOB")
    @Temporal(TemporalType.DATE)
    private Date dob;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "HEIGHT")
    private BigDecimal height;
    @Column(name = "WEIGHT")
    private BigDecimal weight;
    @Size(max = 1)
    @Column(name = "GENDER")
    private String gender;
    @Size(max = 200)
    @Column(name = "ADDRESS")
    private String address;
    @Size(max = 4)
    @Column(name = "POSTCODE")
    private String postcode;
    @Size(max = 1)
    @Column(name = "LEVELOFACTIVITY")
    private String levelofactivity;
    @Column(name = "STEPSPERMILE")
    private Integer stepspermile;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userid")
    private Collection<Credentials> credentialsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userid")
    private Collection<Report> reportCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userid")
    private Collection<Consumption> consumptionCollection;

    public Users() {
    }

    public Users(Integer userid) {
        this.userid = userid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getLevelofactivity() {
        return levelofactivity;
    }

    public void setLevelofactivity(String levelofactivity) {
        this.levelofactivity = levelofactivity;
    }

    public Integer getStepspermile() {
        return stepspermile;
    }

    public void setStepspermile(Integer stepspermile) {
        this.stepspermile = stepspermile;
    }

    @XmlTransient
    public Collection<Credentials> getCredentialsCollection() {
        return credentialsCollection;
    }

    public void setCredentialsCollection(Collection<Credentials> credentialsCollection) {
        this.credentialsCollection = credentialsCollection;
    }

    @XmlTransient
    public Collection<Report> getReportCollection() {
        return reportCollection;
    }

    public void setReportCollection(Collection<Report> reportCollection) {
        this.reportCollection = reportCollection;
    }

    @XmlTransient
    public Collection<Consumption> getConsumptionCollection() {
        return consumptionCollection;
    }

    public void setConsumptionCollection(Collection<Consumption> consumptionCollection) {
        this.consumptionCollection = consumptionCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userid != null ? userid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.userid == null && other.userid != null) || (this.userid != null && !this.userid.equals(other.userid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Backendpkg.Users[ userid=" + userid + " ]";
    }
    
}
