package com.musicmindproject.backend.rest.endpoints;

import com.musicmindproject.backend.DatabaseManager;
import com.musicmindproject.backend.logic.PersonalityEvaluator;

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
    /**
     * @param id
     * @return JsonObject with Personality, Username, UserId, Path to Music-Track
     */
    @GET
    @Path("{id}")
    public int doMusicGet(@PathParam("id") int id) {
        return 0;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response doMusicPost(JsonObject answer) {

        System.out.println(answer);

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

    /**
     * @param query
     * Fixed Keywords:
     * - newest
     * - popularity
     * @param min
     * @param max
     * @return JsonArray of doMusicGet() with all Users (max 50 Users)
     */
    @GET
    @Path("{query}/{min}/{max}")
    public Response doMusicGetForExplore(@PathParam("query") String query, @PathParam("min") int min, @PathParam("max") int max){
        return Response.ok().build();
    }
}
