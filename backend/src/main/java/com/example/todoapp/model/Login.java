package com.example.todoapp.model;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/login")
@Consumes({ "application/json"})
@Produces({ "application/json"})
public interface Login {

    @POST
    public Response login(@QueryParam("email")String email, @QueryParam("pwd")String password);

}
