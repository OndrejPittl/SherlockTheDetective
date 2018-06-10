package cz.zcu.kiv.nlp.ir.trec.sherlock;

import cz.zcu.kiv.nlp.ir.trec.core.Indexer;
import cz.zcu.kiv.nlp.ir.trec.core.Loader;
import cz.zcu.kiv.nlp.ir.trec.core.Preprocessor;
import cz.zcu.kiv.nlp.ir.trec.core.SerializedDataHelper;
import cz.zcu.kiv.nlp.ir.trec.data.Topic;
import cz.zcu.kiv.nlp.ir.trec.domain.Document;
import cz.zcu.kiv.nlp.ir.trec.domain.IDocument;
import cz.zcu.kiv.nlp.ir.trec.domain.IResult;
import cz.zcu.kiv.nlp.ir.trec.io.IOUtils;
import org.apache.log4j.*;

import java.io.*;
import java.util.*;


/**
 * @author tigi
 */

public class TestTrecEval {

    private static Logger log = Logger.getRootLogger();

    public static final String OUTPUT_DIR = "./TREC";

    protected static void configureLogger() {
        BasicConfigurator.resetConfiguration();
        BasicConfigurator.configure();

        File results = new File(OUTPUT_DIR);
        if (!results.exists()) {
            results.mkdir();
        }

        try {
            Appender appender = new WriterAppender(new PatternLayout(), new FileOutputStream(new File(OUTPUT_DIR + "/" + SerializedDataHelper.SDF.format(System.currentTimeMillis()) + " - " + ".log"), false));
            BasicConfigurator.configure(appender);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Logger.getRootLogger().setLevel(Level.INFO);
    }

    public static void main(String args[]) throws IOException {
        configureLogger();

        List<Topic> topics = SerializedDataHelper.loadTopic(new File(OUTPUT_DIR + "/topicData.bin"));

        Map<Integer, Document> crawledData;
        Loader loader = new Loader();
        Preprocessor preprocessor = new Preprocessor();
        Indexer index = new Indexer();

        // load data
        crawledData = loader.process();
        log.info("Documents: " + crawledData.size());

        // pre-process
        log.info(Thread.currentThread().getName() + ": beginning pre-processing.");
        crawledData = preprocessor.process(crawledData);

        // index
        log.info(Thread.currentThread().getName() + ": beginning indexing.");
        index.index(crawledData);


        List<String> lines = new ArrayList<>();

        for (Topic t : topics) {
            List<IResult> resultHits = index.search(t.getTitle() + " " + t.getDescription());

            // -- already sorted
            /*Comparator<IResult> cmp = (o1, o2) -> {
                if (o1.getScore() > o2.getScore()) return -1;
                if (o1.getScore() == o2.getScore()) return 0;
                return 1;
            };
            Collections.sort(resultHits, cmp);*/


            for (IResult r : resultHits) {
                final String line = r.toString(t.getId());
                lines.add(line);
            }
            if (resultHits.size() == 0) {
                lines.add(t.getId() + " Q0 " + "abc" + " " + "99" + " " + 0.0 + " runindex1");
            }
        }

        final File outputFile = new File(OUTPUT_DIR + "/results " + SerializedDataHelper.SDF.format(System.currentTimeMillis()) + ".txt");
        IOUtils.saveFile(outputFile, lines);

        //try to run evaluation
        try {
            runTrecEval(outputFile.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String runTrecEval(String predictedFile) throws IOException {

        String commandLine = "./trec_eval.8.1/./trec_eval" +
                " ./trec_eval.8.1/czech" +
                " " + predictedFile;

        System.out.println(commandLine);
        Process process = Runtime.getRuntime().exec(commandLine);

        BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        String trecEvalOutput;
        StringBuilder output = new StringBuilder("TREC EVAL output:\n");
        for (String line; (line = stdout.readLine()) != null; ) output.append(line).append("\n");
        trecEvalOutput = output.toString();
        System.out.println(trecEvalOutput);

        int exitStatus = 0;
        try {
            exitStatus = process.waitFor();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        System.out.println(exitStatus);

        stdout.close();
        stderr.close();

        return trecEvalOutput;
    }
}
