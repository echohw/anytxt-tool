package com.example.anytxttool.service;

import com.example.anytxttool.entity.IndexStat;
import com.example.anytxttool.manager.IndexStatManager;
import com.example.anytxttool.objects.RuleFtlDataModel;
import com.example.anytxttool.objects.enums.RuleType;
import com.example.devutils.dep.Charsets;
import com.example.devutils.dep.MyFileVisitor;
import com.example.devutils.utils.io.DirectoryUtils;
import com.example.devutils.utils.io.PathUtils;
import com.example.devutils.utils.text.StringUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jooq.lambda.Unchecked;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by AMe on 2020-08-02 20:17.
 */
@Service
public class AnytxtToolService {

    private static final String FTL_RULE_NAME = "index_stat.rule.ftl";

    @Autowired
    private IndexStatManager indexStatManager;
    @Autowired
    private Configuration configuration;

    public void startScan(List<Path> scanDirList, Predicate<Path> scanPredicate, Consumer<Path> fileConsumer, BiFunction<Path, IOException, FileVisitResult> failedHandler) {
        scanDirList.stream().filter(PathUtils::exists).forEach(path -> {
            Unchecked.runnable(() -> DirectoryUtils.walkFileTree(path, new MyFileVisitor<>(scanPredicate, fileConsumer, failedHandler))).run();
        });
    }

    public void deleteAllIndexStat() {
        indexStatManager.deleteAll(true);
    }

    public void deleteIndexStatByIdIn(List<Integer> idList) {
        idList.forEach(id -> indexStatManager.deleteById(id, true));
    }

    public void addAllIndexStat(List<IndexStat> indexStatList) {
        indexStatManager.addAll(indexStatList);
    }

    public List<IndexStat> getAllIndexStat() {
        return indexStatManager.getAll();
    }

    public IndexStat assembleIndexStat(String ext, int stat, int total, String rule) {
        IndexStat indexStat = new IndexStat();
        indexStat.setExt(ext);
        indexStat.setStat(stat);
        indexStat.setTotal(total);
        indexStat.setRule(rule);
        return indexStat;
    }

    public String renderRuleFtl(RuleFtlDataModel dataModel)throws IOException, TemplateException {
        Template template = configuration.getTemplate(FTL_RULE_NAME, Charsets.UTF_8.name());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        template.process(dataModel, new OutputStreamWriter(byteArrayOutputStream, Charsets.UTF_8));
        return new String(byteArrayOutputStream.toByteArray(), Charsets.UTF_8);
    }

    public RuleFtlDataModel parseRuleXml(String ruleXml) throws DocumentException {
        RuleFtlDataModel dataModel = new RuleFtlDataModel();
        // 解析XML
        ruleXml = ruleXml + "</boost_serialization>"; // 标签未闭合补齐
        Document document = DocumentHelper.parseText(ruleXml);
        Element dataObjElem = (Element) document.selectSingleNode("//dataObj");
        Element ruleTypeElem = dataObjElem.element("m_ruleType");
        Optional.ofNullable(ruleTypeElem.getTextTrim()).filter(StringUtils::isNotBlank).map(Integer::valueOf).ifPresent(type -> {
            dataModel.setRuleType(RuleType.find(ruleType -> ruleType.getCoord() == type).orElse(RuleType.ALL).getCoord());
        });
        Element qslPathsElem = dataObjElem.element("m_qslPaths");
        Element countElem = (Element) qslPathsElem.selectSingleNode("stdList/count");
        Optional.ofNullable(countElem.getTextTrim()).filter(StringUtils::isNotBlank).map(Integer::valueOf).ifPresent(dataModel::setCount);
        Element versionElem = (Element) qslPathsElem.selectSingleNode("stdList/item_version");
        Optional.ofNullable(versionElem.getTextTrim()).filter(StringUtils::isNotBlank).map(Integer::valueOf).ifPresent(dataModel::setVersion);
        List<Element> itemElems = qslPathsElem.selectNodes("stdList/item");
        List<String> pathList = itemElems.stream()
            .map(itemElem -> Optional.ofNullable(itemElem.element("stdString").getTextTrim()).filter(StringUtils::isNotBlank).orElse(""))
            .filter(StringUtils::isNotBlank).collect(Collectors.toList());
        dataModel.setDirList(pathList);
        return dataModel;
    }

}
