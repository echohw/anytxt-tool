package com.example.anytxttool.manager;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

/**
 * Created by AMe on 2020-08-02 18:41.
 */
@Component
public class ToolConfigManager {

    private List<String> defaultFileTypeList = Arrays.asList(
        ".pdf", ".doc", ".ppt", ".xls", ".docx", ".pptx", ".xlsx", ".chm", ".mobi", ".epub", ".fb2",
        ".tcr", ".txt"
    );
    private List<String> extFileTypeList = Arrays.asList(
        ".java", ".py", ".php", ".sql", ".scala", ".go", ".html", ".js", ".css", ".vue", ".sh",
        ".lua", ".vue", ".json", ".xml", ".yml", ".log", ".md", ".ini"
    );
    private List<String> scanDirList = Arrays.stream(File.listRoots()).map(File::getAbsolutePath).collect(Collectors.toList());
    private List<String> ignoreDirRegexList = Arrays.asList(
        "^C:",
        ".*(?:/|\\\\)\\.idea$",
        ".*(?:/|\\\\)\\.gradle$",
        ".*(?:/|\\\\)\\.git$",
        ".*(?:/|\\\\)\\.github$",
        ".*(?:/|\\\\)\\.svn$",
        ".*(?:/|\\\\)node_modules$"
    );

    public List<String> getFileTypeList() {
        return Stream.of(defaultFileTypeList, extFileTypeList).flatMap(List::stream).distinct().collect(Collectors.toList());
    }

    public List<String> getScanDirList() {
        return scanDirList;
    }

    public List<String> getIgnoreDirRegexList() {
        return ignoreDirRegexList;
    }

}
