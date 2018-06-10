package cz.zcu.kiv.nlp.ir.trec.sherlock;

import cz.zcu.kiv.nlp.ir.trec.config.AppConfig;
import cz.zcu.kiv.nlp.ir.trec.config.Routes;
import cz.zcu.kiv.nlp.ir.trec.domain.AppStates;
import cz.zcu.kiv.nlp.ir.trec.view.controllers.SherlockController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;


public class Sherlock {

    private static final Logger log = Logger.getLogger(Sherlock.class);

    /**
     * State of the app.
     */
    public static AppStates appState;

    /**
     * Sync threads.
     */
    private static CyclicBarrier barrier;

    /**
     * DetectiveWorker thread.
     */
    private static Thread detectiveThrd;
    private static DetectiveWorker detectiveRunnable;

    private SherlockController controller;



    public Sherlock(Stage primaryStage) {
        this.init();
        this.initGui(primaryStage);
    }

    private void init() {
        Sherlock.barrier = new CyclicBarrier(2);
        Sherlock.detectiveRunnable = new DetectiveWorker();
        Sherlock.detectiveThrd = new Thread(Sherlock.detectiveRunnable);
        Sherlock.detectiveThrd.setDaemon(true);
        Sherlock.appState = AppStates.INITIALIZED;
        Thread.currentThread().setName("GuiThread");
        log.info(Thread.currentThread().getName() + ": Threads initialized.");
    }

    public void start() {
        Sherlock.detectiveThrd.start();
    }


    public void changeAppState(AppStates state) {
        Sherlock.appState = state;

        switch (state) {

            case INITIALIZED: break;

            // urls are set, start pre-processing
            case CRAWLING:
                this.controller.setBusy(true);
                break;

            case LOADING:
                this.controller.setBusy(true);
                break;

            case PREPROCESSING_INDEXING:
                // should not be reached here
                break;

            case PREPARED:
                break;

            case SEARCHING:
                this.controller.setBusy(true);
                break;

            case SHUTDOWN:
                break;
        }

        Sherlock.awaitAtBarrier();
    }


    public static void awaitAtBarrier() {
        log.info(Thread.currentThread().getName() + ": awaits at barrier.");

        try {
            Sherlock.barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        log.info(Thread.currentThread().getName() + " breaks barrier and continues running.");
    }

    private void initGui(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource( Routes.PATH_VIEW + "/sherlock.fxml"));
            Parent root = loader.load();

            this.controller = loader.getController();
            this.controller.inject(this);
            this.controller.onInitialized();
            Sherlock.detectiveRunnable.inject(this, this.controller);

            Scene scene = new Scene(root, AppConfig.VIEW_WIDTH, AppConfig.VIEW_HEIGHT);
            scene.getStylesheets().add(Routes.PATH_STYLE + "/styles.css");
            primaryStage.setTitle(AppConfig.APP_NAME);
            primaryStage.setScene(scene);
            primaryStage.show();

            log.info(Thread.currentThread().getName() + ": GUI rendered.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
