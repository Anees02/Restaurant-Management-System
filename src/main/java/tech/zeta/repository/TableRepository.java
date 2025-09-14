package tech.zeta.repository;

import lombok.extern.slf4j.Slf4j;
import tech.zeta.utils.enums.TableStatus;
import tech.zeta.utils.DB.PostgresDBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class for managing restaurant table data.
 * Provides methods to book, release, and check status of tables,
 * as well as retrieve available tables and customer assignments.
 * Singleton pattern is used to provide a single instance.
 */
@Slf4j
public class TableRepository {
  private Connection connection;
  private static TableRepository tableRepository;
  private TableRepository(){
    connection = new PostgresDBConnection().getConnection();
  }

  /**
   * Retrieves the singleton instance of TableRepository.
   *
   * @return the single instance of TableRepository
   */
  public static  TableRepository getInstance(){
    if(tableRepository == null){
      tableRepository = new TableRepository();
    }

    return tableRepository;
  }


  /**
   * Retrieves the tableStatus of the table with tableId
   * @param tableId the table id
   * @return tableStatus type TableStatus (enum)
   */
  public TableStatus giveTableStatus(int tableId){
    String tableAvailable = "SELECT tableStatus FROM RestaurantTable WHERE tableId = ?";
    try(PreparedStatement preparedStatement = connection.prepareStatement(tableAvailable)){
      preparedStatement.setInt(1,tableId);
      ResultSet resultSet = preparedStatement.executeQuery();
      if(resultSet.next()){

        TableStatus tableStatus =  TableStatus.valueOf(resultSet.getString("tableStatus"));
        resultSet.close();
        return tableStatus;
      }
    } catch (SQLException sqlException) {
      log.error(sqlException.getMessage());
    }

    return null;
  }

  /**
   * Retrieves all tables from the database.
   *
   * @return a list of RestaurantTable objects representing all tables
   */
  public List<int[]> giveAvailableTables(){
    String AvailableTablesQuery = "SELECT tableId, tableCapacity FROM RestaurantTable WHERE tableStatus = 'AVAILABLE'";
    try(PreparedStatement preparedStatement = connection.prepareStatement(AvailableTablesQuery)){
      ResultSet rs = preparedStatement.executeQuery();
      List<int[]> tables = new ArrayList<>();
      while(rs.next()){
        int tableId = rs.getInt(1);
        int tableCapacity = rs.getInt(2);
        tables.add(new int[]{tableId,tableCapacity});
      }
      rs.close();
      return tables;
    } catch (SQLException sqlException) {
      log.error(sqlException.getMessage());
    }
    return null;

  }

  /**
   * book tableId for the customerId
   *
   * @param tableId the ID of the table
   * @param customerId the ID of the customer.
   * @return true if customerId book the table, false otherwise.
   */
  public boolean bookTable(int tableId, int customerId) {
    String bookTableQuery = "UPDATE RestaurantTable SET tableStatus = ?, customerId = ?, booking_time = NOW() WHERE tableId = ?";

    try (PreparedStatement preparedStatement = connection.prepareStatement(bookTableQuery)) {
      preparedStatement.setString(1, TableStatus.BOOKED.name());
      preparedStatement.setInt(2, customerId);
      preparedStatement.setInt(3, tableId);

      int rowAffected = preparedStatement.executeUpdate();
      if (rowAffected > 0) {
        log.info("Table {} booked successfully for customer {}", tableId, customerId);
        return true;
      }
    } catch (SQLException sqlException) {
      log.error("Error booking table {} for customer {}: {}", tableId, customerId, sqlException.getMessage());
    }
    return false;
  }

  /**
   * Updates the status of a specific table in the database.
   *
   * @param tableId the ID of the table to update
   * @param tableStatus the new status to set for the table
   * @return true if the update was successful, false otherwise
   */
  public boolean updateTableStatus(int tableId, TableStatus tableStatus, Integer customerId){
    String updateSql = "update RestaurantTable SET tableStatus = ?, customerId = ? WHERE tableId = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(updateSql)) {
      preparedStatement.setString(1, tableStatus.name());
      preparedStatement.setInt(2, customerId);
      preparedStatement.setInt(3, tableId);

      int rowAffected = preparedStatement.executeUpdate();
      if (rowAffected > 0) {
        return true;
      }
    } catch (SQLException sqlException) {
      log.error(sqlException.getMessage());
    }

    return false;
  }

  /**
   * retrieves customerId who booked the tableId
   *
   * @param tableId the ID of the table.
   * @return customerID who booked the tableId
   */
  public int getCustomerId(int tableId){
    String updateSql = "select customerId from restauranttable where tableId = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(updateSql)) {
      preparedStatement.setInt(1, tableId);


      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return resultSet.getInt("customerId");
      }
    } catch (SQLException sqlException) {
      log.error(sqlException.getMessage());
    }

    return -1;
  }



  public void closeConnection(){
    try {
      connection.close();
    } catch (SQLException error) {
      log.error("Failed to Close Connection {}",error.getMessage());
    }
  }

}
