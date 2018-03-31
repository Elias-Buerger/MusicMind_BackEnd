package com.musicmindproject.backend.rest;

import com.musicmindproject.personalizer.DatabaseManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("question")
public class QuestionEndpoint {
    @GET
    @Path("questionCount")
    public Response doQuestionCountGet() {
        return Response.ok(DatabaseManager.getInstance().getNumberOfAvailableQuestions()).build();
    }

    @GET
    @Path("{questionId}/{lang}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response doQuestionGet(@PathParam("questionId") int qId, @PathParam("lang") String lang) {
        return Response.ok(DatabaseManager.getInstance().getQuestion(qId, lang), MediaType.TEXT_PLAIN).build();
    }
}