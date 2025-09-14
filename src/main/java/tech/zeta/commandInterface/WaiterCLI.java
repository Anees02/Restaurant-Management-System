package tech.zeta.commandInterface;

import tech.zeta.model.Employee;
import tech.zeta.model.FoodItem;
import tech.zeta.repository.FoodItemRepository;
import tech.zeta.service.TableService;
import tech.zeta.service.WaiterService;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class WaiterCLI {
  private final WaiterService waiterService;
  private final TableService tableService;
  private final FoodItemRepository foodItemRepository;
  private Employee waiter;
  private final Scanner scanner = new Scanner(System.in);

  public WaiterCLI(Employee waiter){
    waiterService = new WaiterService();
    tableService = new TableService();
    foodItemRepository = FoodItemRepository.getInstance();
    this.waiter = waiter;
  }
  public void start() {
    while (true) {
      System.out.println("\n=== Waiter Menu ===");
      System.out.println("1. Take New Order");
      System.out.println("0. Exit");
      System.out.print("Choose: ");
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
        case 1 -> takeNewOrder();
        case 0 -> {
          System.out.println("Exiting Waiter Menu...");
          return;
        }
        default -> System.out.println("Invalid choice.");
      }
    }
  }


  private void takeNewOrder() {
    System.out.print("Enter Table ID: ");
    int tableId = -1;
    try {
      tableId  = scanner.nextInt();
    } catch (InputMismatchException exception) {
      System.out.println("Please Enter Valid Input!");
      return;
    }

    int customerId = tableService.giveCustomerId(tableId);
    if(customerId == -1){
      System.out.println("The Customer has to book the table, so please ask the customer.");
      return;
    }
    scanner.nextLine();

    // create order
    int orderId = waiterService.createOrder(customerId,tableId);
    if (orderId == -1) {
      System.out.println(" Failed to create order.");
      return;
    }
    System.out.println("Order created with ID: " + orderId);

    while (true) {
      List<FoodItem> foodItems = foodItemRepository.getAllItems();
      System.out.println("\n--- Menu ---");
      for (FoodItem food : foodItems) {
        if(food.isAvailable())
          System.out.printf("%d. %-24s - %.2f%n", food.getFoodItemId(), food.getFoodName(), food.getPrice());
      }

      System.out.print("Enter Food ID (0 to finish): ");
      int foodId = scanner.nextInt();
      if (foodId == 0) break;

      FoodItem food = foodItemRepository.getFoodItemById(foodId);
      if (food == null || !food.isAvailable()) {
        System.out.println("Invalid or unavailable item.");
        continue;
      }

      System.out.print("Enter Quantity: ");
      int qty = scanner.nextInt();

      boolean added = waiterService.addOrderItem(orderId, foodId, qty, food.getPrice());
      if (added) {
        System.out.println("Added " + qty + "x " + food.getFoodName());
      } else {
        System.out.println(" Could not add item.");
      }
    }
  }
}
