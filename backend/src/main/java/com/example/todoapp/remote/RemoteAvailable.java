package com.example.todoapp.remote;

import com.example.todoapp.model.Available;
import org.apache.log4j.Logger;

import javax.ws.rs.core.Response;

public class RemoteAvailable implements Available {

    protected static Logger logger = Logger
            .getLogger(RemoteDataItemAccessor.class);

    @Override
    public Response check() {
        // delay
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ie) {
            logger.error(ie.getMessage());
        }
        return Response.ok().build();
    }
}
