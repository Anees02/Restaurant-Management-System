package tech.zeta.service;

import tech.zeta.model.FoodItem;
import tech.zeta.repository.FoodItemRepository;
import tech.zeta.repository.OrderRepository;

import java.time.LocalDate;

/**
 * Service class for admin operations.
 * Handles CRUD operations on food items and generates daily sales reports.
 */
public class AdminService {
  private final OrderRepository orderRepository;
  private final FoodItemRepository foodItemRepository;
  public AdminService(){
    orderRepository = OrderRepository.getInstance();
    foodItemRepository = FoodItemRepository.getInstance();
  }

  /**
   * Adds a new food item to the system.
   *
   * @param foodItem the FoodItem object to add
   * @return true if the item is added successfully, false otherwise
   */
  public boolean addFoodItem(FoodItem foodItem){
    return foodItemRepository.addFoodItem(foodItem);
  }

  /**
   * Updates an existing food item’s price and availability status.
   *
   * @param foodId the ID of the food item
   * @param newPrice the new price to set
   * @param isAvailable the new availability status
   * @return true if the update is successful, false otherwise
   */
  public boolean updateFoodItem(int foodId, double newPrice, boolean isAvailable){
    return foodItemRepository.updateFoodItem(foodId, newPrice, isAvailable);
  }

  /**
   * Removes a food item from the system.
   *
   * @param foodId the ID of the food item to remove
   * @return true if the removal is successful, false otherwise
   */
  public boolean removeFoodItem(int foodId){
    return foodItemRepository.removeFoodItem(foodId);
  }

  /**
   * Retrieves the total sales for a specific date.
   *
   * @param date the date to generate the sales report for
   * @return the total sales amount for the given date
   */
  public double getDailySalesReport(LocalDate date){
    return orderRepository.getTotalAmountOnDate(date);
  }

}
