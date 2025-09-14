package tech.zeta.repository;

import lombok.extern.slf4j.Slf4j;
import tech.zeta.entity.FoodItem;
import tech.zeta.utils.DB.PostgresDBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class FoodItemRepository {
  private static FoodItemRepository foodItemRepository;
  private Connection connection;
  private FoodItemRepository(){
    connection = new PostgresDBConnection().getConnection();
  }
  public static FoodItemRepository getInstance(){
    if(foodItemRepository == null){
      synchronized (FoodItemRepository.class){
        if(foodItemRepository == null){
          foodItemRepository = new FoodItemRepository();
        }
      }
    }
    return foodItemRepository;
  }

  public List<FoodItem> getAllItems(){
      String query = "SELECT food_id, food_name, price, is_available FROM FoodItem";

    try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      List<FoodItem> list = new ArrayList<>();
      ResultSet rs = preparedStatement.executeQuery();
      while(rs.next()){
        int foodItemId = rs.getInt("food_id");
        String foodName = rs.getString("food_name");
        double price = rs.getDouble("price");
        boolean is_available = rs.getBoolean("is_available");
        list.add(new FoodItem(foodItemId,foodName,price,is_available));
      }

      return list;
    } catch (SQLException sqlException) {
        log.error(sqlException.getMessage());
    }
    return null;
  }

  public FoodItem getFoodItemById(int foodItemId){
    String query = "SELECT food_id, food_name, price, is_available FROM FoodItem WHERE food_id = ?";
    FoodItem food = null;

    try (PreparedStatement ps = connection.prepareStatement(query)) {
      ps.setInt(1, foodItemId);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          food = new FoodItem();
          food.setFoodItemId(rs.getInt("food_id"));
          food.setFoodName(rs.getString("food_name"));
          food.setPrice(rs.getDouble("price"));
          food.setAvailable(rs.getBoolean("is_available"));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return food;
  }

  public boolean addFoodItem(FoodItem foodItem){
    String sql = "INSERT INTO FoodItem (food_name, price, is_available) values (?, ?, ?)";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setString(1, foodItem.getFoodName());
      ps.setDouble(2, foodItem.getPrice());
      ps.setBoolean(3, foodItem.isAvailable());
      int rows = ps.executeUpdate();
      return rows > 0;
    } catch (SQLException error) {
      log.error(error.getMessage());
      return false;
    }
  }

  public boolean updateFoodItem(int foodId, double newPrice, boolean isAvailable){
    String sql = "update FoodItem set price = ?, is_available = ? where food_id = ?";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setDouble(1, newPrice);
      ps.setBoolean(2, isAvailable);
      ps.setInt(3, foodId);
      int rows = ps.executeUpdate();
      return rows > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean removeFoodItem(int foodId) {
    String sql = "delete from FoodItem where food_id = ?";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setInt(1, foodId);
      int rows = ps.executeUpdate();
      return rows > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public void closeConnection(){
    try {
      connection.close();
    } catch (SQLException error) {
      log.error("Failed to Close Connection {}",error.getMessage());
    }
  }
}
