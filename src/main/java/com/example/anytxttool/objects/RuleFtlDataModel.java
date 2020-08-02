package com.example.anytxttool.objects;

import java.util.List;
import lombok.Data;

/**
 * Created by AMe on 2020-08-02 19:56.
 */
@Data
public class RuleFtlDataModel {

    private Integer ruleType;
    private Integer count;
    private Integer version;
    private List<String> dirList;

}
