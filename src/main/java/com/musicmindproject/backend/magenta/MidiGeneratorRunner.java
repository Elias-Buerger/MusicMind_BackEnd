package com.musicmindproject.backend.magenta;

import com.musicmindproject.backend.entities.enums.Instrument;
import com.musicmindproject.backend.entities.enums.MusicGenre;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

@Startup
@Singleton
public class MidiGeneratorRunner {
    private static final String AVAILABLE_MUSIC = "/mnt/midifiles/%s";
    private static final String GENERATED_DATASET = "/mnt/sequence_examples/%s";
    private static final String TFRECORD_FILE = "/mnt/sequences_tmp/%s.tfrecord";
    private static final String RUN_DIRECTORY = "/mnt/sequences_tmp/melody_rnn/logdir/run_%s";
    private static final String OUTPUT_DIRECTORY = "/mnt/sequences_tmp/melody_rnn/generated_tracks/%s";
    private static final String SEQUENCE_EXAMPLE_FILE = "/mnt/sequence_examples/%s/training_melodies.tfrecord";
    private static final String COUNTRY_SEQUENCE_EXAMPLE_FILE = "/mnt/sequence_examples/country/training_melodies.tfrecord";
    private static final String WORKING_DIRECTORY = "/root/magenta";
    private static final int NUM_TRAINING_STEPS = 1;
    private static final int NUM_RUN_STEPS = 1;

    /**
     * Runs commands to generate music and to train the neural network
     */

    private static MidiGeneratorRunner instance;

    public static MidiGeneratorRunner getInstance() {
        if (instance == null) instance = new MidiGeneratorRunner();
        return instance;
    }

    private MidiGeneratorRunner() {
    }

    int exitStatus;

    @PostConstruct
    public void init() {
        System.out.println("SERVER STARTED");
        for (MusicGenre genre : MusicGenre.values()) {
            for (Instrument instrument : genre.getInstruments()) {
                String currName = genre.name().toLowerCase() + "_" + instrument.name().toLowerCase();
                Thread toRun = new Thread(() -> {
                    try {
                        while (true) {
                            //System.out.println("Name of network: " + currName);
                            Process networkTrainer = Runtime.getRuntime().exec("echo test");
                            if (Files.notExists(Paths.get(String.format(RUN_DIRECTORY, currName)))) {
                                //System.out.println("Make RUNDIR for " + currName);
                                networkTrainer = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", "sudo bash make_rundir.bash " + currName + " " + NUM_TRAINING_STEPS}, null, new File(WORKING_DIRECTORY));
                                networkTrainer.waitFor();
                                //System.out.println("RUNDIR FOR " + currName + " CREATED");
                            }
                            Process magentaCommand = Runtime.getRuntime().exec("echo test");
                            if (Files.notExists(Paths.get(String.format(OUTPUT_DIRECTORY, currName))) || new File(String.format(OUTPUT_DIRECTORY, currName)).listFiles().length < 10) {
                                //System.out.println("Generate 1 file for " + currName);
                                magentaCommand = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", "sudo bash generate_file.bash " + currName + " " + NUM_RUN_STEPS}, null, new File(WORKING_DIRECTORY));
                                magentaCommand.waitFor();
                                //System.out.println("1 FILE FOR " + currName + " CREATED");
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("Could not start magenta! No training or generating will be done. Reason: " + e.getLocalizedMessage());
                        e.printStackTrace();
                        System.err.println();
                    }});
                toRun.start();
            }
        }
    }
}
