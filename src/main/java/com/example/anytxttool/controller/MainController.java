package com.example.anytxttool.controller;

import com.example.anytxttool.entity.IndexStat;
import com.example.anytxttool.manager.ToolConfigManager;
import com.example.anytxttool.objects.IndexStatVO;
import com.example.anytxttool.objects.RuleFtlDataModel;
import com.example.anytxttool.objects.enums.EntityStat;
import com.example.anytxttool.objects.enums.RuleType;
import com.example.anytxttool.service.AnytxtToolService;
import com.example.anytxttool.view.MainView;
import de.felixroske.jfxsupport.FXMLController;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import org.jooq.lambda.Unchecked;
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
    private MainView mainView;
    @Autowired
    private ToolConfigManager toolConfigManager;
    @Autowired
    private AnytxtToolService anytxtToolService;

    // @FXML
    // private Button startScanBtn;
    // @FXML
    // private Button resetIndexStatBtn;
    // @FXML
    // private ListView<String> ignoreDirRegexListView;
    // @FXML
    // private ListView<String> fileTypeListView;
    // @FXML
    // private ListView<String> scanPathListView;
    // @FXML
    // private TextArea ignoredDirTextArea;
    // @FXML
    // private ChoiceBox<RuleType> ruleTypeChoiceBox;
    //
    // @FXML
    // public void startScan() {
    //
    // }
    //
    // @FXML
    // public void resetIndexStat() {
    //
    // }

    @FXML
    private TableView<IndexStatVO> indexStatTableView;
    @FXML
    private CheckBox batchChoiceCheckBox;
    @FXML
    private TableColumn<IndexStatVO, Boolean> checkTableCol;
    @FXML
    private TableColumn<IndexStatVO, Integer> orderTableCol;
    @FXML
    private TableColumn<IndexStatVO, String> extTableCol;
    @FXML
    private TableColumn<IndexStatVO, String> statTableCol;
    @FXML
    private TableColumn<IndexStatVO, Integer> totalTableCol;
    @FXML
    private TableColumn<IndexStatVO, String> ruleTableCol;
    @FXML
    private TableColumn<IndexStatVO, String> optTableCol;

    @FXML
    public void batchChoice(ActionEvent event) {
        CheckBox checkBox = (CheckBox) event.getSource();
        boolean checkAll = indexStatTableView.getItems().stream().anyMatch(indexStatVO -> !indexStatVO.isSelected());
        checkBox.setSelected(checkAll);
        indexStatTableView.getItems().forEach(indexStatVO -> indexStatVO.setSelected(checkAll));
    }

    @FXML
    public void filterItem() {

    }

    @FXML
    public void newItem() {

    }

    @FXML
    public void delItems() {
        boolean hasSelected = indexStatTableView.getItems().stream().anyMatch(IndexStatVO::isSelected);
        if (!hasSelected) {
            Alert alert = new Alert(AlertType.INFORMATION, "请选择要删除的项");
            alert.show();
            return;
        }
        ButtonType okBtn = new ButtonType("确定", ButtonData.YES);
        ButtonType cancelBtn = new ButtonType("取消", ButtonData.NO);
        Alert alert = new Alert(Alert.AlertType.WARNING, "", okBtn, cancelBtn);
        alert.setHeaderText("您正在执行删除操作！");
        List<IndexStatVO> indexStatVOList = indexStatTableView.getItems().stream().filter(IndexStatVO::isSelected).collect(Collectors.toList());
        String delItemInfo = indexStatVOList.stream().map(IndexStatVO::getExt).limit(5).collect(Collectors.joining("、"));
        alert.setContentText(String.format("即将删除%s等文件类型的%s项数据，是否继续？", delItemInfo, indexStatVOList.size()));
        Optional<ButtonType> buttonType = alert.showAndWait();
        buttonType.ifPresent(btnType -> {
            if (btnType.equals(okBtn)) {
                anytxtToolService.deleteIndexStatByIdIn(indexStatVOList.stream().map(IndexStatVO::getId).collect(Collectors.toList()));
                batchChoiceCheckBox.setSelected(false);
                this.refresh();
            }
        });
    }

    @FXML
    public void refresh() {
        this.initTableView();
        batchChoiceCheckBox.setSelected(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.location = location;
        this.resources = resources;

        this.initTableView();
        // ignoreDirRegexListView.setItems(FXCollections.observableArrayList(toolConfigManager.getIgnoreDirRegexList()));
        // fileTypeListView.setItems(FXCollections.observableArrayList(toolConfigManager.getFileTypeList()));
        // scanPathListView.setItems(FXCollections.observableArrayList(toolConfigManager.getScanPathList()));
        // // 索引规则选择框
        // ruleTypeChoiceBox.setItems(FXCollections.observableArrayList(RuleType.values()));
        // ruleTypeChoiceBox.getSelectionModel().select(RuleType.EXCLUDE_DIR); // default option
        // // ruleTypeChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {});
        // ruleTypeChoiceBox.converterProperty().set(new StringConverter<RuleType>() {
        //     @Override
        //     public String toString(RuleType ruleType) {
        //         return ruleType.getDisplayName();
        //     }
        //
        //     @Override
        //     public RuleType fromString(String display) {
        //         for (RuleType ruleType : RuleType.values()) {
        //             if (ruleType.getDisplayName().equals(display)) {
        //                 return ruleType;
        //             }
        //         }
        //         return null;
        //     }
        // });
    }

    private void initTableView() {
        List<IndexStat> allIndexStat = anytxtToolService.getAllIndexStat();
        List<IndexStatVO> allIndexStatVO = allIndexStat.stream().map(IndexStatVO::new).collect(Collectors.toList());
        indexStatTableView.setItems(FXCollections.observableArrayList(allIndexStatVO));

        checkTableCol.setCellValueFactory(new PropertyValueFactory<>("selected"));
        checkTableCol.setCellFactory(CheckBoxTableCell.forTableColumn(checkTableCol));

        orderTableCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(allIndexStatVO.indexOf(param.getValue()) + 1));

        extTableCol.setCellValueFactory(new PropertyValueFactory<>("ext"));
        extTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
        extTableCol.setEditable(true);

        statTableCol.setCellValueFactory(param -> {
            String statDisplayName = EntityStat.find(stat -> stat.getCoord() == param.getValue().getStat()).map(EntityStat::getDisplayName).orElse("");
            return new ReadOnlyObjectWrapper<>(statDisplayName);
        });
        statTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
        statTableCol.setEditable(true);

        totalTableCol.setCellValueFactory(new PropertyValueFactory<>("total"));
        totalTableCol.setEditable(false);

        ruleTableCol.setCellValueFactory(param -> {
            RuleFtlDataModel dataModel = Unchecked.supplier(() -> anytxtToolService.parseRuleXml(param.getValue().getRule())).get();
            String ruleDisplayName = RuleType.find(type -> type.getCoord() == dataModel.getRuleType()).map(RuleType::getDisplayName).orElse("");
            return new ReadOnlyObjectWrapper<>(ruleDisplayName);
        });
        ruleTableCol.setEditable(true);
        ruleTableCol.setCellFactory(TextFieldTableCell.forTableColumn());

        optTableCol.setCellFactory(param -> new TableCell<IndexStatVO, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                Button modBtn = new Button("修改");
                modBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    System.out.println("modBtn");
                });
                Button showDescBtn = new Button("查看规则目录列表");
                showDescBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    System.out.println("showDescBtn");
                });
                HBox hBox = new HBox(modBtn, showDescBtn);
                hBox.setSpacing(10);
                hBox.setAlignment(Pos.CENTER);
                this.setGraphic(hBox);
            }
        });
    }
}
