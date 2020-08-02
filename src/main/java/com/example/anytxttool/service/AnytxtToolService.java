package com.example.anytxttool.service;

import com.example.anytxttool.entity.IndexStat;
import com.example.anytxttool.manager.IndexStatManager;
import com.example.anytxttool.manager.ToolConfigManager;
import com.example.anytxttool.objects.enums.EntityStat;
import com.example.anytxttool.objects.RuleFtlDataModel;
import com.example.anytxttool.objects.enums.RuleType;
import com.example.devutils.dep.Charsets;
import com.example.devutils.dep.MyFileVisitor;
import com.example.devutils.utils.io.DirectoryUtils;
import com.example.devutils.utils.io.PathUtils;
import com.example.devutils.utils.text.RegexUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.jooq.lambda.Unchecked;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by AMe on 2020-08-02 20:17.
 */
@Service
public class AnytxtToolService {

    @Autowired
    private ToolConfigManager toolConfigManager;
    @Autowired
    private IndexStatManager indexStatManager;
    @Autowired
    private Configuration configuration;

    private List<String> dirList = new ArrayList<>(
        Arrays.asList("C:\\")
    );

    private Consumer<Path> scannedDirConsumer = path -> {};
    private BiConsumer<Path, Object> ignoredDirConsumer = (path, msg) -> {
        dirList.add(path.toString());
        System.out.println(path);
    };
    private Consumer<Path> fileConsumer = filePath -> {};
    private Predicate<Path> scanPredicate = dirPath -> {
        for (String ignoreDirRegex : toolConfigManager.getIgnoreDirRegexList()) {
            if (RegexUtils.match(dirPath.toString(), ignoreDirRegex, true)) {
                ignoredDirConsumer.accept(dirPath, "忽略目录");
                return false;
            }
        }
        scannedDirConsumer.accept(dirPath);
        return true;
    };
    private BiFunction<Path, IOException, FileVisitResult> failedHandler = (path, ex) -> {
        ignoredDirConsumer.accept(path, "目录访问受限");
        return FileVisitResult.SKIP_SUBTREE;
    };

    public void startScan() throws IOException, TemplateException {
        toolConfigManager.getScanDirList().stream().map(Paths::get).filter(PathUtils::exists).forEach(path -> {
            Unchecked.runnable(() -> DirectoryUtils.walkFileTree(path, new MyFileVisitor<>(scanPredicate, fileConsumer, failedHandler))).run();
        });
        RuleFtlDataModel dataModel = new RuleFtlDataModel();
        dataModel.setRuleType(RuleType.EXCLUDE_DIR.getCoordinate());
        dataModel.setCount(dirList.size());
        dataModel.setVersion(0);
        dataModel.setDirList(dirList);
        String ruleFtl = renderRuleFtl("index_stat.rule.ftl", dataModel);
        toolConfigManager.getFileTypeList().forEach(fileType -> {
            IndexStat indexStat = new IndexStat();
            indexStat.setExt(fileType);
            indexStat.setStat(EntityStat.NEW.getCoordinate());
            indexStat.setTotal(0);
            indexStat.setRule(ruleFtl);
            indexStatManager.add(indexStat);
        });
    }

    public String renderRuleFtl(String ftlName, RuleFtlDataModel dataModel)throws IOException, TemplateException {
        Template template = configuration.getTemplate(ftlName, Charsets.UTF_8.name());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        template.process(dataModel, new OutputStreamWriter(byteArrayOutputStream, Charsets.UTF_8));
        return new String(byteArrayOutputStream.toByteArray(), Charsets.UTF_8);
    }

}
