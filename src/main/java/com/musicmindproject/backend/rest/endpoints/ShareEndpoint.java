package com.musicmindproject.backend.rest.endpoints;

import com.musicmindproject.backend.logic.AnalyticsManager;

import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("share")
public class ShareEndpoint {
    @POST
    @Path("personalityCanvas")
    @Consumes(MediaType.APPLICATION_JSON)
    public void uploadPersonalityCanvas(JsonObject image) {
        System.out.println(image.toString());
    }
}
