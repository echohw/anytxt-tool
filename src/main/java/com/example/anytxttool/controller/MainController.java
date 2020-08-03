package com.example.anytxttool.controller;

import com.example.anytxttool.manager.ToolConfigManager;
import com.example.anytxttool.objects.enums.RuleType;
import com.example.anytxttool.service.AnytxtToolService;
import de.felixroske.jfxsupport.FXMLController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by AMe on 2020-08-02 13:38.
 */
@FXMLController
public class MainController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    private URL location;
    private ResourceBundle resources;

    @Autowired
    private ToolConfigManager toolConfigManager;
    @Autowired
    private AnytxtToolService anytxtToolService;

    @FXML
    private Button startScanBtn;
    @FXML
    private Button resetIndexStatBtn;
    @FXML
    private ListView<String> ignoreDirRegexListView;
    @FXML
    private ListView<String> fileTypeListView;
    @FXML
    private ListView<String> scanPathListView;
    @FXML
    private TextArea ignoredDirTextArea;
    @FXML
    private ChoiceBox<RuleType> ruleTypeChoiceBox;

    @FXML
    public void startScan() {

    }

    @FXML
    public void resetIndexStat() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.location = location;
        this.resources = resources;

        ignoreDirRegexListView.setItems(FXCollections.observableArrayList(toolConfigManager.getIgnoreDirRegexList()));
        fileTypeListView.setItems(FXCollections.observableArrayList(toolConfigManager.getFileTypeList()));
        scanPathListView.setItems(FXCollections.observableArrayList(toolConfigManager.getScanPathList()));
        // 索引规则选择框
        ruleTypeChoiceBox.setItems(FXCollections.observableArrayList(RuleType.values()));
        ruleTypeChoiceBox.getSelectionModel().select(RuleType.EXCLUDE_DIR); // default option
        // ruleTypeChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {});
        ruleTypeChoiceBox.converterProperty().set(new StringConverter<RuleType>() {
            @Override
            public String toString(RuleType ruleType) {
                return ruleType.getDisplayName();
            }

            @Override
            public RuleType fromString(String display) {
                for (RuleType ruleType : RuleType.values()) {
                    if (ruleType.getDisplayName().equals(display)) {
                        return ruleType;
                    }
                }
                return null;
            }
        });
    }
}
