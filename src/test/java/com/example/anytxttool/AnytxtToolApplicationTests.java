package com.example.anytxttool;

import com.example.anytxttool.entity.IndexStat;
import com.example.anytxttool.manager.IndexStatManager;
import com.example.anytxttool.service.AnytxtToolService;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AnytxtToolApplicationTests {

    private Logger logger = LoggerFactory.getLogger(AnytxtToolApplicationTests.class);

    @Autowired
    private IndexStatManager indexStatManager;
    @Autowired
    private AnytxtToolService anytxtToolService;

    @Test
    public void testDelAllIndex() {
        indexStatManager.deleteAll(true);
    }

    @Test
    @Rollback(false)
    public void test001() throws IOException, TemplateException {
        anytxtToolService.startScan();
    }

    @Test
    public void test002() {
        List<IndexStat> all = indexStatManager.getAll();
        String collect = all.stream().map(stat -> "\"" + stat.getExt() + "\"").collect(Collectors.joining(","));
        // ,
        System.out.println(collect);
    }

}
