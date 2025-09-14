package tech.zeta.repository;


import org.junit.jupiter.api.*;
import tech.zeta.model.Order;
import tech.zeta.model.OrderItem;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderRepositoryTest {

  private static OrderRepository repo;

  @BeforeAll
  static void setup() {
    repo = OrderRepository.getInstance();
  }

  @AfterAll
  static void tearDown() {
    repo.closeConnection();
  }

  @Test
  void testGiveOrderDetails() {
    int existingOrderId = 7;
    Order order = repo.giveOrderDetails(existingOrderId);

    assertNotNull(order, "Order should not be null for an existing orderId");
    assertEquals(existingOrderId, order.getOrderId(), "OrderId must match");
    assertNotNull(order.getPaymentStatus(), "Payment status should not be null");
    assertTrue(order.getTotalAmount() >= 0, "Total amount should be non-negative");

    // Items check
    assertNotNull(order.getItems(), "Order items should not be null");
  }

  @Test
  void testGetTotalAmountOnDate() {
    double total = repo.getTotalAmountOnDate(LocalDate.now());
    assertTrue(total >= 0, "Total sales should be non-negative");
  }
}
