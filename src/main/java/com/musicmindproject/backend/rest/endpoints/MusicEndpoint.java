package com.musicmindproject.backend.rest.endpoints;

import com.google.gson.GsonBuilder;
import com.musicmindproject.backend.entities.User;
import com.musicmindproject.backend.logic.QuestionManager;
import com.musicmindproject.backend.logic.PersonalityEvaluator;
import com.musicmindproject.backend.logic.UserManager;

import javax.inject.Inject;
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
    @Inject
    private UserManager userManager;

    @Inject
    private QuestionManager questionManager;

    /**
     * @param id ID of the user
     * @return JsonObject with Personality, Username, UserId, Path to Music-Track
     */
    @GET
    @Path("{id}")
    public Response doMusicGet(@PathParam("id") String id) {

        return Response.ok().entity(new GsonBuilder().create().toJson(userManager.retrieve(id))).build();
    }

    /**
     *
     * @param answer Answers the user has given
     * @return JSON-Object with the personality of the user represented by the Big-Five (see Wikipedia for more)
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response doMusicPost(JsonObject answer) {
        int totalNumberOfQuestions = questionManager.getNumberOfAvailableQuestions();

        if(answer.size() != totalNumberOfQuestions + 1) {
            System.err.println("Bad input (number of elements != total number of questions ("+ totalNumberOfQuestions +")");
            return Response.notModified("Bad input (number of elements != total number of questions ("+ totalNumberOfQuestions +")").build();
        }

        double[] answerNumbers = new double[answer.size() - 2];
        String userID = answer.getString("" + (answer.size() - 1));
        String userName = answer.getString("" + (answer.size() - 2));

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
        String filepath = musicDirectory.listFiles()[Math.abs(rand.nextInt()%musicDirectory.listFiles().length)].getName();

        User user = userManager.retrieve(userID);
        if(user == null)
            user = new User(userID, userName, filepath, values[4], values[1], values[3], values[2], values[0]);
        else {
            user.setAgreeableness(values[2]);
            user.setConscientiousness(values[1]);
            user.setExtraversion(values[3]);
            user.setNeuroticism(values[0]);
            user.setOpenness(values[4]);
            user.setUserName(userName);
            user.setPlays(0);
            user.setShares(0);
            user.setPathToMusicTrack(filepath);
        }
        userManager.store(user);

        builder.add("musicPath", filepath);

        return Response.ok(builder.build(), MediaType.APPLICATION_JSON).build();
    }

    /**
     * @param query
     * Fixed Keywords:
     * - newest
     * - hottest
     * - name="..."
     * @param min
     * Ignore
     * @param max
     * Ignore
     * @return JsonArray of doMusicGet() with all Users (max 50 Users)
     */
    @POST
    @Path("{query}/{min}/{max}")
    public Response doMusicGetForExplore(@PathParam("query") String query, @PathParam("min") int min, @PathParam("max") int max){
        return Response.ok().build();
    }

    /**
     * @param music
     * object:
     *  - player = id of person who played music
     *  - played = id of person who created music
     */
    @GET
    @Path("play")
    public Response doMusicPlay(JsonObject music){
        return Response.ok().build();
    }
}
