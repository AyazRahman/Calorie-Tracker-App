/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Backendpkg;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author muhta
 */
@Entity
@Table(name = "REPORT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Report.findAll", query = "SELECT r FROM Report r")
    , @NamedQuery(name = "Report.findByReportid", query = "SELECT r FROM Report r WHERE r.reportid = :reportid")
    , @NamedQuery(name = "Report.findByDate", query = "SELECT r FROM Report r WHERE r.date = :date")
    , @NamedQuery(name = "Report.findByCaloriesconsumed", query = "SELECT r FROM Report r WHERE r.caloriesconsumed = :caloriesconsumed")
    , @NamedQuery(name = "Report.findByCaloriesburned", query = "SELECT r FROM Report r WHERE r.caloriesburned = :caloriesburned")
    , @NamedQuery(name = "Report.findByStepstaken", query = "SELECT r FROM Report r WHERE r.stepstaken = :stepstaken")
    , @NamedQuery(name = "Report.findByCaloriegoal", query = "SELECT r FROM Report r WHERE r.caloriegoal = :caloriegoal")
    , @NamedQuery(name = "Report.findByEmailAndDate", query = "SELECT r FROM Report r WHERE r.userid.email = :email AND r.date = :date")})
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "REPORTID")
    private Integer reportid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DATE")
    @Temporal(TemporalType.DATE)
    private Date date;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "CALORIESCONSUMED")
    private BigDecimal caloriesconsumed;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CALORIESBURNED")
    private BigDecimal caloriesburned;
    @Basic(optional = false)
    @NotNull
    @Column(name = "STEPSTAKEN")
    private int stepstaken;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CALORIEGOAL")
    private BigDecimal caloriegoal;
    @JoinColumn(name = "USERID", referencedColumnName = "USERID")
    @ManyToOne(optional = false)
    private Users userid;

    public Report() {
    }

    public Report(Integer reportid) {
        this.reportid = reportid;
    }

    public Report(Integer reportid, Date date, BigDecimal caloriesconsumed, BigDecimal caloriesburned, int stepstaken, BigDecimal caloriegoal) {
        this.reportid = reportid;
        this.date = date;
        this.caloriesconsumed = caloriesconsumed;
        this.caloriesburned = caloriesburned;
        this.stepstaken = stepstaken;
        this.caloriegoal = caloriegoal;
    }

    public Integer getReportid() {
        return reportid;
    }

    public void setReportid(Integer reportid) {
        this.reportid = reportid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getCaloriesconsumed() {
        return caloriesconsumed;
    }

    public void setCaloriesconsumed(BigDecimal caloriesconsumed) {
        this.caloriesconsumed = caloriesconsumed;
    }

    public BigDecimal getCaloriesburned() {
        return caloriesburned;
    }

    public void setCaloriesburned(BigDecimal caloriesburned) {
        this.caloriesburned = caloriesburned;
    }

    public int getStepstaken() {
        return stepstaken;
    }

    public void setStepstaken(int stepstaken) {
        this.stepstaken = stepstaken;
    }

    public BigDecimal getCaloriegoal() {
        return caloriegoal;
    }

    public void setCaloriegoal(BigDecimal caloriegoal) {
        this.caloriegoal = caloriegoal;
    }

    public Users getUserid() {
        return userid;
    }

    public void setUserid(Users userid) {
        this.userid = userid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reportid != null ? reportid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Report)) {
            return false;
        }
        Report other = (Report) object;
        if ((this.reportid == null && other.reportid != null) || (this.reportid != null && !this.reportid.equals(other.reportid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Backendpkg.Report[ reportid=" + reportid + " ]";
    }
    
}
