package tech.zeta.service;

import tech.zeta.utils.enums.TableStatus;
import tech.zeta.repository.OrderRepository;
import tech.zeta.repository.TableRepository;

/**
 * Service class for waiter-related operations.
 * Handles creating orders, adding items to orders,
 * and updating quantities of existing items.
 */
public class WaiterService {
  private TableRepository tableRepository;
  private OrderRepository orderRepository;
  public WaiterService(){
    orderRepository = OrderRepository.getInstance();
    tableRepository = TableRepository.getInstance();
  }

  /**
   * Creates a new order for a specific table and customer.
   * Also updates the table status to OCCUPIED if successful.
   *
   * @param tableId the ID of the table
   * @param customerId the ID of the customer
   * @return the generated order ID if successful, otherwise -1
   */
  public int createOrder(int tableId, int customerId){
    if(tableRepository.updateTableStatus(tableId, TableStatus.OCCUPIED,customerId)){
      return orderRepository.createOrder(tableId, customerId);
    }
    return -1;
  }

  /**
   * Adds a food item to an existing order.
   * If the item already exists in the order, updates its quantity instead.
   *
   * @param orderId the ID of the order
   * @param foodItemId the ID of the food item
   * @param quantity the quantity to add
   * @param unitPrice the price of a single unit of the food item
   * @return true if the item is added or updated successfully, false otherwise
   */
  public boolean addOrderItem(int orderId, int foodItemId, int quantity, double unitPrice){
    if(!orderRepository.isItemAlreadyPresent(orderId, foodItemId)){
      return orderRepository.addItemToOrder(orderId, foodItemId, quantity, unitPrice);
    }
    else{
      return orderRepository.updateQuantity(orderId, foodItemId, quantity, unitPrice);
    }
  }

}
