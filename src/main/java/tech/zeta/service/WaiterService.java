package tech.zeta.service;

import tech.zeta.entity.FoodItem;
import tech.zeta.entity.enums.TableStatus;
import tech.zeta.repository.OrderRepository;
import tech.zeta.repository.TableRepository;

public class WaiterService {
  private TableRepository tableRepository;
  private OrderRepository orderRepository;
  public WaiterService(){
    orderRepository = OrderRepository.getInstance();
    tableRepository = TableRepository.getInstance();
  }

  public int createOrder(int tableId, int customerId){
    if(tableRepository.updateTableStatus(tableId, TableStatus.OCCUPIED,customerId)){
      return orderRepository.createOrder(tableId, customerId);
    }
    return -1;
  }

  public boolean addOrderItems(int orderId, int foodItemId, int quantity, int unitPrice){
    if(!orderRepository.isItemAlreadyPresent(orderId, foodItemId)){
      return orderRepository.addItemToOrder(orderId, foodItemId, quantity, unitPrice);
    }
    else{
      return orderRepository.updateQuantity(orderId, foodItemId, quantity, unitPrice);
    }
  }

}
