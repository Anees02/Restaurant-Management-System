package tech.zeta.service;

import tech.zeta.model.Employee;
import tech.zeta.exception.EmployeeNotFoundException;
import tech.zeta.exception.PasswordIncorrectException;
import tech.zeta.repository.EmployeeRepository;

public class EmployeeService {
  private EmployeeRepository employeeRepository;
  public EmployeeService(){
    employeeRepository = EmployeeRepository.getInstance();
  }

  /**
   * Retrieves an employee by email and validates the password.
   *
   * @param email the email address of the employee
   * @param password the password to validate
   * @return the Employee object if the email exists and password matches
   * @throws EmployeeNotFoundException if no employee is found with the given email
   * @throws PasswordIncorrectException if the provided password does not match
   */
  public Employee getEmployee(String email, String password) throws EmployeeNotFoundException, PasswordIncorrectException {
    Employee employee = employeeRepository.getEmployee(email);
    if(password.equals(employee.getPassword())){
      return employee;
    }
    throw new PasswordIncorrectException("Incorrect Password");
  }
}
