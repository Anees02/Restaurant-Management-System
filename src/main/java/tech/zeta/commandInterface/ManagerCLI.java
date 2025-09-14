package tech.zeta.commandInterface;


import tech.zeta.model.Employee;
import tech.zeta.model.FoodItem;
import tech.zeta.model.Order;
import tech.zeta.model.OrderItem;
import tech.zeta.utils.enums.ItemStatus;
import tech.zeta.repository.FoodItemRepository;
import tech.zeta.service.ManagerService;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ManagerCLI {

  private final Scanner scanner = new Scanner(System.in);
  private final ManagerService managerService;
  private final FoodItemRepository foodItemRepository;
  private Employee manager;
  public ManagerCLI() {
    this.managerService = new ManagerService();
    foodItemRepository = FoodItemRepository.getInstance();
  }

  public void start(Employee manager) {
    this.manager = manager;
    System.out.printf("Welcome, %s",manager.getEmployeeName());
    while (true) {
      System.out.println("\n=== Manager Menu ===");
      System.out.println("1. Generate Bill");
      System.out.println("2. Record Payment");
      System.out.println("0. Exit");
      System.out.print("Enter choice: ");
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
        case 1 -> generateBill();
        case 2 -> recordPayment();
        case 0 -> {
          System.out.println("Exiting Manager Menu...");
          return;
        }
        default -> System.out.println("Invalid choice, try again.");
      }
    }
  }

  private void generateBill() {
    System.out.print("Enter Table ID: ");
    int tableId = -1;
    try {
      tableId  = scanner.nextInt();
    } catch (InputMismatchException exception) {
      System.out.println("Please Enter Valid Input!");
      return;
    }
    scanner.nextLine();

    Integer orderId = managerService.getOrderIdForTable(tableId);
    if (orderId == null) {
      System.out.println("No active order found for Table " + tableId);
      return;
    }

    Order order = managerService.generateBill(orderId);
    if (order == null) {
      System.out.println("Bill could not be generated.");
      return;
    }

    System.out.println("\n\t--- Bill for Order #" + order.getOrderId() + " ---");
    System.out.printf("%-12s: %s%n", "Customer ID", order.getCustomerId());
    System.out.printf("%-12s: %s%n", "Table ID", order.getTableId());
    System.out.printf("%-12s: %s%n", "Bill Date", order.getOrderTime().toLocalDate());
    System.out.println("Items:");
    for(OrderItem orderItem: order.getItems()){
      if(orderItem.getItemStatus() == ItemStatus.PREPARED) {
        FoodItem foodItem = foodItemRepository.getFoodItemById(orderItem.getFoodItemId());
        System.out.printf("  - FoodItem: %-20s | Qty: %-2d | Price: %.2f%n", foodItem.getFoodName(), orderItem.getQuantity(), orderItem.getPrice());
      }
    }
    System.out.println("Total Amount  : ₹" + order.getTotalAmount());
    System.out.println("Payment Status: " + order.getPaymentStatus());
  }

  private void recordPayment() {
    System.out.print("Enter Table ID to record payment: ");
    int tableId = -1;
    try {
      tableId  = scanner.nextInt();
    } catch (InputMismatchException exception) {
      System.out.println("Please Enter Valid Input!");
      return;
    }
    scanner.nextLine();
    Integer orderId = managerService.getOrderIdForTable(tableId);

    if(orderId == null){
      System.out.println("Please Enter a Table ID which is occupied!");
      return;
    }

    boolean success = managerService.makePayment(orderId) && managerService.makeTableAvailable(tableId);

    if (success) {
      System.out.println("Payment recorded successfully for Order #" + orderId);
    } else {
      System.out.println("Could not record payment. Order may not exist or is already completed.");
    }
  }
}
