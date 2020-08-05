package com.example.anytxttool.manager;

import com.example.devutils.utils.collection.CollectionUtils;
import com.example.devutils.utils.io.PathUtils;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
        ".lua", ".json", ".xml", ".yml", ".properties", ".log", ".md", ".ini", ".fxml"
    );
    private List<String> scanPathList = Arrays.stream(File.listRoots()).map(File::getAbsolutePath).collect(Collectors.toList());
    private List<String> ignoreDirRegexList = Arrays.asList(
        getDesktopDirRegex(),
        ".*(?:/|\\\\)\\.idea$",
        ".*(?:/|\\\\)\\.gradle$",
        ".*(?:/|\\\\)\\.git$",
        ".*(?:/|\\\\)\\.github$",
        ".*(?:/|\\\\)\\.svn$",
        ".*(?:/|\\\\)node_modules$"
    );

    public String getDesktopDirRegex() {
        Path desktopPath = PathUtils.getDesktopPath();
        ArrayList<Path> desktopPathList = CollectionUtils.flat(ArrayList::new, PathUtils.getAllParentPath(desktopPath), Collections.singletonList(desktopPath));
        return "^(?:(?!" + desktopPathList.stream().map(path -> "(?:" + path.toString().replace("\\", "\\\\") + "$)").collect(Collectors.joining("|")) + ").*)$";
    }

    public List<String> getFileTypeList() {
        return Stream.of(defaultFileTypeList, extFileTypeList).flatMap(List::stream).distinct().collect(Collectors.toList());
    }

    public List<String> getScanPathList() {
        return scanPathList;
    }

    public List<String> getIgnoreDirRegexList() {
        return ignoreDirRegexList;
    }

}
