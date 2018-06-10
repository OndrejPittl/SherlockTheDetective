package cz.zcu.kiv.nlp.ir.trec.sherlock;

import cz.zcu.kiv.nlp.ir.trec.core.*;
import cz.zcu.kiv.nlp.ir.trec.domain.Document;
import cz.zcu.kiv.nlp.ir.trec.domain.IResult;
import cz.zcu.kiv.nlp.ir.trec.domain.Link;
import cz.zcu.kiv.nlp.ir.trec.view.controllers.SherlockController;
import javafx.application.Platform;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;



public class DetectiveWorker implements Runnable {

    private static final Logger log = Logger.getLogger(DetectiveWorker.class);

    private Sherlock sherlock;

    private SherlockController sherlockController;


    private Loader loader;

    private Crawler crawler;

    private Preprocessor preprocessor;

    private Indexer index;



    private List<Link> urlList;
    private boolean deepMining;
    private Map<Integer, Document> crawledData = null;
    private List<IResult> results;



    public DetectiveWorker() {
        this.init();
    }

    private void init() {
        this.loader = new Loader();
        this.crawler = new Crawler();
        this.preprocessor = new Preprocessor();
        this.index = new Indexer();
        this.deepMining = false;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("DetectiveThread");
        log.info(Thread.currentThread().getName() + ": DetectiveThread: started.");


        while(true) {
            Sherlock.awaitAtBarrier();

            switch (Sherlock.appState) {
                case INITIALIZED:
                    break;

                case CRAWLING:

                    // options from GUI
                    this.urlList = this.sherlockController.getUrlList();
                    this.deepMining = this.sherlockController.isDeepMining();

                    // crawl
                    log.info(Thread.currentThread().getName() + ": beginning crawling & extracting.");
                    this.crawledData = this.crawler.process(this.urlList, this.deepMining);

                    // no break –> loading –> preprocessing & indexing

                case LOADING:

                    // crawling was not preceding
                    if(this.crawledData == null) {
                        this.crawledData = this.loader.process();
                        log.info(Thread.currentThread().getName() + ": " + this.crawledData.size() + "x doc");
                    }

                    // no break –> preprocessing & indexing

                case PREPROCESSING_INDEXING:

                    if(this.crawledData == null) {
                        break;
                    }

                    // pre-process
                    log.info(Thread.currentThread().getName() + ": beginning pre-processing.");
                    this.crawledData = this.preprocessor.process(this.crawledData);

                    // indexing
                    log.info(Thread.currentThread().getName() + ": beginning indexing.");
                    this.index.index(this.crawledData);

                    // done
                    Platform.runLater(() -> sherlockController.notifyIndexingDone());
                    break;


                case PREPARED:
                    System.out.println("DetectiveWorker: done, waiting 4 search query.");
                    break;

                case SEARCHING:
                    log.info(Thread.currentThread().getName() + ": searching.");
                    String query = this.sherlockController.getQuery();
                    this.results = this.index.search(query);

                    // done
                    Platform.runLater(() -> sherlockController.notifySearchingDone(this.results));
                    break;
            }
        }
    }

    public void inject(Sherlock sherlock, SherlockController controller) {
        this.sherlock = sherlock;
        this.sherlockController = controller;
    }
}