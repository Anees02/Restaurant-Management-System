package tech.zeta.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodItem {
  private int foodItemId;
  private String foodName;
  private double price;
  private boolean isAvailable;
}
