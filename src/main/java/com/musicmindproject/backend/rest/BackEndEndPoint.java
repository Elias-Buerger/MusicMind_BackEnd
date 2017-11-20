package com.musicmindproject.backend.rest;

import com.musicmindproject.backend.logic.AnalyticsManager;
import com.musicmindproject.backend.logic.ContactManager;

import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.servlet.ServletException;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import java.io.IOException;
import java.util.Properties;

@Path("/rest")
public class BackEndEndPoint {

    @GET
    @Path("/personalitycount")
    public int doPersonalityCountGet() {
        return AnalyticsManager.getInstance().getGeneratedPersonalitites();
    }

    @GET
    @Path("/pageclicks")
    public int doPageClicksGet() {
        return AnalyticsManager.getInstance().getPageClicks();
    }

    @POST
    @Path("/contact")
    public void doMailPost(@FormParam("name") String name, @FormParam("email") String email, @FormParam("subject") String subject, @FormParam("comment") String comment) throws ServletException, IOException {
        ContactManager.getInstance().sendEmail(name, email, subject, comment);
    }
}
