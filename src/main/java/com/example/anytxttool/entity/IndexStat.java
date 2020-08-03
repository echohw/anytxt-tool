package com.example.anytxttool.entity;

import lombok.Data;

/**
 * Created by AMe on 2020-08-02 01:03.
 */
@Data
public class IndexStat {

    public static final String TABLE = "IndexStat";

    private Integer rowId;
    private Integer id;
    private String ext;
    private Integer stat;
    private Integer total;
    private String rule;
}
