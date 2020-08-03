package com.example.anytxttool.entity;

import lombok.Data;

/**
 * Created by AMe on 2020-08-02 01:03.
 */
@Data
public class Setting {

    public static final String TABLE = "Setting";

    private Integer rowId;
    private String key;
    private String value;
}
