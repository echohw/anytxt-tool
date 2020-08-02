package com.example.anytxttool.config;

import com.example.devutils.utils.jdbc.JdbcUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by AMe on 2020-08-02 01:04.
 */
@Configuration
public class AnytxtToolConfig {

    @Bean
    public JdbcUtils jdbcUtils(JdbcTemplate jdbcTemplate) {
        return new JdbcUtils(jdbcTemplate);
    }

}
