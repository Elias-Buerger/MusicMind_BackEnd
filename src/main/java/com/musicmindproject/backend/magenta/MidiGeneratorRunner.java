package com.musicmindproject.backend.magenta;

import com.musicmindproject.backend.entities.enums.MusicGenre;

import javax.annotation.PostConstruct;
import javax.ejb.Startup;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

@Startup
public class MidiGeneratorRunner {

    private static final String AVAILABLE_MUSIC = "/mnt/midifiles/%s";
    private static final String GENERATED_DATASET = "/mnt/sequence_examples/%s";
    private static final String TFRECORD_FILE = "/mnt/sequences_tmp/%s.tfrecord";
    private static final String RUN_DIRECTORY = "/mnt/sequences_tmp/melody_rnn/logdir/run_%s";
    private static final String OUTPUT_DIRECTORY = "/mnt/sequences_tmp/melody_rnn/generated_tracks/%s";
    private static final String SEQUENCE_EXAMPLE_FILE = "/mnt/sequence_examples/%s/training_melodies.tfrecord";
    private static final String WORKING_DIRECTORY = "/root/magenta";
    private static final int NUM_TRAINING_STEPS = 200;
    private static final int NUM_RUN_STEPS = 128;

    /**
     * Runs commands to generate music and to train the neural network
     */

    private static MidiGeneratorRunner instance;
    public static MidiGeneratorRunner getInstance() {
        if(instance == null) instance = new MidiGeneratorRunner();
        return instance;
    }

    private MidiGeneratorRunner() {  }

    int exitStatus;

    @PostConstruct
    public void init() {
//        try {
//            Process activationProcess = Runtime.getRuntime().exec(new String[]{"bash", "-c", "source activate magenta"}, null, new File(WORKING_DIRECTORY));
//            exitStatus = activationProcess.waitFor();
//            System.out.println("ACTIVATED MAGENTA");
//        } catch (IOException | InterruptedException e) {
//            System.err.println(e.getLocalizedMessage());
//            exitStatus = -1;
//        }

        for (MusicGenre genre : MusicGenre.values()) {
            //JUST FOR TESTING
            if (genre == MusicGenre.COUNTRY) {

                //for (Instrument instrument : genre.getInstruments()) {
                    Thread toRun = new Thread(() -> {
                        try {
                            while (true) {
//                            if (exitStatus == 0) {
                                if (!Files.exists(Paths.get(String.format(TFRECORD_FILE, genre.name().toLowerCase())))) {
                                    System.out.println("NO DATASET FOR " + genre.name().toLowerCase() /*+ "/" + instrument.name()*/ + " FOUND, CREATING...");
                                    Process datasetGenerator = Runtime.getRuntime().exec(new String[]{"bash", "-c", "source activate magenta; bazel run //magenta/scripts:convert_dir_to_note_sequences -- \\" +
                                            " --input_dir=" + String.format(AVAILABLE_MUSIC, genre.name().toLowerCase()) + " \\" +
                                            " --output_file=" + String.format(TFRECORD_FILE, genre.name().toLowerCase()) + " \\" +
                                            " --recursive"}, null, new File(WORKING_DIRECTORY));
                                    datasetGenerator.waitFor();
                                    System.out.println("CREATED DATASET FOR " + genre.name().toLowerCase() /*+ "/" + instrument.name()*/);

                                }
                                if(!Files.exists(Paths.get(String.format(GENERATED_DATASET, genre.name().toLowerCase())))){
                                    Process datasetGenerator = Runtime.getRuntime().exec(new String[]{"bash", "-c", "source activate magenta; bazel run //magenta/models/melody_rnn:melody_rnn_create_dataset -- \\" +
                                            " --config=attention_rnn \\" +
                                            " --input=" + String.format(TFRECORD_FILE, genre.name().toLowerCase()) + " \\" +
                                            " --output_dir=" + String.format(GENERATED_DATASET, genre.name().toLowerCase())+ " \\" +
                                            " --eval_ratio=0.10"}, null, new File(WORKING_DIRECTORY));
                                    datasetGenerator.waitFor();
                                    System.out.println("CREATED EXAMPLE FOR " + genre.name().toLowerCase() /*+ "/" + instrument.name()*/);
                                }

                                //noinspection InfiniteLoopStatement

                                    Process magentaCommand;
                                    if(!Files.exists(Paths.get(String.format(OUTPUT_DIRECTORY, genre.name().toLowerCase())))) {
                                        File dir = new File(String.format(OUTPUT_DIRECTORY, genre.name().toLowerCase()));
                                        if(!dir.mkdir())
                                            break;
                                    }

                                    if (Objects.requireNonNull(new File(String.format(OUTPUT_DIRECTORY, genre.name().toLowerCase())).listFiles()).length < 50) {
                                        magentaCommand = Runtime.getRuntime().exec(new String[]{"bash", "-c", "source activate magenta && bazel run //magenta/models/melody_rnn:melody_rnn_train -- \\" +
                                                " --config=attention_rnn \\" +
                                                " --run_dir=" + String.format(RUN_DIRECTORY, genre.name().toLowerCase()) + " \\" +
                                                " --sequence_example_file=" + String.format(SEQUENCE_EXAMPLE_FILE, genre.name().toLowerCase()) + " \\" +
                                                " --hparams=\"batch_size=64,rnn_layer_sizes=[64,64]\" \\" +
                                                " --num_training_steps=" + NUM_TRAINING_STEPS}, null, new File(WORKING_DIRECTORY));

                                        //System.out.println("TRAINING FOR " + genre.name() + " FINISHED (" + NUM_TRAINING_STEPS + " steps)");
                                    } else {
                                        magentaCommand = Runtime.getRuntime().exec(new String[]{"bash", "-c", "source activate magenta && bazel run //magenta/models/melody_rnn:melody_rnn_generate -- \\" +
                                                " --config=attention_rnn \\" +
                                                " --run_dir=" + String.format(RUN_DIRECTORY, genre.name().toLowerCase()) + " \\" +
                                                " --output_dir=" + String.format(OUTPUT_DIRECTORY, genre.name().toLowerCase()) + " \\" +
                                                " --num_outputs=1 \\" +
                                                " --num_steps=" + NUM_RUN_STEPS + " \\" +
                                                " --hparams=\"batch_size=64,rnn_layer_sizes=[64,64]\" \\" +
                                                " --primer_melody=\"[60]\""}, null, new File(WORKING_DIRECTORY));
                                        //System.out.println("1 FILE FOR " + genre.name().toLowerCase() + " CREATED");
                                    }

                                    magentaCommand.waitFor();
                                }

//                            } else {
//                                System.err.println("Error: Code " + exitStatus);
//                            }
                        } catch (IOException | InterruptedException e) {
                            System.err.println("Could not start magenta! No training or generating will be done. Reason: " + e.getLocalizedMessage());
                            e.printStackTrace();
                            System.err.println();
                        }
                    });

                    toRun.start();
                //}
            }
        }
    }
}
