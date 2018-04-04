package com.musicmindproject.backend.rest.endpoints;

import com.musicmindproject.backend.logic.QuestionManager;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("question")
public class QuestionEndpoint {
    @Inject
    private QuestionManager questionManager;
    /**
     * @return the total count of all questions
     */
    @GET
    @Path("questionCount")
    public Response doQuestionCountGet() {
        return Response.ok(questionManager.getNumberOfAvailableQuestions()).build();
    }

    /**
     * @param qId ID of the question
     * @param lang language of the question
     * @return the needed question translated in the wanted language
     */
    @GET
    @Path("{questionId}/{lang}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response doQuestionGet(@PathParam("questionId") int qId, @PathParam("lang") String lang) {
        return Response.ok(questionManager.retrieve(qId, lang), MediaType.TEXT_PLAIN).build();
    }
}