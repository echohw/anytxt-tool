package com.example.anytxttool;

import com.example.anytxttool.entity.IndexStat;
import com.example.anytxttool.entity.Setting;
import com.example.anytxttool.manager.IndexStatManager;
import com.example.anytxttool.manager.SettingManager;
import com.example.anytxttool.manager.ToolConfigManager;
import com.example.anytxttool.objects.RuleFtlDataModel;
import com.example.anytxttool.objects.enums.EntityStat;
import com.example.anytxttool.objects.enums.RuleType;
import com.example.anytxttool.service.AnytxtToolService;
import com.example.devutils.dep.Charsets;
import com.example.devutils.utils.io.StreamUtils;
import com.example.devutils.utils.text.RegexUtils;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.dom4j.DocumentException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AnytxtToolApplicationTests {

    private Logger logger = LoggerFactory.getLogger(AnytxtToolApplicationTests.class);

    private List<String> ignoredDirList = new LinkedList<>();
    private List<String> scannedDirList = new LinkedList<>();

    @Autowired
    private SettingManager settingManager;
    @Autowired
    private IndexStatManager indexStatManager;
    @Autowired
    private ToolConfigManager toolConfigManager;
    @Autowired
    private AnytxtToolService anytxtToolService;

    @Test
    public void testGetAllSetting() {
        List<Setting> settingList = settingManager.getAll();
        settingList.forEach(System.out::println);
    }

    @Test
    public void testGetAllIndexStat() {
        List<IndexStat> indexStatList = indexStatManager.getAll();
        indexStatList.forEach(indexStat -> {
            indexStat.setRule(null);
            System.out.println(indexStat);
        });
    }

    @Test
    public void testDelAllIndexStat() {
        indexStatManager.deleteAll(true);
    }

    @Test
    public void testRenderRuleFtl() throws IOException, TemplateException {
        RuleFtlDataModel dataModel = new RuleFtlDataModel();
        dataModel.setVersion(0);
        dataModel.setCount(1001);
        dataModel.setRuleType(RuleType.EXCLUDE_DIR);
        dataModel.setDirList(Collections.emptyList());
        String ruleFtl = anytxtToolService.renderRuleFtl(dataModel);
        System.out.println(ruleFtl);
    }

    @Test
    public void testResetIndexStat() throws IOException, TemplateException {
        Consumer<Path> scannedDirConsumer = path -> {
            scannedDirList.add(path.toString());
            logger.info("已扫描的目录: {}", path.toString());
        };
        BiConsumer<Path, Object> ignoredDirConsumer = (path, msg) -> {
            ignoredDirList.add(path.toString());
            logger.info("已忽略的目录: {}", path.toString());
        };
        // 扫描
        anytxtToolService.startScan(
            toolConfigManager.getScanPathList().stream().map(Paths::get).collect(Collectors.toList()),
            dirPath -> {
                for (String ignoreDirRegex : toolConfigManager.getIgnoreDirRegexList()) {
                    if (RegexUtils.match(dirPath.toString(), ignoreDirRegex, true)) {
                        ignoredDirConsumer.accept(dirPath, "匹配的忽略目录");
                        return false;
                    }
                }
                scannedDirConsumer.accept(dirPath);
                return true;
            },
            filePath -> {},
            (path, ex) -> {
                // logger.error("{}", ex);
                ignoredDirConsumer.accept(path, new RuntimeException("目录访问受限", ex));
                return FileVisitResult.SKIP_SUBTREE;
            }
        );
        // 重置
        indexStatManager.deleteAll(true);
        RuleType ruleType = RuleType.EXCLUDE_DIR;
        String ruleFtl = anytxtToolService.renderRuleFtl(new RuleFtlDataModel(ruleType, ignoredDirList.size(), 0, ignoredDirList));
        List<IndexStat> indexStatList = toolConfigManager.getFileTypeList().stream().map(fileType -> anytxtToolService.assembleIndexStat(fileType, EntityStat.NEW.getCoord(), 0, ruleFtl)).collect(Collectors.toList());
        anytxtToolService.addAllIndexStat(indexStatList);
    }

    @Test
    public void testParseRuleXml() throws IOException, DocumentException {
        ClassPathResource classPathResource = new ClassPathResource("ruleXml.xml");
        String ruleXml = StreamUtils.readAsString(classPathResource.getInputStream(), Charsets.UTF_8);
        RuleFtlDataModel dataModel = anytxtToolService.parseRuleXml(ruleXml);
        System.out.println(dataModel);
        dataModel.getDirList().forEach(System.out::println);
    }
}
