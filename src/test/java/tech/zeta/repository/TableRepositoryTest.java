package tech.zeta.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tech.zeta.utils.enums.TableStatus;

import static org.junit.jupiter.api.Assertions.*;

class TableRepositoryTest {
  private static TableRepository repo;

  @BeforeAll
  static void init() {
    repo = TableRepository.getInstance();
  }

  @AfterAll
  static void cleanup() {
    repo.closeConnection();
  }

  @Test
  void testGiveTableStatus() {
    int testTableId = 1;
    TableStatus status = repo.giveTableStatus(testTableId);

    assertNotNull(status, "Table status should not be null for a valid tableId");
    assertTrue(
            status == TableStatus.AVAILABLE || status == TableStatus.BOOKED || status == TableStatus.OCCUPIED,
            "Status should be one of the enum values"
    );
  }

}