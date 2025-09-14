package tech.zeta.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.zeta.utils.enums.ItemStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
  private int orderId;
  private int foodItemId;
  private int quantity;
  private int quantityToPrepare;
  private double price;
  private ItemStatus itemStatus;


}
