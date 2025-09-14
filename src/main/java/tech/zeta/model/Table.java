package tech.zeta.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.zeta.utils.enums.TableStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Table {
  private int tableId;
  private int tableCapacity;
  private TableStatus tableStatus;
  private int customerId;
  private LocalDateTime bookingTime;
}
