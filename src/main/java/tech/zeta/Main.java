package tech.zeta;



import lombok.extern.slf4j.Slf4j;
import tech.zeta.commandInterface.*;
import tech.zeta.model.Employee;
import tech.zeta.utils.enums.EmployeeType;
import tech.zeta.exception.EmployeeNotFoundException;
import tech.zeta.exception.PasswordIncorrectException;
import tech.zeta.repository.*;
import tech.zeta.service.EmployeeService;
import tech.zeta.utils.EmailValidator;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

@Slf4j
public class Main {
  static Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) throws IOException {
    boolean loopBreakFlag = false;

    while(true) {
      System.out.println("\n====== Welcome to Internation Restaurant ======");
      System.out.printf("Choose your login type:%n");
      System.out.printf("\t1. Customer%n\t2. Employee%n\t3. Exit%n");
      System.out.print("Please Enter the Option: ");


      int option = -1;
      try {
        option  = scanner.nextInt();
      } catch (InputMismatchException exception) {
        System.out.println("Please Enter Valid Input!");
        scanner.nextLine();
        continue;
      }
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
        default:
          System.out.println("Invalid Option, Please Try Again!");
      }



    }
  }
  public static void employeeLogin(){
    System.out.println("\t=== Welcome to Employee Login Page ===");
    System.out.print("Enter emailId: ");
    String emailId = scanner.nextLine().trim();
    if(!EmailValidator.isValid(emailId)){
      System.out.println("Please Enter a Valid EmailId");
      return;
    }
    System.out.print("Enter password: ");
    String password = scanner.nextLine();


    EmployeeService employeeService = new EmployeeService();

    try {
      Employee employee = employeeService.getEmployee(emailId, password);

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
      }
    } catch (EmployeeNotFoundException | PasswordIncorrectException exception) {
      System.out.println(exception.getMessage());
    }
  }



}
