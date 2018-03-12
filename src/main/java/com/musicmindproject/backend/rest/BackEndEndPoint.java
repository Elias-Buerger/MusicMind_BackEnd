package com.musicmindproject.backend.rest;

import com.musicmindproject.backend.logic.AnalyticsManager;
import com.musicmindproject.backend.logic.ContactManager;
import com.musicmindproject.personalizer.DatabaseManager;
import com.musicmindproject.personalizer.PersonalityEvaluator;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.File;
import java.io.IOException;
import java.util.Random;

@Path("/rest")
public class BackEndEndPoint {

    /*
     * ---------------------------------------------------------------------
     * Music
     * ---------------------------------------------------------------------
     * */

    @GET
    @Path("/music/{id}")
    public int doMusicGet(@PathParam("id") int id) {
        return 0;
    }

    @GET
    @Path("/music")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response doMusicPost(JsonObject answer) {

        int totalNumberOfQuestions = DatabaseManager.getInstance().getNumberOfAvailableQuestions();

        if(answer.size() != totalNumberOfQuestions + 1) {
            System.err.println("Bad input (number of elements != total number of questions ("+ totalNumberOfQuestions +")");
            return Response.notModified("Bad input (number of elements != total number of questions ("+ totalNumberOfQuestions +")").build();
        }

        double[] answerNumbers = new double[answer.size() - 2];
        String userID = answer.getString("" + (answer.size() - 1));
        String userName = answer.getString("" + (answer.size() - 2));

        //TODO Persist User

        JsonObjectBuilder builder = Json.createObjectBuilder();

        JsonObjectBuilder personalityBuilder = Json.createObjectBuilder();
        for(int i = 0; i < answerNumbers.length; i++){
            answerNumbers[i] = Integer.parseInt(answer.getString("" + i));
        }
        PersonalityEvaluator evaluator = PersonalityEvaluator.getInstance();
        double[] values = evaluator.getOutputs(answerNumbers);
        personalityBuilder.add("neuroticism", values[0]);
        personalityBuilder.add("extraversion", values[3]);
        personalityBuilder.add("openness", values[4]);
        personalityBuilder.add("agreeableness", values[2]);
        personalityBuilder.add("conscientiousness", values[1]);

        builder.add("personality", personalityBuilder);

        final String PATHNAME = "/mnt/sequences_tmp/melody_rnn/generated_tracks";
        File musicDirectory = new File(PATHNAME);
        Random rand = new Random();

        builder.add("musicPath", musicDirectory.listFiles()[Math.abs(rand.nextInt()%musicDirectory.listFiles().length)].getName());

        return Response.ok(builder.build(), MediaType.APPLICATION_JSON).build();
    }


    /*
     * ---------------------------------------------------------------------
     * Questions
     * ---------------------------------------------------------------------
     * */

    @GET
    @Path("/question/questionCount")
    public Response doQuestionCountGet() {
        return Response.ok(DatabaseManager.getInstance().getNumberOfAvailableQuestions()).build();
    }

    @GET
    @Path("/question/{questionId}/{lang}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response doQuestionGet(@PathParam("questionId") int qId, @PathParam("lang") String lang) {
        return Response.ok(DatabaseManager.getInstance().getQuestion(qId, lang), MediaType.TEXT_PLAIN).build();
    }

    /*
     * ---------------------------------------------------------------------
     * Stats
     * ---------------------------------------------------------------------
     * */

    @GET
    @Path("/stats/personalitycount")
    public long doPersonalityCountGet() {
        return AnalyticsManager.getInstance().getGenerated();
    }

    @GET
    @Path("/stats/pageclicks")
    public long doPageClicksGet() {
        return AnalyticsManager.getInstance().getPageClicks();
    }

    @GET
    @Path("/stats/shared")
    public long doSharesGet() {
        return AnalyticsManager.getInstance().getShares();
    }

    /*
     * ---------------------------------------------------------------------
     * Contact
     * ---------------------------------------------------------------------
     * */

    @POST
    @Path("/contact")
    public void doMailPost(@FormParam("name") String name, @FormParam("email") String email, @FormParam("subject") String subject, @FormParam("comment") String comment) throws ServletException, IOException {
        ContactManager.getInstance().sendEmail(name, email, subject, comment);
    }
}
