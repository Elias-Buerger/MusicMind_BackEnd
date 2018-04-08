package com.musicmindproject.backend.rest.endpoints;

import com.musicmindproject.backend.logic.AnalyticsManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("analytics")
public class AnalyticsEndpoint {
    @GET
    @Path("personalitycount")
    @Produces(MediaType.TEXT_PLAIN)
    public long doPersonalityCountGet() {
        return AnalyticsManager.getInstance().getGenerated();
    }

    @GET
    @Path("pageclicks")
    @Produces(MediaType.TEXT_PLAIN)
    public long doPageClicksGet() {
        return AnalyticsManager.getInstance().getPageClicks();
    }

    @GET
    @Path("shared")
    @Produces(MediaType.TEXT_PLAIN)
    public long doSharesGet() {
        return AnalyticsManager.getInstance().getShares();
    }
}
