package tech.zeta.utils.DB;

import java.sql.Connection;

public interface DBConnection {
  Connection getConnection();
}
