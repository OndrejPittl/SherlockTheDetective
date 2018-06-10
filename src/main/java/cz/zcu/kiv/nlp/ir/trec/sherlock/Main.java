package cz.zcu.kiv.nlp.ir.trec.sherlock;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.apache.log4j.Logger;


public class Main extends Application {

    private static final Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        Main.initialize();
        launch(args);
    }

    private static void initialize(){
        log.info(Thread.currentThread().getName() + ": Launching app.");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        new Sherlock(primaryStage).start();
    }

    @Override
    public void stop() {
        Platform.exit();
    }
}
