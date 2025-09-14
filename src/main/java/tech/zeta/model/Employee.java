package tech.zeta.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.zeta.utils.enums.EmployeeType;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
  private int employeeId;
  private String employeeName;
  private String emailId;
  private EmployeeType employeeType;
  private String password;
}
