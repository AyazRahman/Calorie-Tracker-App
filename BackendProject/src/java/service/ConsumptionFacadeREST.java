/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import Backendpkg.Consumption;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author muhta
 */
@Stateless
@Path("backendpkg.consumption")
public class ConsumptionFacadeREST extends AbstractFacade<Consumption> {

    @PersistenceContext(unitName = "BackendProjectPU")
    private EntityManager em;

    public ConsumptionFacadeREST() {
        super(Consumption.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Consumption entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Consumption entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Consumption find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Consumption> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Consumption> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }
    
    @GET
    @Path("findByDate/{date}")
    @Produces({"application/json"})
    public List<Consumption> findByDate(@PathParam("date") String date){
        Query q = em.createNamedQuery("Consumption.findByDate");
        try{
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date dateob = format.parse(date);
            q.setParameter("date", dateob);
        }
        catch(ParseException e){
            System.out.println(e);
        }
        return q.getResultList();
    }
    
    @GET
    @Path("findByQuantity/{quantity}")
    @Produces({"application/json"})
    public List<Consumption> findByQuantity(@PathParam("quantity") BigDecimal quantity){
        Query q = em.createNamedQuery("Consumption.findByQuantity");
        q.setParameter("quantity", quantity);
        return q.getResultList();
    }

    //part 4d
    @GET
    @Path("getTotalCalorie/{userid}/{date}")
    @Produces(MediaType.TEXT_PLAIN)
    public double getTotalCalorie(@PathParam("userid") Integer userid, @PathParam("date") String date){
        double result = 0.0;
        TypedQuery<Consumption> q = em.createQuery("SELECT c FROM Consumption c WHERE c.userid.userid = :userid AND c.date=:date", Consumption.class);
        try{
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date dateob = format.parse(date);
            q.setParameter("date", dateob);
        }
        catch(ParseException e){
            System.out.println(e);
        }
        q.setParameter("userid", userid);
        List<Consumption> con = q.getResultList();
        for (Consumption c : con){
           result += c.getFoodid().getCalorieamount().doubleValue() * c.getQuantity().doubleValue();
        }
        return result;
    }
    //    Additional
    
    @GET
    @Path("getMaxID")
    @Produces(MediaType.TEXT_PLAIN)
    public Integer getMaxID(){
        TypedQuery<Integer> q = em.createQuery("SELECT MAX(c.consumptionid) FROM Consumption c", Integer.class);
        return q.getSingleResult();
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
