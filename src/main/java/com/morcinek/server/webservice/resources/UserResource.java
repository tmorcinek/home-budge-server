package com.morcinek.server.webservice.resources;

import com.google.inject.Inject;
import com.morcinek.server.model.User;
import com.morcinek.server.webservice.exceptions.MethodNotImplementedException;
import com.morcinek.server.webservice.exceptions.UserLoginException;
import com.morcinek.server.webservice.exceptions.UserNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 2/10/13
 * Time: 9:56 PM
 * To change this template use File | Settings | File Templates.
 */
@Path("/user")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class UserResource {

    @Inject
    private EntityManager entityManager;

    @GET
    public User getUser(@QueryParam("email") String email, @QueryParam("password") String password) {
        checkBasicData(new User(email, password));
        TypedQuery<User> query = entityManager.createNamedQuery("findUserByEmailAndPassword", User.class);
        query.setParameter("email", email);
        query.setParameter("password", password);
        User user = null;
        try {
            List<User> resultList = query.getResultList();
            user = resultList.get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new UserNotFoundException();
        }
        return user;
    }

    @POST
    @Path("/create")
    public Response createUser(@QueryParam("email") String email,
                               @QueryParam("password") String password, @QueryParam("name") String name) {
        User user = new User(email, password);
        user.setName(name);
        return createUser(user);
    }

    @POST
    public Response createUser(User user) {
        checkBasicData(user);
        checkName(user);
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        try {
            entityManager.persist(user);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserLoginException();
        }
        return Response.status(Response.Status.CREATED.getStatusCode()).entity(user).build();
    }

    @POST
    @Path("/confirm")
    public String confirmUser(@QueryParam("email") String email, @QueryParam("token") String token) {
        throw new MethodNotImplementedException();
    }

    @PUT
    public User updateUser(User user) {
        throw new MethodNotImplementedException();
    }

//    public Response deleteUser(User user) {
//        EntityTransaction tx = entityManager.getTransaction();
//        tx.begin();
//        try {
//            user = getUser(user.getEmail(),user.getPassword());
//            entityManager.remove(user);
//            tx.commit();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new UserLoginException();
//        }
//        return Response.status(Response.Status.OK.getStatusCode()).build();
//    }
//
//    public Response deleteUser(long userId) {
//        EntityTransaction tx = entityManager.getTransaction();
//        tx.begin();
//        try {
//            User user = entityManager.find(User.class, userId);
//            entityManager.remove(user);
//            tx.commit();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new UserLoginException();
//        }
//        return Response.status(Response.Status.OK.getStatusCode()).build();
//    }

    private void checkBasicData(User user) {
        if (user.getEmail() == null || user.getPassword() == null) {
            throw new UserLoginException();
        }
    }

    private void checkName(User user) {
        if (user.getName() == null) {
            throw new UserLoginException();
        }
    }

}
