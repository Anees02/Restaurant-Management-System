package tech.zeta.commandInterface;

import lombok.extern.slf4j.Slf4j;
import tech.zeta.model.Customer;
import tech.zeta.model.FoodItem;
import tech.zeta.exception.CustomerNotFoundException;
import tech.zeta.exception.PasswordIncorrectException;
import tech.zeta.repository.FoodItemRepository;
import tech.zeta.service.ChefService;
import tech.zeta.service.CustomerService;
import tech.zeta.service.TableService;
import tech.zeta.utils.EmailValidator;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

@Slf4j
public class CustomerCLI {
  private final Scanner scanner = new Scanner(System.in);
  private final CustomerService customerService;
  private final FoodItemRepository foodItemRepository;
  private final TableService tableService;
  private Customer currentCustomer;

  public CustomerCLI() {
    customerService = new CustomerService();
    foodItemRepository = FoodItemRepository.getInstance();
    tableService = new TableService();
  }

  public void start() {
    loginOrRegister();

  }

  private void loginOrRegister() {
    System.out.println("1. Login");
    System.out.println("2. Register");
    System.out.println("3. Go Back");
    System.out.print("Choose option: ");
    int choice = -1;
    try {
      choice = scanner.nextInt();
    } catch (InputMismatchException exception) {
      System.out.println("Please Enter Valid Input!");
      scanner.nextLine();
      return;
    }
    scanner.nextLine();

    if (choice == 1) {
      if(login())
        customerMenuLoop();
    }
    else if(choice == 2) {
      register();
    }

  }

  private boolean login() {
    System.out.print("Enter Email: ");
    String email = scanner.nextLine();
    if(!EmailValidator.isValid(email)){
      System.out.println("Please Enter a Valid Email Id!");
      return false;
    }
    System.out.print("Enter Password: ");
    String password = scanner.nextLine();
    try {
      currentCustomer = customerService.getCustomer(email, password);
      System.out.println("Welcome back, " + currentCustomer.getCustomerName() + "!");
      return true;
    } catch (PasswordIncorrectException | CustomerNotFoundException exception) {
      System.out.println(exception.getMessage());
    }
    return false;
  }

  private void register() {
    System.out.print("Enter Name: ");
    String name = scanner.nextLine();
    System.out.print("Enter Contact Number: ");
    String contact = scanner.nextLine();
    System.out.print("Enter Email: ");
    String email = scanner.nextLine();
    if(!EmailValidator.isValid(email)){
      System.out.println("Please Enter a Valid Email Id!");
      return;
    }
    System.out.print("Enter New Password: ");
    String password = scanner.nextLine();

    currentCustomer = new Customer();
    currentCustomer.setCustomerName(name);
    currentCustomer.setContactNumber(contact);
    currentCustomer.setCustomerMailId(email);
    currentCustomer.setPassword(password);

    customerService.addCustomer(currentCustomer);
    System.out.println("Registered successfully. Welcome, " + name + "!");
  }

  private void customerMenuLoop() {
    while (true) {
      System.out.println("\n\t=== Customer Menu ===");
      System.out.println("1. View Available Food Items");
      System.out.println("2. Book Table Online");
      System.out.println("3. Exit");
      System.out.print("Choose option: ");
      int choice = 0;
      try {
        choice  = scanner.nextInt();
      } catch (InputMismatchException exception) {
        System.out.println("Please Enter Valid Input!");
        scanner.nextLine();
        continue;
      }
      scanner.nextLine();

      switch (choice) {
        case 1 -> viewFoodItems();
        case 2 -> bookTable();
        case 3 -> {
          System.out.println("Thank you! Visit again.");
          return;
        }
        default -> System.out.println("Invalid choice, try again.");
      }
    }
  }

  private void viewFoodItems() {
    List<FoodItem> foodItems = foodItemRepository.getAllItems();
    System.out.println("\n\t--- Available Food Items ---");
    for (FoodItem food : foodItems) {
      if(food.isAvailable())
        System.out.printf("%d. %-24s - %.2f%n", food.getFoodItemId(), food.getFoodName(), food.getPrice());
    }
  }

  public void bookTable() {
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
    int tableId = -1;
    try {
      tableId  = scanner.nextInt();
    } catch (InputMismatchException exception) {
      System.out.println("Please Enter Valid Input!");
      return;
    }
    if(tableService.bookTable(currentCustomer.getCustomerId(), tableId)){
      System.out.printf("Congrats %s, you have successfully booked the table with table id = %d%n",currentCustomer.getCustomerName(), tableId);
      System.out.println("See you in the restaurant!....");
    }
    else{
      System.out.println("Sorry! the table is already booked, please try again!");
    }

  }
}
