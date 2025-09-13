package tech.zeta.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.zeta.entity.enums.TableStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Table {
  private int tableId;
  private int tableCapacity;
  private TableStatus tableStatus;
  private int customerId; // nullable when not booked
  private LocalDateTime bookingTime; // when the table was booked
}
