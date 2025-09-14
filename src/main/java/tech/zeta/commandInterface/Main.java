package tech.zeta.commandInterface;



import tech.zeta.entity.Employee;
import tech.zeta.entity.enums.EmployeeType;
import tech.zeta.repository.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Main {
  static Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) throws IOException {
    boolean loopBreakFlag = false;

    while(true) {
      System.out.println("\n====== Welcome to Dilip's Internation Restaurant ======");
      System.out.printf("Choose your login type:%n");
      System.out.printf("\t1. Customer%n\t2. Employee%n\t3. Exit%n");
      System.out.print("Please Enter the Option: ");
      int option = scanner.nextInt();
      scanner.nextLine();
      switch (option){
        case 1:
          CustomerCLI customerMenu = new CustomerCLI();
          customerMenu.start();
          break;
        case 2:
          employeeLogin();
          break;
        case 3:
          System.out.println("Thanks for visiting, See you again");

          CustomerRepository.getInstance().closeConnection();
          EmployeeRepository.getInstance().closeConnection();
          FoodItemRepository.getInstance().closeConnection();
          OrderRepository.getInstance().closeConnection();
          TableRepository.getInstance().closeConnection();
          return;
      }

    }
  }
  public static void employeeLogin(){
    System.out.println("=== Welcome to Employee Login Page ===");
    System.out.print("Enter emailId: ");
    String emailId = scanner.nextLine();
    System.out.print("Enter password: ");
    String password = scanner.nextLine();

    EmployeeRepository employeeRepository = EmployeeRepository.getInstance();
    Employee employee = employeeRepository.getEmployee(emailId, password);

    switch (employee.getEmployeeType()) {
      case EmployeeType.WAITER :
        WaiterCLI waiterCLI = new WaiterCLI(employee);
        waiterCLI.start();
        break;
      case EmployeeType.CHEF:
        ChefCLI chefCLI = new ChefCLI();
        chefCLI.chefMenu(employee);
        break;
      case EmployeeType.MANAGER:
        ManagerCLI managerCLI = new ManagerCLI();
        managerCLI.start(employee);
        break;
      case EmployeeType.ADMIN:
        AdminCLI adminCLI = new AdminCLI(employee);
        adminCLI.start();
      default:
        System.out.println("Employee Not Found!");
    }
  }

}
