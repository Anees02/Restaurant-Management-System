package tech.zeta.service;

import tech.zeta.entity.OrderItem;
import tech.zeta.entity.enums.ItemStatus;
import tech.zeta.repository.OrderRepository;

import java.util.List;

public class ChefService {
  private OrderRepository orderRepository;
  public ChefService(){
    orderRepository = OrderRepository.getInstance();
  }

  public boolean updateOrderItemStatus(int orderId, int foodId, ItemStatus itemStatus) {
    return orderRepository.updateStatus(orderId,foodId,itemStatus);
  }

  public List<OrderItem> getPendingItems(){
    return orderRepository.getPendingItems();
  }


}
