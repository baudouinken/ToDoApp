package com.example.todoapp.remote;

import com.example.todoapp.model.Login;
import org.apache.log4j.Logger;
import javax.ws.rs.core.Response;
import java.util.HashMap;

public class RemoteLogin implements Login {

    protected static Logger logger = Logger
            .getLogger(RemoteDataItemAccessor.class);

    private static HashMap<String,String> user = new HashMap<>();
    {
        user.put("schmnorm@th-brandenburg.de", "111111");
        user.put("keunnema@th-brandenburg.de", "222222");
        user.put("ngueyepj@th-brandenburg.de", "333333");
    }

    @Override
    public Response login(String email, String password) {
        logger.info("login(): " + email + ": " + password);

        // delay
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ie) {
            logger.error(ie.getMessage());
        }
        if(user.containsKey(email) && user.get(email).equals(password)) {
            return Response.ok()
                    .build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Not authorized.")
                    .build();
        }
    }
}
