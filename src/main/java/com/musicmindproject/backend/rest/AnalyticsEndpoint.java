package com.musicmindproject.backend.rest;

import com.musicmindproject.backend.logic.AnalyticsManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("analytics")
public class AnalyticsEndpoint {
    @GET
    @Path("personalitycount")
    public long doPersonalityCountGet() {
        return AnalyticsManager.getInstance().getGenerated();
    }

    @GET
    @Path("pageclicks")
    public long doPageClicksGet() {
        return AnalyticsManager.getInstance().getPageClicks();
    }

    @GET
    @Path("shared")
    public long doSharesGet() {
        return AnalyticsManager.getInstance().getShares();
    }
}
