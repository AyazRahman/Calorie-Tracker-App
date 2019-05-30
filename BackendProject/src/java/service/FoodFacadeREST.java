/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import Backendpkg.Food;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
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
@Path("backendpkg.food")
public class FoodFacadeREST extends AbstractFacade<Food> {

    @PersistenceContext(unitName = "BackendProjectPU")
    private EntityManager em;

    public FoodFacadeREST() {
        super(Food.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Food entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Food entity) {
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
    public Food find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Food> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Food> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
    public List<Food> findByName(@PathParam("name") String name){
        Query q = em.createNamedQuery("Food.findByName");
        q.setParameter("name", name);
        return q.getResultList();
    }
    
    @GET
    @Path("findByCategory/{category}")
    @Produces({"application/json"})
    public List<Food> findByCategory(@PathParam("category") String category){
        Query q = em.createNamedQuery("Food.findByCategory");
        q.setParameter("category", category);
        return q.getResultList();
    }
    
    @GET
    @Path("findByCalorieAmount/{calorie}")
    @Produces({"application/json"})
    public List<Food> findByCalorieAmount(@PathParam("calorie") BigDecimal calorie){
        Query q = em.createNamedQuery("Food.findByCalorieamount");
        q.setParameter("calorieamount", calorie);
        return q.getResultList();
    }
    
    @GET
    @Path("findByServingUnit/{servingunit}")
    @Produces({"application/json"})
    public List<Food> findByServingUnit(@PathParam("servingunit") String servingunit){
        Query q = em.createNamedQuery("Food.findByServingunit");
        q.setParameter("servingunit", servingunit);
        return q.getResultList();
    }
    
    @GET
    @Path("findByServingAmount/{servingamount}")
    @Produces({"application/json"})
    public List<Food> findByServingAmount(@PathParam("servingamount") BigDecimal servingamount){
        Query q = em.createNamedQuery("Food.findByServingamount");
        q.setParameter("servingamount", servingamount);
        return q.getResultList();
    }
    
    @GET
    @Path("findByFat/{fat}")
    @Produces({"application/json"})
    public List<Food> findByFat(@PathParam("fat") BigDecimal fat){
        Query q = em.createNamedQuery("Food.findByFat");
        q.setParameter("fat", fat);
        return q.getResultList();
    }
    
    // for part 3b
    @GET
    @Path("findByNameAndServingunit/{name}/{servingunit}")
    @Produces({"application/json"})
    public List<Food> findByNameAndServingunit(@PathParam("name") String name, @PathParam("servingunit") String serving){
        TypedQuery<Food> q = em.createQuery("SELECT f FROM Food f WHERE f.name=:name AND f.servingunit=:servingunit", Food.class);
        q.setParameter("name", name);
        q.setParameter("servingunit", serving);
        return q.getResultList();
    }

    ////    Additional
    
    @GET
    @Path("getMaxID")
    @Produces(MediaType.TEXT_PLAIN)
    public Integer getMaxID(){
        TypedQuery<Integer> q = em.createQuery("SELECT MAX(f.foodid) FROM Food f", Integer.class);
        return q.getSingleResult();
    }
    
    @GET
    @Path("getCategory")
    @Produces({"application/json"})
    
    public Object getCategory(){
        List<String> category= em.createQuery("SELECT DISTINCT f.category FROM Food f", String.class).getResultList();
        JsonArrayBuilder arraybuilder = Json.createArrayBuilder();
        for (String row : category){
            arraybuilder.add(row);
        }
        JsonArray jsArray = arraybuilder.build();
        return jsArray;
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
