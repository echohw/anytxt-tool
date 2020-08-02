package com.example.anytxttool.controller;

import de.felixroske.jfxsupport.FXMLController;
import java.awt.TextArea;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

/**
 * Created by AMe on 2020-08-02 13:38.
 */
@FXMLController
public class MainController implements Initializable {

    @FXML
    private Button btn;

    @FXML
    public void btnClick() {
        System.out.println("点击");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
