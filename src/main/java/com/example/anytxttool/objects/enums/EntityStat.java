package com.example.anytxttool.objects.enums;

/**
 * Created by AMe on 2020-08-02 15:23.
 */
public enum EntityStat {
    NEW(1, "NEW", "新加"),
    HANDLING(2, "HANDLING", "处理中"),
    WAITING(3, "WAITING", "等待中"),
    COMPLETED(4, "COMPLETED", "完成"),
    DELETED(5, "DELETED", "移除");

    private int coordinate;
    private String name;
    private String displayName;

    EntityStat(int coordinate, String name, String displayName) {
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
