package com.example.anytxttool.objects.enums;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Created by AMe on 2020-08-02 15:23.
 */
public enum EntityStat {
    NEW(1, "NEW", "新加"),
    HANDLING(2, "HANDLING", "处理中"),
    WAITING(3, "WAITING", "等待中"),
    COMPLETED(4, "COMPLETED", "完成"),
    DELETED(5, "DELETED", "移除");

    private int coord;
    private String name;
    private String displayName;

    EntityStat(int coord, String name, String displayName) {
        this.coord = coord;
        this.name = name;
        this.displayName = displayName;
    }

    public int getCoord() {
        return this.coord;
    }

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static Optional<EntityStat> find(Predicate<EntityStat> entityStatPredicate) {
        for (EntityStat entityStat : EntityStat.values()) {
            if (entityStatPredicate.test(entityStat)) {
                return Optional.of(entityStat);
            }
        }
        return Optional.empty();
    }

    public static Optional<EntityStat> findByCoord(int coord) {
        return find(stat -> stat.getCoord() == coord);
    }

    public static Optional<EntityStat> findByName(String name) {
        return find(stat -> stat.getName().equals(name));
    }
}
