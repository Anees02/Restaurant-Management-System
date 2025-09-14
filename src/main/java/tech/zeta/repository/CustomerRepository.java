package tech.zeta.repository;

import lombok.extern.slf4j.Slf4j;
import tech.zeta.entity.Customer;
import tech.zeta.utils.DB.PostgresDBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class CustomerRepository {
  private static CustomerRepository customerRepository;
  private Connection DBConnection;
  private CustomerRepository(){
    DBConnection = new PostgresDBConnection().getConnection();
  }

  public static CustomerRepository getInstance(){
    if(customerRepository == null){
      customerRepository = new CustomerRepository();
    }

    return customerRepository;
  }




  public boolean addCustomer(Customer customer){
    String insertQuery = "insert into customer(customerName, contactNumber, customerMailId, password) values (?,?,?,?);";
    try(PreparedStatement preparedStatement = DBConnection.prepareStatement(insertQuery)) {

      preparedStatement.setString(1,customer.getCustomerName());
      preparedStatement.setString(2,customer.getContactNumber());
      preparedStatement.setString(3,customer.getCustomerMailId());
      preparedStatement.setString(4,customer.getPassword());
      int rowsEffected = preparedStatement.executeUpdate();
      return rowsEffected > 0;

    } catch (SQLException sqlException) {
      log.error(sqlException.getMessage());
    }
    return false;
  }

  public Customer getCustomerByEmail(String emailId){
    String checkQuery = "SELECT * FROM customer WHERE customerMailId = ?;";

    try(PreparedStatement preparedStatement = DBConnection.prepareStatement(checkQuery)){

      preparedStatement.setString(1,emailId);
      ResultSet resultSet = preparedStatement.executeQuery();
      if(resultSet.next()){
        int customerID  = resultSet.getInt(1);
        String customerName = resultSet.getString(2);
        String customerContactNumber = resultSet.getString(3);
        String customerEmailId = resultSet.getString(4);
        String password = resultSet.getString(5);

        Customer customer = new Customer(customerID, customerName, customerContactNumber, customerEmailId, password);
        resultSet.close();
        return customer;
      }

    }
    catch (SQLException sqlException){
      log.error(sqlException.getMessage());
    }

    return null;
  }

  public boolean isUserAlreadyExists(String mailId){
    String checkQuery = "SELECT 1 FROM customer WHERE customerMailId = ?;";

    try(PreparedStatement preparedStatement = DBConnection.prepareStatement(checkQuery)){

      preparedStatement.setString(1,mailId);
      ResultSet resultSet = preparedStatement.executeQuery();
      if(resultSet.next()) {
        resultSet.close();
        return true;
      }

    }
    catch (SQLException sqlException){
      log.error(sqlException.getMessage());
    }

    return false;
  }
  public void closeConnection(){
    try {
      DBConnection.close();
    } catch (SQLException error) {
      log.error("Failed to Close Connection {}",error.getMessage());
    }
  }



}
