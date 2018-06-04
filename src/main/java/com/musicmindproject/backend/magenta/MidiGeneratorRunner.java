package com.musicmindproject.backend.magenta;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Startup
@Singleton
public class MidiGeneratorRunner {
    private static final String LOG_DIRECTORY = "/mnt/network_training/melody_rnn/logdir";
    private static final String OUTPUT_DIRECTORY = "/mnt/generated_tracks/%s";
    private static final String WORKING_DIRECTORY = "/root/magenta";
    private static final int NUM_RUN_STEPS = 128;

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
        File logDir = new File(LOG_DIRECTORY);
        File[] runDirs = logDir.listFiles();
        for(File subdir: runDirs) {
            String currName = subdir.getName().replace("run_", "");
            Thread toRun = new Thread(() -> {
                while (true) {
                    try {
                        if (Files.notExists(Paths.get(String.format(OUTPUT_DIRECTORY, currName))) || Objects.requireNonNull(new File(String.format(OUTPUT_DIRECTORY, currName)).listFiles()).length < 10) {
                            //System.out.println("Generate 1 file for " + currName);
                            Process magentaCommand = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", "sudo bash generate_file.bash " + currName + " " + NUM_RUN_STEPS}, null, new File(WORKING_DIRECTORY));
                            magentaCommand.waitFor();
                            magentaCommand = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", String.format("chmod -R 777 " + OUTPUT_DIRECTORY, "")});
                            //System.out.println("1 FILE FOR " + currName + " CREATED");
                        }

                        TimeUnit.SECONDS.sleep(5);
                    } catch (Exception e) {
                        System.err.println("Could not start magenta! No training or generating will be done. Reason: " + e.getLocalizedMessage());
                        e.printStackTrace();
                        System.err.println();
                    }
                }
            });
            toRun.start();
        }

    }

}
