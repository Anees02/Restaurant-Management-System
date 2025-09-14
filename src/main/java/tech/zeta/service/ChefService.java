package tech.zeta.service;

import tech.zeta.model.OrderItem;
import tech.zeta.utils.enums.ItemStatus;
import tech.zeta.repository.OrderRepository;

import java.util.List;

/**
 * Service class for chef-related operations.
 * Handles retrieving pending order items and updating their preparation status.
 * Typically used to track and manage items that need to be prepared in the kitchen.
 */
public class ChefService {
  private OrderRepository orderRepository;
  public ChefService(){
    orderRepository = OrderRepository.getInstance();
  }

  /**
   * Updates the status of a specific order item.
   *
   * @param orderId the ID of the order
   * @param foodId the ID of the food item
   * @param itemStatus the new status to set for the item
   * @return true if the status is updated successfully, false otherwise
   */
  public boolean updateOrderItemStatus(int orderId, int foodId, ItemStatus itemStatus) {
    return orderRepository.updateStatus(orderId,foodId,itemStatus);
  }

  /**
   * Retrieves all order items that are currently pending.
   *
   * @return a list of OrderItem objects with status PENDING
   */
  public List<OrderItem> getPendingItems(){
    return orderRepository.getPendingItems();
  }


}
