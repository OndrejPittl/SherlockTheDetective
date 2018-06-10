package cz.zcu.kiv.nlp.ir.trec.view.controllers;

import cz.zcu.kiv.nlp.ir.trec.dev.IMockable;
import cz.zcu.kiv.nlp.ir.trec.sherlock.Validator;
import cz.zcu.kiv.nlp.ir.trec.view.partial.DeletableListCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.List;

import static cz.zcu.kiv.nlp.ir.trec.config.AppConfig.WITH_MOCK_DATA;

public class UrlListViewController implements IMockable {

    /**
     * ----- GUI components -----
     */

    @FXML
    private ListView urlListView;

    @FXML
    private TextField urlInputField;

    @FXML
    private CheckBox deepMiningCheckbox;




    /**
     * ----- business logic -----
     */

    private ObservableList<String> urlList;

    private SherlockController sherlockController;
    private boolean deepMining;


    @FXML
    public void initialize() {
        this.urlListView.getStyleClass().add("deletable-list-view");
        this.urlListView.setCellFactory(param -> new DeletableListCell());
    }

    public void inject(SherlockController controller) {
        if(WITH_MOCK_DATA) this.initMockData();
        this.urlListView.setItems(this.urlList);
        this.sherlockController = controller;
    }

    @FXML
    private void handleAddUrlBtnClick(ActionEvent event) {
        String url = this.urlInputField.getText();

        if(!Validator.validateUrl(url)) {
            this.urlInputField.getStyleClass().add("error-input");
            return;
        }

        boolean found = false;
        for(Object o : this.urlListView.getItems()) {
            if(o.equals(url)) {
                found = true;
            }
        }

        if(!found) {
            this.urlList.add(url);
        }

        this.urlInputField.setText("");
        this.urlInputField.getStyleClass().clear();
        this.urlInputField.getStyleClass().addAll("text-field", "text-input");

    }

    @FXML
    private void handleRunClick() {
        if(!this.urlList.isEmpty()) this.sherlockController.runCrawling();
    }

    public void clearUrlList() {
        this.urlList.clear();
    }

    public List<String> getUrlList() {
        return this.urlList;
    }

    @Override
    public void initMockData() {
        this.urlList = FXCollections.observableArrayList(
                "https://www.recepty.cz/recept/testoviny-se-smetanovou-omackou-a-lososem-156496"
                , "https://www.recepty.cz/recept/rukolovy-salat-s-novymi-bramborami-162178"
                //, "https://www.recepty.cz/recepty/rychly-tip/zdravy-obed"
        );
    }

    public boolean isDeepMining() {
        return this.deepMiningCheckbox.isSelected();
    }
}
