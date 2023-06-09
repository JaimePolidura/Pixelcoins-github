package es.serversurvival._shared.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultsetObjetBuilder<T> {
    T build(ResultSet rs) throws SQLException;
}
