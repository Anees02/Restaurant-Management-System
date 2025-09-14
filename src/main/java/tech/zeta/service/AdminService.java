package tech.zeta.service;

import tech.zeta.entity.FoodItem;
import tech.zeta.repository.FoodItemRepository;
import tech.zeta.repository.OrderRepository;

import java.time.LocalDate;
import java.util.Date;

public class AdminService {
  private final OrderRepository orderRepository;
  private final FoodItemRepository foodItemRepository;
  public AdminService(){
    orderRepository = OrderRepository.getInstance();
    foodItemRepository = FoodItemRepository.getInstance();
  }

  public boolean addFoodItem(FoodItem foodItem){
    return foodItemRepository.addFoodItem(foodItem);
  }

  public boolean updateFoodItem(int foodId, double newPrice, boolean isAvailable){
    return foodItemRepository.updateFoodItem(foodId, newPrice, isAvailable);
  }
  public boolean removeFoodItem(int foodId){
    return foodItemRepository.removeFoodItem(foodId);
  }

  public double getDailySalesReport(LocalDate date){
    return orderRepository.getTotalAmountOnDate(date);
  }

}
