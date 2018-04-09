package com.musicmindproject.backend.magenta;

import com.musicmindproject.backend.entities.enums.Instrument;
import com.musicmindproject.backend.entities.enums.MusicGenre;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

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
        System.out.println("SERVER STARTED");
        for (MusicGenre genre : MusicGenre.values()) {
            for (Instrument instrument : genre.getInstruments()) {
                String currName = genre.name().toLowerCase() + "_" + instrument.name().toLowerCase();
                Thread toRun = new Thread(() -> {
                    try {
                        //while (true) {
//                            if (exitStatus == 0) {
                                /*if (Files.notExists(Paths.get(String.format(TFRECORD_FILE, currName)))) {
                                    System.out.println("NO DATASET FOR " + genre.name().toLowerCase() + "/" + instrument.name() + " FOUND, CREATING...");
                                    Process datasetGenerator = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", "source /root/miniconda2/bin/activate magenta; bazel run //magenta/scripts:convert_dir_to_note_sequences -- \\" +
                                            " --input_dir=" + String.format(AVAILABLE_MUSIC, currName) + " \\" +
                                            " --output_file=" + String.format(TFRECORD_FILE, currName) + " \\" +
                                            " --recursive"}, null, new File(WORKING_DIRECTORY));
                                    datasetGenerator.waitFor();
                                    System.out.println("CREATED DATASET FOR " + currName + "/" + instrument.name());

                                }
                                if(Files.notExists(Paths.get(String.format(GENERATED_DATASET, currName)))){
                                    Process datasetGenerator = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", "source /root/miniconda2/bin/activate magenta; bazel run //magenta/models/melody_rnn:melody_rnn_create_dataset -- \\" +
                                            " --config=attention_rnn \\" +
                                            " --input=" + String.format(TFRECORD_FILE, currName) + " \\" +
                                            " --output_dir=" + String.format(GENERATED_DATASET, currName)+ " \\" +
                                            " --eval_ratio=0.10"}, null, new File(WORKING_DIRECTORY));
                                    datasetGenerator.waitFor();
                                    System.out.println("CREATED EXAMPLE FOR " + currName + "/" + instrument.name());
                                }*/
                        System.out.println("Name of network: " + currName);
                        if (Files.notExists(Paths.get(String.format(RUN_DIRECTORY, currName)))) {
                            System.out.println("Make RUNDIR for " + currName);
                            Process networkTrainer = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", "source /root/miniconda2/bin/activate magenta && sudo -E -S -u root -i /bin/bash -l -c 'cd /root/magenta/ && bazel run //magenta/models/melody_rnn:melody_rnn_train -- \\" +
                                    " --config=attention_rnn \\" +
                                    " --run_dir=" + String.format(RUN_DIRECTORY, currName) + " \\" +
                                    " --sequence_example_file=" + String.format(COUNTRY_SEQUENCE_EXAMPLE_FILE, currName) + " \\" +
                                    " --hparams=\"batch_size=64,rnn_layer_sizes=[64,64]\" \\" +
                                    " --num_training_steps=" + NUM_TRAINING_STEPS + "'"}, null, new File(WORKING_DIRECTORY));
                            networkTrainer.waitFor();
                            System.out.println("RUNDIR FOR " + currName + " CREATED");
                        }

                        //noinspection InfiniteLoopStatement

                        Process magentaCommand = Runtime.getRuntime().exec("echo test");

                        if (Files.notExists(Paths.get(String.format(OUTPUT_DIRECTORY, currName))) || new File(String.format(OUTPUT_DIRECTORY, currName)).listFiles().length < 10) {
                            System.out.println("Generate 1 file for " + currName);
                            magentaCommand = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", "source /root/miniconda2/bin/activate magenta && sudo -E -S -u root -i /bin/bash -l -c 'cd /root/magenta/ && bazel run //magenta/models/melody_rnn:melody_rnn_generate -- \\" +
                                    " --config=attention_rnn \\" +
                                    " --run_dir=" + String.format(RUN_DIRECTORY, currName) + " \\" +
                                    " --output_dir=" + String.format(OUTPUT_DIRECTORY, currName) + " \\" +
                                    " --num_outputs=1 \\" +
                                    " --num_steps=" + NUM_RUN_STEPS + " \\" +
                                    " --hparams=\"batch_size=64,rnn_layer_sizes=[64,64]\" \\" +
                                    " --primer_melody=\"[60]\"'"}, null, new File(WORKING_DIRECTORY));
                            //magentaCommand.waitFor();
                            System.out.println("1 FILE FOR " + currName + " CREATED");
                        }


                        BufferedReader stdInput = new BufferedReader(new
                                InputStreamReader(magentaCommand.getInputStream()));

                        BufferedReader stdError = new BufferedReader(new
                                InputStreamReader(magentaCommand.getErrorStream()));
                        System.out.println("Here is the standard output of the command:\n");
                        String s = null;
                        while ((s = stdInput.readLine()) != null) {
                            System.out.println(s);
                        }
                        System.out.println("Here is the standard error of the command (if any):\n");
                        while ((s = stdError.readLine()) != null) {
                            System.out.println(s);
                        }

                        //} else {


                            //System.out.println("TRAINING FOR " + genre.name() + " FINISHED (" + NUM_TRAINING_STEPS + " steps)");
                        //}


                        //}
                    } catch (Exception e) {
                        System.err.println("Could not start magenta! No training or generating will be done. Reason: " + e.getLocalizedMessage());
                        e.printStackTrace();
                        System.err.println();
                    }
                });
                toRun.start();
            }
        }
    }
}
