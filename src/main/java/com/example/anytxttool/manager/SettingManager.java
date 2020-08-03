package com.example.anytxttool.manager;

import com.example.anytxttool.entity.Setting;
import com.example.devutils.utils.jdbc.JdbcUtils;
import java.sql.Types;
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

    public int updateByRowId(int rowId, String value) {
        String sql = String.format("update %s set value = ? where rowid = %s", Setting.TABLE, rowId);
        return jdbcUtils.update(sql, new Object[]{value}, new int[]{Types.VARCHAR});
    }

    public int updateByKey(String key, String value) {
        String sql = String.format("update %s set value = ? where key = ?", Setting.TABLE);
        return jdbcUtils.update(sql, new Object[]{Objects.requireNonNull(key), value}, new int[]{Types.VARCHAR, Types.VARCHAR});
    }

    public Optional<Setting> getByRowId(int rowId) {
        String sql = String.format("select rowid, key, value from %s where rowid = %s", Setting.TABLE, rowId);
        return jdbcUtils.selectRow(sql, Setting.class);
    }

    public List<Setting> getAll() {
        String sql = String.format("select rowid, key, value from %s", Setting.TABLE);
        return jdbcUtils.selectRows(sql, Setting.class);
    }
}
