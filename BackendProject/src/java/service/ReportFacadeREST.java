/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import Backendpkg.Users;
import Backendpkg.Report;
import Entities.CalorieDetails;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
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
@Path("backendpkg.report")
public class ReportFacadeREST extends AbstractFacade<Report> {

    @PersistenceContext(unitName = "BackendProjectPU")
    private EntityManager em;

    public ReportFacadeREST() {
        super(Report.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Report entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Report entity) {
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
    public Report find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Report> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Report> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
    public List<Report> findByDate(@PathParam("date") String date){
        Query q = em.createNamedQuery("Report.findByDate");
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
    @Path("findByCalorieconsumed/{caloriesconsumed}")
    @Produces({"application/json"})
    public List<Report> findByCalorieConsumed(@PathParam("caloriesconsumed") BigDecimal calorie){
        Query q = em.createNamedQuery("Report.findByCaloriesconsumed");
        q.setParameter("caloriesconsumed", calorie);
        return q.getResultList();
    }
    
    @GET
    @Path("findByCalorieburned/{caloriesburned}")
    @Produces({"application/json"})
    public List<Report> findByCalorieBurned(@PathParam("caloriesburned") BigDecimal calorie){
        Query q = em.createNamedQuery("Report.findByCaloriesburned");
        q.setParameter("caloriesburned", calorie);
        return q.getResultList();
    }
    
    @GET
    @Path("findByStepTaken/{steps}")
    @Produces({"application/json"})
    public List<Report> findByStepTaken(@PathParam("steps") int steps){
        Query q = em.createNamedQuery("Report.findByStepstaken");
        q.setParameter("stepstaken", steps);
        return q.getResultList();
    }
    
    @GET
    @Path("findByCaloriegoal/{caloriegoal}")
    @Produces({"application/json"})
    public List<Report> findByCalorieGoal(@PathParam("caloriegoal") BigDecimal calorie){
        Query q = em.createNamedQuery("Report.findByCaloriegoal");
        q.setParameter("caloriegoal", calorie);
        return q.getResultList();
    }
    
    // Part 3d
    @GET
    @Path("findByEmailAndDate/{email}/{date}")
    @Produces({"application/json"})
    public List<Report> findByEmailAndDate(@PathParam("email") String email, @PathParam("date") String date){
        Query q = em.createNamedQuery("Report.findByEmailAndDate");
        q.setParameter("email", email);
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
          
    
    //part 5a
    @GET
    @Path("getCalorieDetails/{userid}/{date}")
    @Produces({MediaType.APPLICATION_JSON})
    public Object getCalorieDetials(@PathParam("userid") Integer userid, @PathParam("date")String date) throws ParseException{
        
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dateob = format.parse(date);
        List<Object[]>q_result = em.createQuery("SELECT r.caloriesconsumed,r.caloriesburned,r.caloriegoal FROM Report r WHERE r.userid.userid=:userid AND r.date=:date")
                                   .setParameter("date", dateob)
                                   .setParameter("userid", userid)
                                   .getResultList();        
        JsonArrayBuilder arraybuilder = Json.createArrayBuilder();
        for (Object[] row : q_result){
            double row0 = ((BigDecimal)row[0]).doubleValue();
            double row1 = ((BigDecimal)row[1]).doubleValue();
            double row2 = ((BigDecimal)row[2]).doubleValue();
            JsonObject reportObject = Json.createObjectBuilder().add("caloriesconsumed", row0)
                                                                .add("caloriesburned", row1)
                                                                .add("calorieremaining", row2-(row0-row1))
                                                                .build();
            arraybuilder.add(reportObject);
        }
        JsonArray jArray = arraybuilder.build();
        return jArray;
        
    }
   
    /*public List<CalorieDetails> getCalorieDetails(@PathParam("userid")Integer userid, @PathParam("date")String date){
        TypedQuery<CalorieDetails> q = em.createQuery("Select new entities.CalorieDetails(r.caloriesconsumed,r.caloriesburned,r.caloriegoal) FROM Report r WHERE r.userid=:userid AND r.date=:date", CalorieDetails.class);
        q.setParameter("userid", userid);
        try{
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date dateob = format.parse(date);
            q.setParameter("date", dateob);
        }
        catch(ParseException e){
            System.out.println(e);
        }
        return q.getResultList();
    }*/
    
    // Part 5B
    @GET
    @Path("getTotalDetails/{userid}/{date1}/{date2}")
    @Produces({MediaType.APPLICATION_JSON})
    public Object getTotalDetails(@PathParam("userid")Integer userid,@PathParam("date1")String date1, @PathParam("date2")String date2) throws ParseException{
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dateob1 = format.parse(date1);
        Date dateob2 = format.parse(date2);
        JsonArrayBuilder arraybuilder = Json.createArrayBuilder();
        List<Object[]>q_result = em.createQuery("Select r.caloriesburned,r.caloriesconsumed,r.stepstaken "
                                   + "FROM Report r WHERE r.userid.userid=:userid AND r.date>:date1 AND r.date<:date2")
                                   .setParameter("userid", userid)
                                   .setParameter("date1", dateob1)
                                   .setParameter("date2", dateob2)
                                   .getResultList();
        double caloriesburned=0.0, caloriesconsumed=0.0;
        int stepstaken =0;
        for (Object[]row : q_result){
            caloriesburned+=((BigDecimal)row[0]).doubleValue();
            caloriesconsumed+=((BigDecimal)row[1]).doubleValue();
            stepstaken+=(int)row[2];
        }
        JsonObject reportObject = Json.createObjectBuilder().add("caloriesburned", caloriesburned)
                                                            .add("caloriesconsumed", caloriesconsumed)
                                                            .add("stepstaken", stepstaken).build();
        arraybuilder.add(reportObject);
        JsonArray jArray = arraybuilder.build();
        return jArray;
    }
    
    //---------Additional------------
    
    @GET
    @Path("findByUserId/{userid}")
    @Produces({"application/json"})
    public List<Report> findByUserId(@PathParam("userid") Integer userid){
        TypedQuery<Report> q = em.createQuery("SELECT r FROM Report r WHERE r.date=:date AND r.userid.userid=:userid",Report.class);
        q.setParameter("userid", userid);
        
        q.setParameter("date", new Date());
        
        
        return q.getResultList();
    }
    
    @PUT
    @Path("/updateCalorieGoal/{id}/{calorie}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void upddateCalorieGoal(@PathParam("id")Integer id,@PathParam("calorie")BigDecimal calorie){
        Report r = super.find(id);
        r.setCaloriegoal(calorie);
        super.edit(r);
    }
    
    //    Additional 
    
    @GET
    @Path("getMaxID")
    @Produces(MediaType.TEXT_PLAIN)
    public Integer getMaxID(){
        TypedQuery<Integer> q = em.createQuery("SELECT MAX(r.reportid) FROM Report r", Integer.class);
        return q.getSingleResult();
    }
    
    @GET
    @Path("getTotalReports/{userid}/{date1}/{date2}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Report> getTotalReports(@PathParam("userid")Integer userid,@PathParam("date1")String date1, @PathParam("date2")String date2) throws ParseException{
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dateob1 = format.parse(date1);
        Date dateob2 = format.parse(date2);
        TypedQuery<Report>q = em.createQuery("Select r "
                                   + "FROM Report r WHERE r.userid.userid=:userid AND r.date>=:date1 AND r.date<=:date2",Report.class)
                                   .setParameter("userid", userid)
                                   .setParameter("date1", dateob1)
                                   .setParameter("date2", dateob2);
        /*JsonArrayBuilder arraybuilder = Json.createArrayBuilder();
        List<Object[]>q_result = em.createQuery("Select r.caloriesburned,r.caloriesconsumed,r.stepstaken "
                                   + "FROM Report r WHERE r.userid.userid=:userid AND r.date>:date1 AND r.date<:date2")
                                   .setParameter("userid", userid)
                                   .setParameter("date1", dateob1)
                                   .setParameter("date2", dateob2)
                                   .getResultList();
        double caloriesburned=0.0, caloriesconsumed=0.0;
        int stepstaken =0;
        for (Object[]row : q_result){
            caloriesburned+=((BigDecimal)row[0]).doubleValue();
            caloriesconsumed+=((BigDecimal)row[1]).doubleValue();
            stepstaken+=(int)row[2];
        }
        JsonObject reportObject = Json.createObjectBuilder().add("caloriesburned", caloriesburned)
                                                            .add("caloriesconsumed", caloriesconsumed)
                                                            .add("stepstaken", stepstaken).build();
        arraybuilder.add(reportObject);
        JsonArray jArray = arraybuilder.build();*/
        return q.getResultList();
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
