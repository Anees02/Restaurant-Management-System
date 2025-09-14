package tech.zeta.commandInterface;

import tech.zeta.model.Employee;
import tech.zeta.model.FoodItem;
import tech.zeta.service.AdminService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class AdminCLI {
  private final AdminService adminService;
  private final Scanner scanner;
  private Employee employee;
  public AdminCLI(Employee employee) {
    this.adminService = new AdminService();
    this.scanner = new Scanner(System.in);
    this.employee = employee;
  }

  public void start() {
    boolean running = true;
    while (running) {
      System.out.println("\n================= ADMIN MENU =================");
      System.out.println("1. Add Food Item");
      System.out.println("2. Update Food Item");
      System.out.println("3. Remove Food Item");
      System.out.println("4. View Daily Sales Report");
      System.out.println("5. Exit");
      System.out.println("==============================================");
      System.out.print("Choose an option: ");

      int choice = -1;
      try {
        choice  = scanner.nextInt();
      } catch (InputMismatchException exception) {
        System.out.println("Please Enter Valid Input!");
        scanner.nextLine();
        continue;
      }
      scanner.nextLine();

      switch (choice) {
        case 1 -> addFoodItem();
        case 2 -> updateFoodItem();
        case 3 -> removeFoodItem();
        case 4 -> viewDailySalesReport();
        case 5 -> running = false;
        default -> System.out.println("Invalid choice. Try again!");
      }
    }
  }

  private void addFoodItem() {

    try {
      System.out.print("Enter Food Name: ");
      String name = scanner.nextLine();

      System.out.print("Enter Price: ");
      double price = scanner.nextDouble();

      System.out.print("Is Available? (true/false): ");
      boolean available = scanner.nextBoolean();
      scanner.nextLine();

      FoodItem foodItem = new FoodItem(0, name, price, available);
      if (adminService.addFoodItem(foodItem)) {
        System.out.printf("Food Item '%s' added successfully!%n", name);
      } else {
        System.out.printf("Failed to add Food Item '%s'.%n", name);
      }
    }
    catch (InputMismatchException exception){
      System.out.println("Please Enter the Valid Input!");
    }
  }

  private void updateFoodItem() {

    try {

      System.out.print("Enter Food ID to update: ");
      int foodId = scanner.nextInt();

      System.out.print("Enter New Price: ");
      double newPrice = scanner.nextDouble();

      System.out.print("Is Available? (true/false): ");
      boolean available = scanner.nextBoolean();
      scanner.nextLine();

      if (adminService.updateFoodItem(foodId, newPrice, available)) {
        System.out.printf("Food Item #%d updated successfully!%n", foodId);
      } else {
        System.out.printf("Failed to update Food Item #%d.%n", foodId);
      }
    } catch (InputMismatchException exception) {
      System.out.println("Please Enter a Valid Input!");
    }
  }

  private void removeFoodItem() {

    try {

      System.out.print("Enter Food ID to remove: ");
      int foodId = scanner.nextInt();
      scanner.nextLine();

      if (adminService.removeFoodItem(foodId)) {
        System.out.printf("Food Item #%d removed successfully!%n", foodId);
      } else {
        System.out.printf("Failed to remove Food Item #%d.%n", foodId);
      }
    } catch (InputMismatchException exception) {
      System.out.println("Please Enter a Valid Input");
    }
  }

  private void viewDailySalesReport() {
    try {
      System.out.print("Enter date (YYYY-MM-DD): ");
      String dateInput = scanner.nextLine();
      LocalDate date = LocalDate.parse(dateInput);

      double sales = adminService.getDailySalesReport(date);
      System.out.printf("Total Sales on %s = ₹%.2f%n", date, sales);
    }
    catch (InputMismatchException | DateTimeParseException exception){
      System.out.println("Please Enter a Valid Input!");
    }
  }
}
