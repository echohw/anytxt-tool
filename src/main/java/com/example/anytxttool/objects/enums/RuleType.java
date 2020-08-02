package com.example.anytxttool.objects.enums;

/**
 * Created by AMe on 2020-08-02 12:54.
 */
public enum RuleType {
    ALL(0, "ALL", "所有文件将被包含"),
    ONLY_DIR(1, "ONLY_DIR", "仅包含指定的目录"),
    EXCLUDE_DIR(2, "EXCLUDE_DIR", "仅排除指定的目录");

    private int coordinate;
    private String name;
    private String displayName;

    RuleType(int coordinate, String name, String displayName) {
        this.coordinate = coordinate;
        this.name = name;
        this.displayName = displayName;
    }

    public int getCoordinate() {
        return this.coordinate;
    }

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
