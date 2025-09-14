package tech.zeta.commandInterface;


import tech.zeta.entity.Employee;
import tech.zeta.entity.Order;
import tech.zeta.entity.OrderItem;
import tech.zeta.entity.enums.EmployeeType;
import tech.zeta.entity.enums.ItemStatus;
import tech.zeta.repository.OrderRepository;
import tech.zeta.service.ChefService;

import java.util.List;
import java.util.Scanner;

public class ChefCLI {

  private final Scanner scanner = new Scanner(System.in);
  private final ChefService chefService;
  public ChefCLI(){
    chefService = new ChefService();
  }
  private  Employee chef;
  public void chefMenu(Employee chef) {
    this.chef = chef;
    System.out.printf("Welcome, chef %-8s",chef.getEmployeeName());
    while (true) {
      System.out.println("\n=== Chef Menu ===");
      System.out.println("1. View Pending Items");
      System.out.println("2. Mark Item as Prepared");
      System.out.println("3. Exit");

      System.out.print("Enter the choice: ");

      int choice = scanner.nextInt();

      switch (choice) {
        case 1:
          viewPendingItems();
          break;
        case 2:
          markItemPrepared();
          break;
        case 3:
          return;
        default:
          System.out.println("Invalid choice.");
      }
    }
  }

  private void viewPendingItems() {
    List<OrderItem> pending = chefService.getPendingItems();
    if (pending.isEmpty()) {
      System.out.println("No pending items right now!");
      return;
    }


    printPendingOrderItems(pending);
  }

  private void printPendingOrderItems(List<OrderItem> pendingItems){
    System.out.println("\n--- Pending Items ---");
    for (int i = 0; i < pendingItems.size(); i++) {
      OrderItem item = pendingItems.get(i);
      System.out.println(i + ". OrderId: " + item.getOrderId() +
              " | FoodId: " + item.getFoodItemId() +
              " | Qty: " + item.getQuantity() +
              " | Status: " + item.getItemStatus());
    }
  }

  private void markItemPrepared() {
    List<OrderItem> pending = chefService.getPendingItems();
    if (pending.isEmpty()) {
      System.out.println(" No items to mark as prepared.");
      return;
    }
    printPendingOrderItems(pending);

    System.out.print("Enter item index to mark prepared: ");
    int idx = scanner.nextInt();

    if (idx < 0 || idx >= pending.size()) {
      System.out.println(" Invalid index.");
      return;
    }

    OrderItem item = pending.get(idx);
    System.out.println(item);
    boolean updated = chefService.updateOrderItemStatus(item.getOrderId(), item.getFoodItemId(), ItemStatus.PREPARED);

    if (updated) {
      System.out.println(" Item from Order " + item.getOrderId() + " marked as PREPARED.");
    } else {
      System.out.println("Failed to update item.");
    }
  }
}

