package tech.zeta.repository;

import lombok.extern.slf4j.Slf4j;
import tech.zeta.model.Customer;
import tech.zeta.exception.CustomerNotFoundException;
import tech.zeta.utils.DB.PostgresDBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Repository class for managing customer data.
 * Provides methods to add new customers, fetch customers by email,
 * and check if a user already exists.
 * Throws CustomerNotFoundException if customer is not found.
 * Singleton pattern is used to provide a single instance.
 */
@Slf4j
public class CustomerRepository {
  private static CustomerRepository customerRepository;
  private Connection DBConnection;
  private CustomerRepository(){
    DBConnection = new PostgresDBConnection().getConnection();
  }

  /**
   * Retrieves the singleton instance of CustomerRepository.
   *
   * @return the single instance of CustomerRepository
   */
  public static CustomerRepository getInstance(){
    if(customerRepository == null){
      customerRepository = new CustomerRepository();
    }

    return customerRepository;
  }


  /**
   * Adds a new customer to the database.
   *
   * @param customer the Customer object to add
   * @return true if the customer is added successfully, false otherwise
   */
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

  /**
   * Fetches a customer by their email ID.
   *
   * @param emailId the email address of the customer
   * @return the Customer object if found
   * @throws CustomerNotFoundException if no customer is found with the given email
   */
  public Customer getCustomerByEmail(String emailId) throws CustomerNotFoundException {
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

    throw new CustomerNotFoundException("Customer Not Found!");
  }

  /**
   * Checks if a customer already exists with the given email ID.
   *
   * @param mailId the email address to check
   * @return true if a customer with the email already exists, false otherwise
   */
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


  /**
   * Closes the database connection used by this repository.
   */
  public void closeConnection(){
    try {
      DBConnection.close();
    } catch (SQLException error) {
      log.error("Failed to Close Connection {}",error.getMessage());
    }
  }



}
