package com.musicmindproject.backend.rest.endpoints;

import com.musicmindproject.backend.logic.ContactManager;

import javax.servlet.ServletException;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.io.IOException;

@Path("contact")
public class ContactEndpoint {
    @POST
    public void doMailPost(@FormParam("name") String name, @FormParam("email") String email, @FormParam("subject") String subject, @FormParam("comment") String comment) throws ServletException, IOException {
        ContactManager.getInstance().sendEmail(name, email, subject, comment);
    }
}
