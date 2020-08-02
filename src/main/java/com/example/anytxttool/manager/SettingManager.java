package com.example.anytxttool.manager;

import com.example.anytxttool.entity.Setting;
import com.example.devutils.utils.jdbc.JdbcUtils;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by AMe on 2020-08-02 01:20.
 */
@Component
public class SettingManager {

    @Autowired
    private JdbcUtils jdbcUtils;

    public int updateByRowId(long rowId, String value) {
        String sql = String.format("update %s set value = %s where rowid = %s", Setting.TABLE, value, rowId);
        return jdbcUtils.update(sql);
    }

    public int updateByKey(String key, String value) {
        String sql = String.format("update %s set value = %s where key = %s", Setting.TABLE, value, Objects.requireNonNull(key));
        return jdbcUtils.update(sql);
    }

    public Optional<Setting> getByRowId(long rowId) {
        String sql = String.format("select rowid, key, value from %s where rowid = %s", Setting.TABLE, rowId);
        return jdbcUtils.selectRow(sql, Setting.class);
    }

    public List<Setting> getAll() {
        String sql = String.format("select rowid, key, value from %s", Setting.TABLE);
        return jdbcUtils.selectRows(sql, Setting.class);
    }
}
