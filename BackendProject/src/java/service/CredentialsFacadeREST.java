/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import Backendpkg.Credentials;
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
@Path("backendpkg.credentials")
public class CredentialsFacadeREST extends AbstractFacade<Credentials> {

    @PersistenceContext(unitName = "BackendProjectPU")
    private EntityManager em;

    public CredentialsFacadeREST() {
        super(Credentials.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Credentials entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") String id, Credentials entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") String id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Credentials find(@PathParam("id") String id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Credentials> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Credentials> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }
    
    @GET
    @Path("findByUsername/{name}")
    @Produces({"application/json"})
    public List<Credentials> findByUsername(@PathParam("name") String name){
        Query q = em.createNamedQuery("Credentials.findByUsername");
        q.setParameter("username", name);
        return q.getResultList();
    }
    
    @GET
    @Path("findByPasswordhash/{hash}")
    @Produces({"application/json"})
    public List<Credentials> findByPasswordHash(@PathParam("hash") String hash){
        Query q = em.createNamedQuery("Credentials.findByPasswordhash");
        q.setParameter("passwordhash", hash);
        return q.getResultList();
    }
    
    @GET
    @Path("findBySignupDate/{date}")
    @Produces({"application/json"})
    public List<Credentials> findBySignupDate(@PathParam("date") String date){
        Query q = em.createNamedQuery("Credentials.findBySignupdate");
        try{
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date dateob = format.parse(date);
            q.setParameter("signupdate", dateob);
        }
        catch(ParseException e){
            System.out.println(e);
        }
        return q.getResultList();
    }
    
    //part 3c
    @GET
    @Path("findByEmailAndUsername/{email}/{name}")
    @Produces({"application/json"})
    public List<Credentials> findByEmailAndUsername(@PathParam("email") String email, @PathParam("name") String name){
        TypedQuery<Credentials> q = em.createQuery("SELECT c FROM Credentials c WHERE c.userid.email=:email AND c.username=:username", Credentials.class);
        q.setParameter("email", email);
        q.setParameter("username", name);
        return q.getResultList();
    }
    //Additional
    @GET
    @Path("findByUsernameAndPassword/{name}/{password}")
    @Produces({"application/json"})
    public List<Credentials> findByUsernameAndPassword(@PathParam("name")String name, @PathParam("password")String password){
        TypedQuery<Credentials> q = em.createQuery("SELECT c FROM Credentials c WHERE c.username=:name AND c.passwordhash=:password",Credentials.class);
        q.setParameter("name", name);
        q.setParameter("password", password);
        return q.getResultList();
    }
    
    @GET
    @Path("emailExist/{email}")
    @Produces(MediaType.TEXT_PLAIN)
    public Boolean emailExist(@PathParam("email")String email){
        TypedQuery<Credentials> q = em.createQuery("SELECT c FROM Credentials c WHERE c.userid.email=:email", Credentials.class);
        q.setParameter("email", email);
        return (q.getResultList().size()>0)? false:true;
    }
    
    @GET
    @Path("usernameExist/{username}")
    @Produces(MediaType.TEXT_PLAIN)
    public Boolean usernameExist(@PathParam("username")String username){
        TypedQuery<Credentials> q = em.createQuery("SELECT c FROM Credentials c WHERE c.username=:username", Credentials.class);
        q.setParameter("username", username);
        return (q.getResultList().size()>0)? false:true;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
