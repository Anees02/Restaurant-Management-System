package tech.zeta.repository;

import lombok.extern.slf4j.Slf4j;
import tech.zeta.model.Order;
import tech.zeta.model.OrderItem;
import tech.zeta.utils.enums.ItemStatus;
import tech.zeta.utils.enums.PaymentStatus;
import tech.zeta.utils.DB.PostgresDBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class for managing orders and order items.
 * Provides methods to create orders, add/update order items,
 * update item status, retrieve order details, calculate total sales,
 * and handle payments.
 * Singleton pattern is used to provide a single instance.
 */
@Slf4j
public class OrderRepository {
  private static OrderRepository orderRepository;
  private Connection connection;
  private OrderRepository(){
    connection = new PostgresDBConnection().getConnection();
  }


  /**
   * Retrieves the singleton instance of OrderRepository.
   *
   * @return the single instance of OrderRepository
   */
  public static OrderRepository getInstance(){
    if(orderRepository == null){
      synchronized (OrderRepository.class){
        if(orderRepository == null) orderRepository = new OrderRepository();
      }
    }
    return orderRepository;
  }



  /**
   * Creates a new order for a specific customer at a given table.
   *
   * @param customerId the ID of the customer placing the order
   * @param tableId the ID of the table for the order
   * @return the generated order ID if successful, otherwise -1
   */
  public int createOrder(int customerId, int tableId) {
    String sql = "insert into orders(tableId, customerId, paymentStatus, totalAmount) values (?, ?, ?, ?) returning orderId";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setInt(1, tableId);
      ps.setInt(2, customerId);
      ps.setString(3, PaymentStatus.PENDING.name());
      ps.setDouble(4, 0.0);

      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        int orderId = rs.getInt("orderId");
        log.info("Created order {} for customer {}", orderId, customerId);
        return orderId;
      }
    } catch (SQLException  e) {
      log.error("Error creating order: {}", e.getMessage());
    }
    return -1;
  }

  /**
   * Adds a food item to an existing order.
   *
   * @param orderId the ID of the order
   * @param foodItemId the ID of the food item
   * @param quantity the quantity to add
   * @param unitPrice the price of a single unit of the food item
   * @return true if the item is added successfully, false otherwise
   */
  public boolean addItemToOrder(int orderId, int foodItemId, int quantity, double unitPrice) {
    String queryToAddItem = "insert into OrderItem (orderId, foodId, quantity,quantityToPrepare, price) values (?, ?, ?, ?,?)";

    try (PreparedStatement ps = connection.prepareStatement(queryToAddItem)) {
      ps.setInt(1, orderId);
      ps.setInt(2, foodItemId);
      ps.setInt(3, quantity);
      ps.setInt(4,quantity);
      ps.setDouble(5, unitPrice*quantity);

      int rows = ps.executeUpdate();
      return rows > 0;
    } catch (SQLException sqlException) {
      log.error("Error adding item to orderId {}: {}", orderId, sqlException.getMessage());
    }
    return false;
  }

  /**
   * Updates the quantity of a food item in an order.
   *
   * @param orderId the ID of the order
   * @param foodItemId the ID of the food item
   * @param addedQuantity the quantity to add
   * @param unitPrice the price of a single unit of the food item
   * @return true if the update is successful, false otherwise
   */
  public boolean updateQuantity(int orderId, int foodItemId, int addedQuantity, double unitPrice){
    String sql = "update OrderItem set quantity = quantity + ?, quantityToPrepare = quantityToPrepare + ?, price = price + ? " +
                  "where orderId = ? and foodId = ? and itemStatus = 'PENDING'";

    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setInt(1, addedQuantity);                   // increment total quantity
      ps.setInt(2, addedQuantity);                   // increment quantityToPrepare
      ps.setDouble(3, unitPrice * addedQuantity);    // update total price
      ps.setInt(4, orderId);
      ps.setInt(5, foodItemId);

      int rowsAffected = ps.executeUpdate();
      return rowsAffected > 0;

    } catch (SQLException e) {
      log.error("Error updating quantity for orderId {} and item {}: {}", orderId, foodItemId, e.getMessage());
      return false;
    }
  }

  /**
   * Updates the status of a specific food item in an order.
   *
   * @param orderId the ID of the order
   * @param foodItemId the ID of the food item
   * @param itemStatus the new status of the item
   * @return true if the status is updated successfully, false otherwise
   */
  public boolean updateStatus(int orderId,int foodItemId, ItemStatus itemStatus){
    String queryToUpdateStatus = "update OrderItem set quantityToPrepare = 0, itemStatus = ? where orderId = ? and foodId = ?";

    try(PreparedStatement preparedStatement = connection.prepareStatement(queryToUpdateStatus)){
      preparedStatement.setString(1,itemStatus.name());
      preparedStatement.setInt(2, orderId);
      preparedStatement.setInt(3, foodItemId);

      int rowAffected = preparedStatement.executeUpdate();
      return rowAffected > 0;
    } catch (SQLException sqlException) {
      log.error("Error updating status for foodItemId {}: {}", foodItemId, sqlException.getMessage());
    }
    return false;
  }

  /**
   * Checks if a specific food item is already present in an order.
   *
   * @param orderId the ID of the order
   * @param foodItemId the ID of the food item
   * @return true if the item exists in the order, false otherwise
   */
  public boolean isItemAlreadyPresent(int orderId, int  foodItemId){
    String queryToCheckItem = "select 1 from OrderItem where orderId = ? and foodId = ?";

    try (PreparedStatement ps = connection.prepareStatement(queryToCheckItem)) {

      ps.setInt(1,orderId);
      ps.setInt(2, foodItemId);
      try(ResultSet resultSet = ps.executeQuery()){
        return resultSet.next();
      }
    } catch (SQLException sqlException) {
      log.error("Error Checking an  item in orderItems ");
    }
    return false;
  }

  /**
   * Retrieves all items of a specific order.
   *
   * @param orderId the ID of the order
   * @return a list of OrderItem objects belonging to the order, or null if an error occurs
   */
  public List<OrderItem> giveAllOrderItems(int orderId){
    String queryForOrderItems = "select * from OrderItem where orderId = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(queryForOrderItems)) {
      List<OrderItem> items = new ArrayList<>();
      preparedStatement.setInt(1, orderId);
      ResultSet rsItems = preparedStatement.executeQuery();

      while (rsItems.next()) {
        items.add(new OrderItem(
                rsItems.getInt("orderId"),
                rsItems.getInt("foodId"),
                rsItems.getInt("quantity"),
                rsItems.getInt("quantitytoprepare"),
                rsItems.getDouble("price"),
                ItemStatus.valueOf(rsItems.getString("itemStatus"))
        ));
      }
      return items;
    } catch (SQLException sqlException) {
      log.error(sqlException.getMessage());
    }
    return null;
  }

  /**
   * Updates the total amount of an order.
   *
   * @param orderId the ID of the order
   * @param totalAmount the new total amount
   * @return true if the update is successful, false otherwise
   */
  public boolean addTotalAmount(int orderId, double totalAmount){
    String query = "update orders set totalAmount = ? where orderId = ?";

    try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
      preparedStatement.setDouble(1,totalAmount);
      preparedStatement.setInt(2, orderId);

      int rowAffected = preparedStatement.executeUpdate();

      return rowAffected > 0;
    }
    catch (SQLException sqlException){
      log.error(sqlException.getMessage());
    }
    return false;
  }

  /**
   * Updates the payment status of an order.
   *
   * @param orderId the ID of the order
   * @param paymentStatus the new payment status
   * @return true if the payment status is updated successfully, false otherwise
   */
  public boolean makePayment(int orderId, PaymentStatus paymentStatus){
    String query = "update orders set paymentStatus = ? where orderId = ?";

    try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
      preparedStatement.setString(1, paymentStatus.name());
      preparedStatement.setInt(2, orderId);

      int rowAffected = preparedStatement.executeUpdate();

      return rowAffected > 0;
    }
    catch (SQLException sqlException){
      log.error(sqlException.getMessage());
    }
    return false;
  }

  /**
   * Retrieves the details of a specific order, including its items.
   *
   * @param orderId the ID of the order
   * @return the Order object with all details, or null if not found
   */
  public Order giveOrderDetails(int orderId){
    String query = "select * from orders where orderId = ?;";
    try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
      preparedStatement.setInt(1,orderId);
      ResultSet rs = preparedStatement.executeQuery();

      Order order = new Order();
      if(rs.next()){
        order.setOrderId(rs.getInt("orderId"));
        order.setTableId(rs.getInt("tableId"));
        order.setCustomerId(rs.getInt("customerId"));
        Timestamp ts = rs.getTimestamp("orderTime");
        if (ts != null) {
          order.setOrderTime(ts.toLocalDateTime());
        }

        order.setPaymentStatus(PaymentStatus.valueOf(rs.getString("paymentstatus")));
        order.setTotalAmount(rs.getDouble("totalAmount"));
        List<OrderItem> orderItemList = giveAllOrderItems(orderId);
        order.setItems(orderItemList);
        return order;
      }

    }
    catch (SQLException sqlException){
      log.error(sqlException.getMessage());
    }
    log.warn("The order id {} not found", orderId);
    return null;

  }

  /**
   * Calculates the total sales amount for a specific date.
   *
   * @param date the date for which to calculate total sales
   * @return the total amount of all orders on that date, or 0.0 if no orders exist
   */
  public double getTotalAmountOnDate(LocalDate date) {
    String sql = "SELECT SUM(totalAmount) AS totalSales " +
            "FROM Orders " +
            "WHERE DATE(orderTime) = ?";

    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setDate(1, Date.valueOf(date));

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return rs.getDouble("totalSales");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return 0.0; // in case no sales found
  }

  /**
   * Retrieves all pending items across all orders.
   *
   * @return a list of pending OrderItem objects
   */
  public List<OrderItem> getPendingItems() {
    String sql = "SELECT * FROM OrderItem WHERE itemStatus = 'PENDING'";
    List<OrderItem> pendingItems = new ArrayList<>();

    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        pendingItems.add(new OrderItem(
                rs.getInt("orderId"),
                rs.getInt("foodId"),
                rs.getInt("quantity"),
                rs.getInt("quantityToPrepare"),
                rs.getDouble("price"),
                ItemStatus.valueOf(rs.getString("itemStatus"))
        ));
      }
    } catch (SQLException e) {
      log.error("Error fetching pending items: {}", e.getMessage());
    }
    return pendingItems;
  }


  /**
   * Retrieves the most recent order ID for a specific table with pending payment.
   *
   * @param tableId the ID of the table
   * @return the order ID if found, otherwise null
   */
  public Integer getOrderIdByTableId(int tableId) {
    String sql = "SELECT orderId FROM orders WHERE tableId = ? AND paymentStatus = 'PENDING' ORDER BY orderTime DESC LIMIT 1";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setInt(1, tableId);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        return rs.getInt("orderId");
      }
    } catch (SQLException e) {
      log.error("Error fetching orderId for tableId {}: {}", tableId, e.getMessage());
    }
    return null;
  }

  /**
   * Closes the database connection used by this repository.
   */

  public void closeConnection(){
    try {
      connection.close();
    } catch (SQLException error) {
      log.error("Failed to Close Connection {}",error.getMessage());
    }
  }



}
