package tech.zeta.utils.DB;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Provides a connection to a PostgreSQL database using configuration
 * from the `application.properties` file.
 * Implements the DBConnection interface.
 */
@Slf4j
public class PostgresDBConnection implements DBConnection {
  private Connection connection;
  private Properties config = new Properties();

  public PostgresDBConnection(){

    try(FileInputStream fileInputStream = new FileInputStream("src/main/resources/application.properties")) {
      config.load(fileInputStream);
      String database_url = config.getProperty("database_url");
      String username = config.getProperty("username");
      String password = config.getProperty("password");

      connection = DriverManager.getConnection(database_url, username, password);
    } catch (SQLException | IOException exception){
      log.error(exception.getMessage());
      exception.printStackTrace();
    }
  }

  /**
   * Returns the established database connection.
   *
   * @return the Connection object to the PostgreSQL database
   */
  @Override
  public Connection getConnection() {
    return connection;
  }
}
