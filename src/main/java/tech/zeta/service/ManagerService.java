package tech.zeta.service;

import lombok.extern.slf4j.Slf4j;
import tech.zeta.entity.Order;
import tech.zeta.entity.OrderItem;
import tech.zeta.entity.enums.ItemStatus;
import tech.zeta.entity.enums.PaymentStatus;
import tech.zeta.repository.OrderRepository;

import java.util.List;

@Slf4j
public class ManagerService {
  private final OrderRepository orderRepository;
  public  ManagerService(){
    orderRepository = OrderRepository.getInstance();
  }

  public Order generateBill(int orderId){

    List<OrderItem> orderItemList = orderRepository.giveAllOrderItems(orderId);
    double totalAmount = orderItemList.stream().filter(a -> a.getItemStatus() == ItemStatus.PREPARED).mapToDouble(OrderItem::getPrice).sum();
    if(orderRepository.addTotalAmount(orderId,totalAmount)){
      return orderRepository.giveOrderDetails(orderId);
    }

    log.error("Bill generate fail with order id:  {}", orderId);
    return null;
  }

  public boolean makePayment(int orderId){
    return orderRepository.makePayment(orderId, PaymentStatus.COMPLETED);
  }
  public Integer getOrderIdForTable(int tableId) {
    return orderRepository.getOrderIdByTableId(tableId);
  }

}
