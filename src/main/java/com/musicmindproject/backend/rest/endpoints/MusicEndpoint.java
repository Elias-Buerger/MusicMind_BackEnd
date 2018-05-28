package com.musicmindproject.backend.rest.endpoints;

import com.google.gson.GsonBuilder;
import com.musicmindproject.backend.entities.Play;
import com.musicmindproject.backend.entities.Share;
import com.musicmindproject.backend.entities.User;
import com.musicmindproject.backend.entities.enums.MusicGenre;
import com.musicmindproject.backend.logic.PersonalityEvaluator;
import com.musicmindproject.backend.logic.PersonalityImageGenerator;
import com.musicmindproject.backend.logic.database.PlaysManager;
import com.musicmindproject.backend.logic.database.QuestionManager;
import com.musicmindproject.backend.logic.database.SharesManager;
import com.musicmindproject.backend.logic.database.UserManager;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Path("music")
public class MusicEndpoint {
    @Inject
    private UserManager userManager;
    @Inject
    private PersonalityEvaluator evaluator;
    @Inject
    private QuestionManager questionManager;
    @Inject
    private PlaysManager playsManager;
    @Inject
    private SharesManager sharesManager;
    @Inject
    private PersonalityImageGenerator personalityImageGenerator;

    private final String PATHNAME = "/mnt/sequences_tmp/melody_rnn/";

    /**
     * @param id ID of the user
     * @return JsonObject with Personality, Username, UserId, Path to Music-Track
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response doMusicGet(@PathParam("id") String id) {
        return Response.ok().entity(new GsonBuilder().create().toJson(userManager.retrieve(id))).build();
    }

    /**
     *
     * @param filepath Path to the files belonging to the user
     * @return ok if no error occurred
     */
    @GET
    @Path("video/{filepath}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getVideo(@PathParam("filepath") String filepath) {
        if(generateVideo(filepath))
            return Response.ok().type(MediaType.TEXT_PLAIN).build();
        return Response.serverError().build();
    }

    /**
     * Generates a video by merging the generated track and the generated image of the user
     * @param filepath Path to the files belonging to the user
     * @return true if no error occurred
     */
    private boolean generateVideo(String filepath) {
        try {
            String[] command = {
//                    "/bin/bash",
                    "ffmpeg",
                    "-i",
                    String.format("/mnt/personality_images/%s.png", filepath),
                    "-i",
                    String.format("/mnt/sequences_tmp/melody_rnn/used_tracks/%s.mp3", filepath),
                    "-strict",
                    "-2",
                    "-c:v",
                    "libx264",
                    "-pix_fmt",
                    "yuv420p",
                    String.format("/mnt/personality_videos/%s.mp4", filepath),
                    "-y"
            };
            Process videoGenerator = Runtime.getRuntime().exec(command);
            int exitStatus = videoGenerator.waitFor();


            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(videoGenerator.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(videoGenerator.getErrorStream()));

            System.out.println("STDOUT::::::::::::::::::::::::");
            String s;
            while ((s = stdInput.readLine()) != null)
                System.out.println(s);
            System.out.println("------------------------------------------------------------------------------------------------------------------");
            System.out.println("STDERR::::::::::::::::::::::::");
            while ((s = stdError.readLine()) != null)
                System.err.println(s);
            System.out.println("DONE::::::::::::::::::::::::::");

            if(exitStatus != 0)
                return false;

            Thread thread = new Thread(() -> {
                try {
                    TimeUnit.MINUTES.sleep(10);
                    Process videoDeleter = Runtime.getRuntime().exec(String.format("rm \"/mnt/personality_videos/%s.mp4\"", filepath));
                    videoDeleter.waitFor();
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            });
            thread.start();

            Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", "chmod -R 777 /mnt/personality_videos"});

            return true;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param answer Answers the user has given plus the Username and the ID
     * @return JSON-Object with the personality of the user represented by the Big-Five (see Wikipedia for more)
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response doMusicPost(JsonObject answer) {
        int totalNumberOfQuestions = questionManager.getNumberOfAvailableQuestions();

        if (answer.size() != totalNumberOfQuestions + 1) {
            System.err.println("Bad input (number of elements != total number of questions (" + totalNumberOfQuestions + ")");
            return Response.notModified("Bad input (number of elements != total number of questions (" + totalNumberOfQuestions + ")").build();
        }

        double[] answerNumbers = new double[answer.size() - 2];
        String userID = answer.getString("" + (answer.size() - 1));
        String userName = answer.getString("" + (answer.size() - 2));

        for (int i = 0; i < answerNumbers.length; i++) {
            answerNumbers[i] = Integer.parseInt(answer.getString("" + i));
        }

        double[] values = evaluator.getOutputs(answerNumbers);
        File musicFile = createMusicFile(userName, userID, values);
        User user = storeUser(userID, userName, musicFile.getName().substring(0, musicFile.getName().length() - 4), values);

        return Response.ok().entity(new GsonBuilder()
                .create()
                .toJson(user))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
    private File convertToMP3(File toConvert) {
        try {
            File tmp = new File(toConvert.getAbsolutePath().substring(0, toConvert.getAbsolutePath().length() - 4) + ".mp3");
            Process p = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", String.format("timidity %s -Ow -o - | lame - -b 64 %s", toConvert.getAbsolutePath(), tmp.getAbsolutePath())});
            p.waitFor();
            return tmp;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return toConvert;
        }
    }
    private File createMusicFile(String userName, String userID, double[] values) {
        File musicTrack = convertToMP3(findFileForUser(values));
        File destination = new File(PATHNAME + "used_tracks/" + userID.hashCode() + "_" + userName + ".mp3");

        try {
            Files.move(musicTrack.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return destination;
    }
    private User storeUser(String userID, String userName, String fileName, double[] values) {
        User user = userManager.retrieve(userID);
        if (user == null)
            user = new User(userID, userName, fileName, values[4], values[1], values[3], values[2], values[0]);
        else {
            user.setAgreeableness(values[2]);
            user.setConscientiousness(values[1]);
            user.setExtraversion(values[3]);
            user.setNeuroticism(values[0]);
            user.setOpenness(values[4]);
            user.setUserName(userName);
            user.setPlays(0);
            user.setShares(0);
            user.setFilename(fileName);
        }
        userManager.store(user);
        personalityImageGenerator.generatePersonalityImage(user);
        return user;
    }
    private File findFileForUser(double[] values) {
        //TODO FIND SPECIFIC FILE FOR USER

        Random rand = new Random();
        MusicGenre g = MusicGenre.values()[Math.abs(rand.nextInt() % MusicGenre.values().length)];
        File[] filesAvailable = new File(PATHNAME + "generated_tracks/" + g.name().toLowerCase() + "_" + g.getInstruments().get(Math.abs(rand.nextInt() % g.getInstruments().size())).name().toLowerCase()).listFiles();
        return filesAvailable[Math.abs(rand.nextInt() % Objects.requireNonNull(filesAvailable).length)];
    }

    /**
     * @param query Fixed Keywords:
     *              - newest
     *              - hottest
     *              - everything else: name of the user or music-track
     * @param min   First Track to return
     * @param max   Last track to return
     * @return JsonArray of doMusicGet() with all Users (between min and max)
     */
    @GET
    @Path("{query}/{min}/{max}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response doMusicGetForExplore(@PathParam("query") String query, @PathParam("min") int min, @PathParam("max") int max) {
        return Response.ok(userManager.retrieveMany(min, max, query)).build();
    }

    /**
     * @param music object:
     *              - player = id of person who played music
     *              - played = id of person who created music
     */
    @POST
    @Path("play")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response increasePlays(JsonObject music) {
        Play play = new GsonBuilder().create().fromJson(music.toString(), Play.class);

        if (playsManager.retrieve(play) == null) {
            playsManager.store(play);
            User u = userManager.retrieve(play.getPlayed());
            u.setPlays(u.getPlays() + 1);
            userManager.store(u);
        }
        return Response.noContent().build();
    }

    /**
     * @param music object:
     *              - share = id of person who shared music
     *              - shared = id of person who's music was shared
     */
    @POST
    @Path("share")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response increaseShares(JsonObject music) {
        Share share = new GsonBuilder().create().fromJson(music.toString(), Share.class);

        if (sharesManager.retrieve(share) == null) {
            sharesManager.store(share);
            User u = userManager.retrieve(share.getShared());
            u.setShares(u.getShares() + 1);
            userManager.store(u);
        }
        return Response.noContent().build();
    }
}
