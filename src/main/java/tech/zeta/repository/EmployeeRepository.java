package tech.zeta.repository;

import lombok.extern.slf4j.Slf4j;
import tech.zeta.model.Employee;
import tech.zeta.utils.enums.EmployeeType;
import tech.zeta.exception.EmployeeNotFoundException;
import tech.zeta.utils.DB.PostgresDBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Repository class for managing employee data.
 * Provides methods to fetch employee details by email.
 * Throws EmployeeNotFoundException if employee is not found.
 * Singleton pattern is used to provide a single instance.
 */
@Slf4j
public class EmployeeRepository {
  private static EmployeeRepository employeeRepository;
  private Connection DBConnection;
  private EmployeeRepository(){
    DBConnection = new PostgresDBConnection().getConnection();
  }
  /**
   * Retrieves the singleton instance of EmployeeRepository.
   *
   * @return the single instance of EmployeeRepository
   */
  public static EmployeeRepository getInstance(){
    if(employeeRepository == null){
      employeeRepository = new EmployeeRepository();
    }

    return employeeRepository;
  }

  /**
   * Fetches an employee by their email ID.
   *
   * @param email the email address of the employee
   * @return the Employee object if found
   * @throws EmployeeNotFoundException if no employee is found with the given email
   */
  public Employee getEmployee(String email) throws EmployeeNotFoundException {
    String checkQuery = "SELECT * FROM employee WHERE emailId = ?";

    try(PreparedStatement preparedStatement = DBConnection.prepareStatement(checkQuery)){

      preparedStatement.setString(1,email);
      ResultSet resultSet = preparedStatement.executeQuery();
      if(resultSet.next()){
        int employeeId  = resultSet.getInt("employeeId");
        String employeeName = resultSet.getString("employeeName");
        String emailId = resultSet.getString("emailId");
        EmployeeType employeeType = EmployeeType.valueOf(resultSet.getString("employeeType"));
        String employeePassword = resultSet.getString("password");
        Employee employee = new Employee(employeeId, employeeName, emailId, employeeType, employeePassword);
        resultSet.close();
        return employee;
      }
  } catch (SQLException error) {
      log.error(error.getMessage());
  }
    throw new EmployeeNotFoundException("Employee Not Found!");
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
