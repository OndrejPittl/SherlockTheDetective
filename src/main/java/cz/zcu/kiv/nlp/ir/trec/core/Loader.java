package cz.zcu.kiv.nlp.ir.trec.core;

import cz.zcu.kiv.nlp.ir.trec.config.LoaderConfig;
import cz.zcu.kiv.nlp.ir.trec.domain.Document;
import cz.zcu.kiv.nlp.ir.trec.domain.IDocument;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cz.zcu.kiv.nlp.ir.trec.sherlock.TestTrecEval.OUTPUT_DIR;


public class Loader {

    private static final Logger log = Logger.getLogger(Loader.class);

    private Map<Integer, Document> loadedData;


    public Loader() {
        this.loadedData = new HashMap<>();
    }

    /**
     *  Loads test data.
     */
    public Map<Integer, Document> process() {
        this.loadedData.clear();

        // loaded docs
        List<IDocument> loadedDocs;

        // path
        String filename = OUTPUT_DIR + LoaderConfig.PATH_FILE;
        File serializedData = new File(filename);

        try {
            if (serializedData.exists()) {
                loadedDocs = SerializedDataHelper.loadDocument(serializedData);

                int docLoaded = 0;
                for(IDocument document : loadedDocs){
                    this.loadedData.put(document.getId(), (Document) document);
                    //if(++docLoaded >= 50) break;
                }
            } else {
                log.info(Thread.currentThread().getName() + ": " + filename + " not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this.loadedData;
    }
}
