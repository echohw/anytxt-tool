package com.example.anytxttool.objects;

import com.example.anytxttool.entity.IndexStat;
import com.example.anytxttool.objects.enums.EntityStat;
import com.example.anytxttool.objects.enums.RuleType;
import java.util.Objects;
import java.util.function.Function;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Data;

/**
 * Created by AMe on 2020-08-02 01:03.
 */
@Data
public class IndexStatVO {

    private IndexStat indexStat;
    private RuleFtlDataModel ruleFtlDataModel;
    private Function<String, RuleFtlDataModel> xmlParser;
    private Function<RuleFtlDataModel, String> dataModelRenderer;

    private IntegerProperty id;
    private StringProperty ext;
    private ObjectProperty<EntityStat> stat;
    private IntegerProperty total;
    private ObjectProperty<RuleType> rule;
    private BooleanProperty selected;

    public IndexStatVO(IndexStat indexStat, Function<String, RuleFtlDataModel> xmlParser, Function<RuleFtlDataModel, String> dataModelRenderer) {
        this.indexStat = Objects.requireNonNull(indexStat);
        this.xmlParser = Objects.requireNonNull(xmlParser);
        this.ruleFtlDataModel = Objects.requireNonNull(xmlParser.apply(indexStat.getRule()));
        this.dataModelRenderer = Objects.requireNonNull(dataModelRenderer);

        id = new SimpleIntegerProperty(indexStat.getId());
        ext = new SimpleStringProperty(indexStat.getExt());
        stat = new SimpleObjectProperty<>(EntityStat.find(stat -> stat.getCoord() == indexStat.getStat()).orElse(EntityStat.NEW));
        total = new SimpleIntegerProperty(indexStat.getTotal());
        rule = new SimpleObjectProperty<>(ruleFtlDataModel.getRuleType());
        selected = new SimpleBooleanProperty(false);
    }

    public IndexStat getIndexStat() {
        return indexStat;
    }

    public RuleFtlDataModel getRuleFtlDataModel() {
        return ruleFtlDataModel;
    }

    // ========================================

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
        this.indexStat.setId(id);
    }

    public String getExt() {
        return ext.get();
    }

    public StringProperty extProperty() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext.set(ext);
        this.indexStat.setExt(ext);
    }

    public EntityStat getStat() {
        return stat.get();
    }

    public ObjectProperty<EntityStat> statProperty() {
        return stat;
    }

    public void setStat(EntityStat stat) {
        this.stat.set(stat);
        this.indexStat.setStat(stat.getCoord());
    }

    public int getTotal() {
        return total.get();
    }

    public IntegerProperty totalProperty() {
        return total;
    }

    public void setTotal(int total) {
        this.total.set(total);
        this.indexStat.setTotal(total);
    }

    public RuleType getRule() {
        return rule.get();
    }

    public ObjectProperty<RuleType> ruleProperty() {
        return rule;
    }

    public void setRule(RuleType rule) {
        this.rule.set(rule);
        this.ruleFtlDataModel.setRuleType(rule);
    }

    public boolean isSelected() {
        return selected.get();
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }
}
