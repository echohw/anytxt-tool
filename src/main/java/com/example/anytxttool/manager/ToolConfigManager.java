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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by AMe on 2020-08-02 18:41.
 */
@Component
public class ToolConfigManager {

    private static final Logger logger = LoggerFactory.getLogger(ToolConfigManager.class);

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
        ".*(?:/|\\\\)\\.idea$",
        ".*(?:/|\\\\)\\.gradle$",
        ".*(?:/|\\\\)\\.git$",
        ".*(?:/|\\\\)\\.github$",
        ".*(?:/|\\\\)\\.svn$",
        ".*(?:/|\\\\)node_modules$",
        getDisIgnorePathRegex() // 必须放在最后
    );

    public String getDisIgnorePathRegex() {
        Path desktopPath = PathUtils.getDesktopPath();
        Path systemDrive = PathUtils.getSystemDrive();
        ArrayList<String> desktopPathList = CollectionUtils.flat(ArrayList::new,
            PathUtils.getAllParentPath(desktopPath).stream().map(Path::toString).collect(Collectors.toList()),
            Collections.singletonList(desktopPath.toString() + ".*")
        );
        List<String> otherRootDrivePathList = Arrays.stream(File.listRoots()).map(File::toPath).filter(path -> !path.equals(systemDrive)).map(path -> path.toString() + ".*").collect(Collectors.toList());
        ArrayList<String> disIgnorePathRegexList = CollectionUtils.flat(ArrayList::new, desktopPathList, otherRootDrivePathList); // 不忽略桌面路径及其他磁盘路径
        // ^(?:(?!(?:C:\\$)|(?:C:\\Users$)|(?:C:\\Users\\Administrator$)|(?:C:\\Users\\Administrator\\Desktop$)).*)$
        String regex = "^(?:(?!" + disIgnorePathRegexList.stream().map(path -> "(?:" + path.replace("\\", "\\\\") + "$)").collect(Collectors.joining("|")) + ").*)$";
        logger.info("不忽略路径的正则: {}", regex);
        return regex;
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
