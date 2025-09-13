package tech.zeta.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.zeta.entity.enums.EmployeeType;

import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
  private UUID employeeId;
  private String employeeName;
  private String emailId;
  private EmployeeType employeeType;
  private String password;
}
