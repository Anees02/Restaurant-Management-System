package tech.zeta.repository;

import lombok.extern.slf4j.Slf4j;
import tech.zeta.entity.Customer;
import tech.zeta.entity.Employee;
import tech.zeta.entity.enums.EmployeeType;
import tech.zeta.utils.DB.PostgresDBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class EmployeeRepository {
  private static EmployeeRepository employeeRepository;
  private Connection DBConnection;
  private EmployeeRepository(){
    DBConnection = new PostgresDBConnection().getConnection();
  }

  public static EmployeeRepository getInstance(){
    if(employeeRepository == null){
      employeeRepository = new EmployeeRepository();
    }

    return employeeRepository;
  }

  public Employee getEmployee(String email, String password){
    String checkQuery = "SELECT * FROM employee WHERE emailId = ? and password = ?;";

    try(PreparedStatement preparedStatement = DBConnection.prepareStatement(checkQuery)){

      preparedStatement.setString(1,email);
      preparedStatement.setString(2, password);
      ResultSet resultSet = preparedStatement.executeQuery();
      if(resultSet.next()){
        int employeeId  = resultSet.getInt("employeeId");
        String employeeName = resultSet.getString("employeeName");
        String emailId = resultSet.getString("emailId");
        EmployeeType employeeType = EmployeeType.valueOf(resultSet.getString("employeeType"));
        String employeePassword = resultSet.getString("password");
        Employee employee = new Employee(employeeId, employeeName, emailId, employeeType, password);
        resultSet.close();
        return employee;
      }
  } catch (SQLException error) {
      log.error(error.getMessage());
  }
    return null;
  }

  public void closeConnection(){
    try {
      DBConnection.close();
    } catch (SQLException error) {
      log.error("Failed to Close Connection {}",error.getMessage());
    }
  }
}
