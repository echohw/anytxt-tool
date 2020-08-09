package com.example.anytxttool.controller;

import com.example.anytxttool.entity.IndexStat;
import com.example.anytxttool.objects.IndexStatVO;
import com.example.anytxttool.service.AnytxtToolService;
import com.example.anytxttool.view.MainView;
import com.example.devutils.utils.ThreadPoolUtils;
import com.example.devutils.utils.collection.CollectionUtils;
import com.example.devutils.utils.text.StringUtils;
import de.felixroske.jfxsupport.FXMLController;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.converter.IntegerStringConverter;
import org.jooq.lambda.Unchecked;
import org.jooq.lambda.tuple.Tuple2;
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

    private Tuple2<List<IndexStat>, List<IndexStatVO>> indexStatTuple2;

    @Autowired
    private MainView mainView;
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
    private HBox optHBox;
    @FXML
    private TextField filterListTextField;
    @FXML
    private Button newItemBtn;
    @FXML
    private Button delItemsBtn;
    @FXML
    private Button refreshListBtn;

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

    private void batchChoice(boolean checkAll) {
        batchChoiceCheckBox.setSelected(checkAll);
        indexStatTableView.getItems().forEach(indexStatVO -> indexStatVO.setSelected(checkAll));
    }

    @FXML
    public void batchChoice(ActionEvent event) {
        batchChoice(indexStatTableView.getItems().stream().anyMatch(indexStatVO -> !indexStatVO.isSelected()));
    }

    private List<IndexStatVO> filterList(String keyword, List<IndexStatVO> indexStatVOList) {
        return indexStatVOList.stream().filter(indexStatVO -> indexStatVO.getExt().contains(keyword)).collect(Collectors.toList());
    }

    @FXML
    public void filterList(KeyEvent event) {
        String keyword = filterListTextField.getCharacters().toString();
        List<IndexStatVO> filteredItems = filterList(keyword, indexStatTuple2.v2());
        indexStatTableView.setItems(FXCollections.observableArrayList(filteredItems));
        batchChoiceCheckBox.setSelected(false);
        indexStatTuple2.v2().forEach(indexStatVO -> indexStatVO.setSelected(false));
    }

    @FXML
    public void newItem() {
        // IndexStatVO indexStatVO = new IndexStatVO(new IndexStat(), );
        // indexStatTableView.getItems().add(indexStatVO);
    }

    @FXML
    public void delItems() {
        List<IndexStatVO> indexStatVOList = indexStatTableView.getItems().stream().filter(IndexStatVO::isSelected).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(indexStatVOList)) {
            Alert alert = new Alert(AlertType.INFORMATION, "请选择要删除的项");
            alert.show();
            return;
        }
        ButtonType okBtn = new ButtonType("确定", ButtonData.YES);
        ButtonType cancelBtn = new ButtonType("取消", ButtonData.NO);
        Alert alert = new Alert(Alert.AlertType.WARNING, "", okBtn, cancelBtn);
        alert.setHeaderText("您正在执行删除操作！");

        String delItemInfo = indexStatVOList.stream().map(IndexStatVO::getExt).limit(5).collect(Collectors.joining("、"));
        alert.setContentText(String.format("即将删除%s等文件类型的%s项数据，是否继续？", delItemInfo, indexStatVOList.size()));
        Optional<ButtonType> buttonType = alert.showAndWait();
        buttonType.ifPresent(btnType -> {
            if (btnType.equals(okBtn)) {
                anytxtToolService.deleteAllIndexStatByIdIn(indexStatVOList.stream().map(IndexStatVO::getId).collect(Collectors.toList()));
                this.refreshList();
            }
        });
    }

    @FXML
    public void refreshList() {
        refreshListBtn.setDisable(true);
        ThreadPoolUtils.submit(() -> {
            try {
                indexStatTableView.setItems(FXCollections.observableArrayList(loadTableViewData().v2()));
                batchChoiceCheckBox.setSelected(false);
            } finally {
                refreshListBtn.setDisable(false);
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.location = location;
        this.resources = resources;

        this.initTableView(loadTableViewData().v2());
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

    private Tuple2<List<IndexStat>, List<IndexStatVO>> loadTableViewData() {
        List<IndexStat> allIndexStat = anytxtToolService.getAllIndexStat();
        List<IndexStatVO> allIndexStatVO = allIndexStat.parallelStream()
            .map(indexStat -> new IndexStatVO(
                indexStat,
                Unchecked.function(ruleXml -> anytxtToolService.parseRuleXml(ruleXml)),
                Unchecked.function(dataModel -> anytxtToolService.renderRuleFtl(dataModel))
            ))
            .sorted(Comparator.comparingInt(IndexStatVO::getId)).collect(Collectors.toList());
        indexStatTuple2 = new Tuple2<>(allIndexStat, allIndexStatVO);
        return indexStatTuple2;
    }

    private void initTableView(List<IndexStatVO> allIndexStatVO) {
        indexStatTableView.setItems(FXCollections.observableArrayList(allIndexStatVO));

        checkTableCol.setCellFactory(CheckBoxTableCell.forTableColumn(checkTableCol));
        checkTableCol.setCellValueFactory(new PropertyValueFactory<>("selected"));
        checkTableCol.setEditable(true);

        orderTableCol.setCellFactory(param -> new TableCell<IndexStatVO, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                this.setText(null);
                this.setGraphic(null);
                if (!empty) { // 非空行才向单元格中填充内容
                    int rowIndex = this.getIndex();
                    this.setText(String.valueOf(rowIndex + 1));
                }
            }
        });
        orderTableCol.setEditable(false);

        extTableCol.setCellFactory(param -> new TextFieldTableCell<IndexStatVO, String>(new DefaultStringConverter()) {
            @Override
            public void commitEdit(String newExt) {
                String errorMsg = null;
                if (StringUtils.isBlank(newExt) || !newExt.startsWith(".")) {
                    errorMsg = "错误的文件类型";
                }
                if (StringUtils.isBlank(errorMsg)) {
                    IndexStatVO rowValue = indexStatTableView.getItems().get(indexStatTableView.getEditingCell().getRow());
                    boolean match = indexStatTableView.getItems().stream().anyMatch(indexStatVO -> indexStatVO.getExt().equals(newExt) && indexStatVO.getId() != rowValue.getId());
                    if (match) {
                        errorMsg = "文件类型已存在";
                    }
                }
                if (StringUtils.isNotBlank(errorMsg)) {
                    new Alert(AlertType.ERROR, errorMsg).showAndWait();
                    super.cancelEdit();
                    return;
                }
                super.commitEdit(newExt);
            }
        });
        extTableCol.setCellValueFactory(new PropertyValueFactory<>("ext"));
        extTableCol.setEditable(false);
        extTableCol.setOnEditCommit(event -> {
            event.getRowValue().setExt(event.getNewValue());
        });

        statTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
        statTableCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getStat().getDisplayName()));
        statTableCol.setEditable(false);

        totalTableCol.setCellFactory(param -> new TextFieldTableCell<>(new IntegerStringConverter()));
        totalTableCol.setCellValueFactory(new PropertyValueFactory<>("total"));
        totalTableCol.setEditable(false);

        ruleTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
        ruleTableCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getRule().getDisplayName()));
        ruleTableCol.setEditable(false);

        optTableCol.setCellFactory(param -> new TableCell<IndexStatVO, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                this.setText(null);
                this.setGraphic(null);
                if (!empty) {
                    Button uptBtn = new Button("修改");
                    uptBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                        System.out.println("uptBtn");
                    });

                    Button showDescBtn = new Button("查看详情");
                    showDescBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                        System.out.println("showDescBtn");
                    });
                    HBox hBox = new HBox(uptBtn, showDescBtn);
                    hBox.setSpacing(10);
                    hBox.setAlignment(Pos.CENTER);
                    this.setGraphic(hBox);
                }
            }
        });
    }
}
