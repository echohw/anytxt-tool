package com.example.anytxttool.objects;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by AMe on 2020-08-02 19:56.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RuleFtlDataModel {

    private Integer ruleType;
    private Integer count;
    private Integer version;
    private List<String> dirList;

}
