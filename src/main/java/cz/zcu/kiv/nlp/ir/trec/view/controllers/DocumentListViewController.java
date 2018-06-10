package cz.zcu.kiv.nlp.ir.trec.view.controllers;

import cz.zcu.kiv.nlp.ir.trec.dev.IMockable;
import cz.zcu.kiv.nlp.ir.trec.domain.Document;
import cz.zcu.kiv.nlp.ir.trec.domain.IResult;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.paint.Color;


import java.awt.*;
import java.util.List;
import java.util.Map;

public class DocumentListViewController implements IMockable {


    /**
     * ----- GUI components -----
     */

    @FXML
    private TextFlow resultsTextFlow;

    @FXML
    private TextField searchQueryInput;


    /**
     * ----- business logic -----
     */

    private ObservableList<IResult> resultList;

    private SherlockController sherlockController;



    @FXML
    public void initialize() {
        //this.resultList = FXCollections.observableArrayList();
    }

    public void inject(SherlockController controller) {
        this.sherlockController = controller;
    }

    @FXML
    private void handleSearchClick(ActionEvent event) {
        String q = this.searchQueryInput.getText();
        if(q.length() > 0) this.sherlockController.runSearching(q);
    }

    public void printResults(List<IResult> results) {
        //this.resultsListView.getItems().clear();
        //this.resultsListView.getItems().addAll(results);

        //this.resultList = FXCollections.observableArrayList();
        //this.resultList.addAll(results);
        //this.resultsListView.setItems(this.resultList);

        //this.resultList.clear();
        //this.resultList.addAll(results);

        //StringBuilder sb = new StringBuilder();
        //for(IResult rs : results) sb.append(rs.serialize());
        //this.resultsTextArea.setText(sb.toString());

        this.resultsTextFlow.getChildren().clear();

        for(IResult rs : results) {

            // heading
            Text rankHeading = new Text(rs.getRank() + ". document" + "\n");
            rankHeading.getStyleClass().addAll("a-font", "a-bold", "a-red");

            Text idHeading = new Text("ID: ");
            idHeading.getStyleClass().addAll("a-font", "a-bold");
            Text idContent = new Text(rs.getDocumentId() + "\n");
            idContent.getStyleClass().addAll("a-font");

            // title
            String title = rs.getDocument().getTitle();
            if(title.startsWith("\n")) title = title.substring(1);
            if(!title.endsWith("\n")) title += "\n";

            Text titleHeading = new Text("title: ");
            titleHeading.getStyleClass().addAll("a-font", "a-bold");
            Text titleContent = new Text(title);
            titleContent.getStyleClass().addAll("a-font");

            // text
            String text = rs.getDocument().getText();
            if(text.startsWith("\n")) text = text.substring(1);
            if(!text.endsWith("\n")) text += "\n";

            Text textHeading = new Text("text: ");
            textHeading.getStyleClass().addAll("a-font", "a-bold");
            Text textContent = new Text(text);
            textContent.getStyleClass().addAll("a-font", "a-separator");

            this.resultsTextFlow.getChildren().addAll(
                rankHeading,
                idHeading,
                idContent,
                titleHeading,
                titleContent,
                textHeading,
                textContent,
                new Text("\n\n\n")
            );

            //this.resultsTextFlow.getChildren().addAll(heading);
        }
    }





    @Override
    public void initMockData() {
        //this.resultList = FXCollections.<String>observableArrayList("1;69;doc12", "2;34;doc5");
    }
}
