package com.musicmindproject.backend.magenta;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
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
     * runs a bash script for every neural network, when there are less than 10 generated tracks in the corresponding target directory
     */
    @PostConstruct
    public void init() {
        File logDir = new File(LOG_DIRECTORY);
        File[] runDirs = logDir.listFiles();
        for(File subdir: Objects.requireNonNull(runDirs)) {
            String currName = subdir.getName().replace("run_", "");
            Thread toRun = new Thread(() -> {
                while (true) {
                    try {
                        if (Files.notExists(Paths.get(String.format(OUTPUT_DIRECTORY, currName))) || Objects.requireNonNull(new File(String.format(OUTPUT_DIRECTORY, currName)).listFiles()).length < 10) {
                            Process magentaCommand = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", "sudo bash generate_file.bash " + currName + " " + NUM_RUN_STEPS}, null, new File(WORKING_DIRECTORY));
                            magentaCommand.waitFor();
                            magentaCommand = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", String.format("chmod -R 777 " + OUTPUT_DIRECTORY, "")});
                            magentaCommand.waitFor();
                        }

                        TimeUnit.SECONDS.sleep(5);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            toRun.start();
        }

    }

}
