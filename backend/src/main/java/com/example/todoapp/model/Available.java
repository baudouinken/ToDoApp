package com.example.todoapp.model;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/available")
@Consumes({ "application/json"})
@Produces({ "application/json"})
public interface Available {

    @GET
    public Response check();

}

