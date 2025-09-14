package tech.zeta.commandInterface;


import tech.zeta.entity.Employee;
import tech.zeta.entity.FoodItem;
import tech.zeta.entity.Order;
import tech.zeta.entity.OrderItem;
import tech.zeta.entity.enums.ItemStatus;
import tech.zeta.repository.FoodItemRepository;
import tech.zeta.service.ManagerService;

import java.time.LocalDateTime;
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
      int choice = scanner.nextInt();
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
    int tableId = scanner.nextInt();
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
    System.out.print("Enter Order ID to record payment: ");
    int orderId = scanner.nextInt();
    scanner.nextLine();

    boolean success = managerService.makePayment(orderId);
    if (success) {
      System.out.println("Payment recorded successfully for Order #" + orderId);
    } else {
      System.out.println("Could not record payment. Order may not exist or is already completed.");
    }
  }
}
