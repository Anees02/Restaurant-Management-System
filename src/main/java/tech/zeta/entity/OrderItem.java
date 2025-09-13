package tech.zeta.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.zeta.entity.enums.ItemStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
  private int foodItemId;
  private int orderId;
  private int quantity;
  private int quantityToPrepare;
  private double price;
  private ItemStatus itemStatus;


}
