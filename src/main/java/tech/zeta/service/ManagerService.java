package tech.zeta.service;

import lombok.extern.slf4j.Slf4j;
import tech.zeta.model.Order;
import tech.zeta.model.OrderItem;
import tech.zeta.utils.enums.ItemStatus;
import tech.zeta.utils.enums.PaymentStatus;
import tech.zeta.utils.enums.TableStatus;
import tech.zeta.repository.OrderRepository;
import tech.zeta.repository.TableRepository;

import java.util.List;


/**
 * Service class for manager-related operations.
 * Handles bill generation, payment processing,
 * retrieving order IDs for tables, and marking tables as available.
 */
@Slf4j
public class ManagerService {
  private final OrderRepository orderRepository;
  private final TableRepository tableRepository;
  public  ManagerService(){
    orderRepository = OrderRepository.getInstance();
    tableRepository = TableRepository.getInstance();
  }

  /**
   * Generates the bill for a specific order by summing the prices of all prepared items.
   * Updates the total amount in the database.
   *
   * @param orderId the ID of the order
   * @return the updated Order object with total amount if successful, otherwise null
   */
  public Order generateBill(int orderId){

    List<OrderItem> orderItemList = orderRepository.giveAllOrderItems(orderId);
    double totalAmount = orderItemList.stream().filter(a -> a.getItemStatus() == ItemStatus.PREPARED).mapToDouble(OrderItem::getPrice).sum();
    if(orderRepository.addTotalAmount(orderId,totalAmount)){
      return orderRepository.giveOrderDetails(orderId);
    }

    log.error("Bill generate fail with order id:  {}", orderId);
    return null;
  }


  /**
   * Marks the payment of an order as completed.
   *
   * @param orderId the ID of the order
   * @return true if payment status is updated successfully, false otherwise
   */
  public boolean makePayment(int orderId){
    return orderRepository.makePayment(orderId, PaymentStatus.COMPLETED);
  }

  /**
   * Retrieves the most recent pending order ID for a specific table.
   *
   * @param tableId the ID of the table
   * @return the order ID if found, otherwise null
   */
  public Integer getOrderIdForTable(int tableId) {
    return orderRepository.getOrderIdByTableId(tableId);
  }

  /**
   * Marks a table as available.
   *
   * @param tableId the ID of the table
   * @return true if the table status is updated successfully, false otherwise
   */
  public boolean makeTableAvailable(int tableId){
    return tableRepository.updateTableStatus(tableId, TableStatus.AVAILABLE,0);
  }

}
