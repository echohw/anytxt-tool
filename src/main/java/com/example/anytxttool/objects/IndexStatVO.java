package com.example.anytxttool.objects;

import com.example.anytxttool.entity.IndexStat;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by AMe on 2020-08-02 01:03.
 */
public class IndexStatVO {

    private IntegerProperty id;
    private StringProperty ext;
    private IntegerProperty stat;
    private IntegerProperty total;
    private StringProperty rule;
    private BooleanProperty selected;

    public IndexStatVO(IndexStat indexStat) {
        id = new SimpleIntegerProperty(indexStat.getId());
        ext = new SimpleStringProperty(indexStat.getExt());
        stat = new SimpleIntegerProperty(indexStat.getStat());
        total = new SimpleIntegerProperty(indexStat.getTotal());
        rule = new SimpleStringProperty(indexStat.getRule());
        selected = new SimpleBooleanProperty(false);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getExt() {
        return ext.get();
    }

    public StringProperty extProperty() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext.set(ext);
    }

    public int getStat() {
        return stat.get();
    }

    public IntegerProperty statProperty() {
        return stat;
    }

    public void setStat(int stat) {
        this.stat.set(stat);
    }

    public int getTotal() {
        return total.get();
    }

    public IntegerProperty totalProperty() {
        return total;
    }

    public void setTotal(int total) {
        this.total.set(total);
    }

    public String getRule() {
        return rule.get();
    }

    public StringProperty ruleProperty() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule.set(rule);
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
