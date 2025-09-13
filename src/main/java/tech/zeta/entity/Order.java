package tech.zeta.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.zeta.entity.enums.PaymentStatus;

import java.time.LocalDateTime;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
  private int orderId;
  private int tableId;
  private int customerId;
  private LocalDateTime orderTime;
  private PaymentStatus paymentStatus;
  private double totalAmount;
  private List<OrderItem> items;

}
