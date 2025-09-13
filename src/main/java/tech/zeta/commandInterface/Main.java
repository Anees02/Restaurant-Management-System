package tech.zeta.commandInterface;

import tech.zeta.entity.Customer;
import tech.zeta.service.CustomerService;
import tech.zeta.service.TableService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Main {
  static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
  static CustomerService customerService = new CustomerService();
  static TableService tableService = new TableService();
  public static void main(String[] args) throws IOException {
    boolean loopBreakFlag = false;
    while(true) {
      System.out.printf("%n%n%n");
      System.out.println("Welcome to Dilip's Internation Restaurant");
      System.out.printf("\t1. Customer%n\t2. Employee%n\t3. Exit%n");
      System.out.print("Please Enter the Option: ");
      int option = Integer.parseInt(bufferedReader.readLine());
      if(option == 1){
        System.out.printf("\t1. Login%n\t2. Sign Up%n\t3. Exit%n");
        System.out.print("Please Enter the Option: ");
        option = Integer.parseInt(bufferedReader.readLine());
        switch (option){
          case 1:
            customerLogin();
            break;
          case 2:
            customerSignUp();
            break;
          case 3: loopBreakFlag = true;
        }
      }
      if(option == 3){
        return;
      }

    }
  }
  public static void customerLogin() throws IOException{
    System.out.println("Welcome to Login");
    System.out.print("Enter the EmailID: ");
    String emailId = bufferedReader.readLine();
    System.out.print("Enter the Password: ");
    String password = bufferedReader.readLine();
    Customer customer = customerService.getCustomer(emailId, password);
    if(customer == null){
      System.out.println("User Details Not Found, Please try again!");
      return;
    }
    System.out.println("Login Successful!");
    System.out.println("Welcome "+customer.getCustomerName());
    System.out.printf("\t1. Book Table%n\t2. Go Back%n");
    System.out.print("Please Enter the option number: ");
    int option = Integer.parseInt(bufferedReader.readLine());
    if(option == 1){
        bookTable(customer);
    }
  }
  public static void customerSignUp() throws IOException{
    System.out.println("Welcome to Sign Up");
    System.out.print("Enter your name: ");
    String name = bufferedReader.readLine();
    System.out.print("Enter your emailId: ");
    String emailId = bufferedReader.readLine();
    System.out.print("Enter your contact number: ");
    String contactNumber = bufferedReader.readLine();
    System.out.print("Enter the new password: ");
    String password = bufferedReader.readLine();

    if(customerService.addCustomer(name,contactNumber, emailId, password)){
      System.out.println("Congratulations, Sign Up Successfull");
      System.out.println("Please Login Now");
    }
    else{
      System.out.println("Sorry!, User Already Exits...");
    }
  }
  public static void bookTable(Customer customer) throws IOException {
    List<int[]> tables = tableService.giveAvailableTables();
    if(tables.isEmpty()){
      System.out.println("No Tables are Available");
      return;
    }
    System.out.println("All the Available Tables are: ");
    for(int[] table: tables){
      System.out.printf("Table Id: %-4d Table Capacity: %d%n", table[0], table[1]);
    }

    System.out.print("Enter the Table Id to Book: ");
    int tableId = Integer.parseInt(bufferedReader.readLine());
    if(tableService.bookTable(customer.getCustomerId(), tableId)){
      System.out.printf("Congrats %s, you have successfully booked the table with table id = %d%n",customer.getCustomerName(), tableId);
      System.out.println("See you in the restaurant!....");
    }

  }
}
