package cz.zcu.kiv.nlp.ir.trec.view.controllers;

import cz.zcu.kiv.nlp.ir.trec.config.ViewConfig;
import cz.zcu.kiv.nlp.ir.trec.domain.Document;
import cz.zcu.kiv.nlp.ir.trec.domain.IResult;
import cz.zcu.kiv.nlp.ir.trec.domain.Link;
import cz.zcu.kiv.nlp.ir.trec.sherlock.Sherlock;
import cz.zcu.kiv.nlp.ir.trec.domain.AppStates;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SherlockController {


    /**
     * ----- GUI components -----
     */

    @FXML
    private UrlListViewController urlListViewController;

    @FXML
    private DocumentListViewController resultsListViewController;

    @FXML
    private BorderPane urlListViewContainer;

    @FXML
    private VBox loaderOverlay;


    /**
     * ----- business logic -----
     */

    private Sherlock sherlock;

    private List<Link> urlList;

    private boolean deepMining;

    private String query;



    public SherlockController() {
        this.deepMining = false;
    }


    @FXML
    public void initialize() {
        this.urlListViewController.inject(this);
        this.resultsListViewController.inject(this);
    }

    @FXML
    private void handleClearUrlsClick() {
        this.urlListViewController.clearUrlList();
    }

    @FXML
    private void handleExportClick() {

    }



    public void runCrawling() {
        this.urlList = new ArrayList<>();

        for(String u : this.urlListViewController.getUrlList()) {
            this.urlList.add(new Link(u));
        }

        this.deepMining = this.urlListViewController.isDeepMining();
        this.sherlock.changeAppState(AppStates.CRAWLING);
        this.setDisabled(true);
    }

    public void onInitialized(){
        Platform.runLater(this::askCrawling);
    }


    private void askCrawling() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(ViewConfig.DIALOG_CRAWL_OR_PREDEFINED_TITLE);
        alert.setHeaderText(ViewConfig.DIALOG_CRAWL_OR_PREDEFINED_HEADER);
        alert.setContentText(ViewConfig.DIALOG_CRAWL_OR_PREDEFINED_TEXT);

        ButtonType btnCustom = new ButtonType(ViewConfig.BTN_CUSTOM_DOCUMENTS);
        ButtonType btnPredefined = new ButtonType(ViewConfig.BTN_PREDEFINED_DOCUMENTS);
        alert.getButtonTypes().setAll(btnPredefined, btnCustom);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnPredefined){
            System.out.println("sherlock controller:askCrawling");
            this.sherlock.changeAppState(AppStates.LOADING);
            this.setDisabled(true);
            //Sherlock.awaitAtBarrier();
        }
    }

    public void notifyIndexingDone() {
        Platform.runLater(() -> setBusy(false));
    }

    public void notifySearchingDone(List<IResult> results) {
        Platform.runLater(() -> {
            this.resultsListViewController.printResults(results);
            setBusy(false);
        });
    }

    public void runSearching(String query) {
        this.query = query;
        this.sherlock.changeAppState(AppStates.SEARCHING);
    }



    public void setBusy(boolean busy) {
        this.loaderOverlay.setVisible(busy);
    }

    public void setDisabled(boolean disabled) {
        this.urlListViewContainer.setDisable(disabled);
    }

    public void inject(Sherlock sherlock){
        this.sherlock = sherlock;
    }

    public List<Link> getUrlList() {
        return this.urlList;
    }

    public boolean isDeepMining() {
        return this.deepMining;
    }

    public String getQuery() {
        return this.query;
    }
}
