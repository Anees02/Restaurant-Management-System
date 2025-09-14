package tech.zeta.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import tech.zeta.exception.EmployeeNotFoundException;
import tech.zeta.model.Employee;
import tech.zeta.utils.enums.EmployeeType;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EmployeeRepositoryDatabaseTest {

  private EmployeeRepository employeeRepository;

  @BeforeAll
  void init() {
    employeeRepository = EmployeeRepository.getInstance();
  }

  @Test
  void testGetEmployee_success() throws Exception {

    Employee employee = employeeRepository.getEmployee("ravi@example.com");

    assertNotNull(employee);
    assertEquals("Ravi Kumar", employee.getEmployeeName());
    assertEquals(EmployeeType.WAITER, employee.getEmployeeType());
  }

  @Test
  void testGetEmployee_notFound() {
    assertThrows(EmployeeNotFoundException.class, () ->
            employeeRepository.getEmployee("missing@example.com")
    );
  }
}
