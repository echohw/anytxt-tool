package com.example.anytxttool.controller;

import com.example.anytxttool.objects.RuleFtlDataModel;
import de.felixroske.jfxsupport.FXMLController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.ToggleButton;

/**
 * Created by AMe on 2020-08-07 07:19.
 */
@FXMLController
public class ListRuleItemsController implements Initializable {

    private URL location;
    private ResourceBundle resources;

    @FXML
    private Button newItemBtn;
    @FXML
    private Button deleteItemsBtn;
    @FXML
    private Button refreshListBtn;
    @FXML
    private ToggleButton showTxtBtn;

    @FXML
    private TableColumn<RuleFtlDataModel, String> checkTableCol;
    @FXML
    private CheckBox batchChoiceCheckBox;

    @FXML
    public void newItem(ActionEvent event) {

    }

    @FXML
    public void deleteItems(ActionEvent event) {

    }

    @FXML
    public void refreshList(ActionEvent event) {

    }

    @FXML
    public void showTxt(ActionEvent event) {

    }

    @FXML
    public void batchChoice(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.location = location;
        this.resources = resources;
    }
}
