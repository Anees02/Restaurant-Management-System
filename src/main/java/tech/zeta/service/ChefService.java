package tech.zeta.service;

import tech.zeta.entity.enums.ItemStatus;
import tech.zeta.repository.OrderRepository;

public class ChefService {
  private OrderRepository orderRepository;
  public ChefService(){
    orderRepository = OrderRepository.getInstance();
  }

  public boolean updateOrderItemStatus(int orderId, int foodId, ItemStatus itemStatus) {
    return orderRepository.updateStatus(orderId,foodId,itemStatus);
  }
}
