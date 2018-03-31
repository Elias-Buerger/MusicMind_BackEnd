package com.musicmindproject.backend.rest;

import com.musicmindproject.personalizer.DatabaseManager;
import com.musicmindproject.personalizer.PersonalityEvaluator;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.Random;

@Path("music")
public class MusicEndpoint {
    @GET
    @Path("{id}")
    public int doMusicGet(@PathParam("id") int id) {
        return 0;
    }

    @GET
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
}
