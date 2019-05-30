/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import Backendpkg.Users;
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
@Path("backendpkg.users")
public class UsersFacadeREST extends AbstractFacade<Users> {

    @PersistenceContext(unitName = "BackendProjectPU")
    private EntityManager em;

    public UsersFacadeREST() {
        super(Users.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Users entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Users entity) {
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
    public Users find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Users> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Users> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @GET
    @Path("findByName/{name}")
    @Produces({"application/json"})
    public List<Users> findByName(@PathParam("name") String name){
        Query q = em.createNamedQuery("Users.findByName");
        q.setParameter("name", name);
        return q.getResultList();
    }
    
    @GET
    @Path("findbySurname/{surname}")
    @Produces({"application/json"})
    public List<Users> findBySurname(@PathParam("surname") String surname){
        Query q = em.createNamedQuery("Users.findBySurname");
        q.setParameter("surname", surname);
        return q.getResultList();
    }

    @GET
    @Path("findbyEmail/{email}")
    @Produces({"application/json"})
    public List<Users> findByEmail(@PathParam("email") String email){
        Query q = em.createNamedQuery("Users.findByEmail");
        q.setParameter("email", email);
        return q.getResultList();
    }
    
    @GET
    @Path("findbyDob/{dateOfBirth}")
    @Produces({"application/json"})
    public List<Users> findByDom(@PathParam("dateOfBirth") String dob){
        Query q = em.createNamedQuery("Users.findByDob");
        try{
            
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date dateob = format.parse(dob);
            q.setParameter("dob", dateob);
            
        }
        catch(ParseException e){
            System.out.println(e);
        }
        
        return q.getResultList();
    }
    
    @GET
    @Path("findbyHeight/{height}")
    @Produces({"application/json"})
    public List<Users> findByHeight(@PathParam("height") BigDecimal height){
        Query q = em.createNamedQuery("Users.findByHeight");
        q.setParameter("height", height);
        return q.getResultList();
    }
    
    @GET
    @Path("findbyWeight/{weight}")
    @Produces({"application/json"})
    public List<Users> findByWeight(@PathParam("weight") BigDecimal weight){
        Query q = em.createNamedQuery("Users.findByWeight");
        q.setParameter("weight", weight);
        return q.getResultList();
    }
    
    @GET
    @Path("findbyGender/{gender}")
    @Produces({"application/json"})
    public List<Users> findByGender(@PathParam("gender") String gender){
        Query q = em.createNamedQuery("Users.findByGender");
        q.setParameter("gender", gender);
        return q.getResultList();
    }
    
    @GET
    @Path("findbyAddress/{address}")
    @Produces({"application/json"})
    public List<Users> findByAddress(@PathParam("address") String address){
        Query q = em.createNamedQuery("Users.findByAddress");
        q.setParameter("address", address);
        return q.getResultList();
    }
    
    @GET
    @Path("findbyPostcode/{postcode}")
    @Produces({"application/json"})
    public List<Users> findByPostcode(@PathParam("postcode") String postcode){
        Query q = em.createNamedQuery("Users.findByPostcode");
        q.setParameter("postcode", postcode);
        return q.getResultList();
    }
    
    @GET
    @Path("findByLevelofactivity/{levelofactivity}")
    @Produces({"application/json"})
    public List<Users> findByLevelofactivity(@PathParam("levelofactivity") String levelofactivity){
        Query q = em.createNamedQuery("Users.findByLevelofactivity");
        q.setParameter("levelofactivity", levelofactivity);
        return q.getResultList();
    }
    
    @GET
    @Path("findbyStepspermile/{spm}")
    @Produces({"application/json"})
    public List<Users> findByStepsPerMile(@PathParam("spm") Integer spm){
        Query q = em.createNamedQuery("Users.findByStepspermile");
        q.setParameter("stepspermile", spm);
        return q.getResultList();
    }
    
    // Part 4a
    @GET
    @Path("getCaloriesBurnedPerStep/{userid}")
    @Produces(MediaType.TEXT_PLAIN)
    public double getCaloriesBurnedPerStep(@PathParam("userid") Integer userid){
        BigDecimal weight = super.find(userid).getWeight();
        Integer steps = super.find(userid).getStepspermile();
        double result;
        result = weight.doubleValue()*0.49*2.2/steps;
        return result;
    }
    
    // Part 4b
    @GET
    @Path("getBMR/{userid}")
    @Produces(MediaType.TEXT_PLAIN)
    public double getBMR(@PathParam("userid") Integer userid){
        BigDecimal weight = super.find(userid).getWeight();
        BigDecimal height = super.find(userid).getHeight();
        String gender = super.find(userid).getGender();
        Date dob = super.find(userid).getDob();
        Date datenow = new Date();
        int age = datenow.getYear() - dob.getYear();
        if (dob.getMonth() < datenow.getMonth())//if users birthday is later in the year
            age-=1;
        double result;
        if ("M".equals(gender))
            result = (13.75 * weight.doubleValue()*2.2) + (5.003 * height.doubleValue()) - (6.755 * age) + 66.5;
        else
            result = (9.563 * weight.doubleValue()*2.2) + (1.85 * height.doubleValue()) - (4.676 * age) + 655.1;
        return result;
    }
    
    // for part 4c
    @GET
    @Path("getCaloriesBurnedAtRest/{userid}")
    @Produces(MediaType.TEXT_PLAIN)
    public double getCaloriesBurnedAtRest(@PathParam("userid") Integer userid){
        String activity = super.find(userid).getLevelofactivity();
        double bmr = this.getBMR(userid);
        double result = 0.0;
        switch(activity){
            case "1":
                result = 1.2 * bmr;
                break;
            case "2":
                result = 1.375 * bmr;
                break;
            case "3":
                result = 1.55 * bmr;
                break;
            case "4":
                result = 1.725 * bmr;
                break;
            case "5":
                result = 1.9 * bmr;
                break;
        }
        return result;
    }
    //---Additional
    @GET
    @Path("getMaxID")
    @Produces(MediaType.TEXT_PLAIN)
    public Integer getMaxID(){
        TypedQuery<Integer> q = em.createQuery("SELECT MAX(u.userid) FROM Users u", Integer.class);
        return q.getSingleResult();
    }
    
    
    
    
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
