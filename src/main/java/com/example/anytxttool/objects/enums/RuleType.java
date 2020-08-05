package com.example.anytxttool.objects.enums;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Created by AMe on 2020-08-02 12:54.
 */
public enum RuleType {
    ALL(0, "ALL", "所有文件将被包含"),
    ONLY_DIR(1, "ONLY_DIR", "仅包含指定的目录"),
    EXCLUDE_DIR(2, "EXCLUDE_DIR", "仅排除指定的目录");

    private int coord;
    private String name;
    private String displayName;

    RuleType(int coord, String name, String displayName) {
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

    public static Optional<RuleType> find(Predicate<RuleType> ruleTypePredicate) {
        for (RuleType ruleType : RuleType.values()) {
            if (ruleTypePredicate.test(ruleType)) {
                return Optional.of(ruleType);
            }
        }
        return Optional.empty();
    }
}
