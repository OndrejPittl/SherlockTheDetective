package cz.zcu.kiv.nlp.ir.trec.view.partial;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class DeletableListCell extends ListCell<String> {

    private HBox hbox;

    private Label label;

    private Pane pane;

    private Button button;


    public DeletableListCell() {
        super();
        this.init();
    }

    private void init() {
        this.hbox = new HBox();
        this.label = new Label("");
        this.pane = new Pane();
        this.button = new Button("–");

        this.button.setVisible(false);
        this.button.getStyleClass().add("deletable-list-view__btn");
        //this.hbox.getChildren().addAll(label, pane, button);
        this.hbox.getChildren().addAll(button, label);
        HBox.setMargin(label, new Insets(4,10,0,10));
        HBox.setHgrow(pane, Priority.ALWAYS);
        this.button.setOnAction(event -> getListView().getItems().remove(getItem()));
        this.getStyleClass().add("deletable-list-view__item");
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        setGraphic(null);

        if (item != null && !empty) {
            label.setText(item);
            setGraphic(hbox);
        }
    }
}