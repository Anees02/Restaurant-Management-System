package tech.zeta.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.PreparedStatement;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
  private int customerId;
  private String customerName;
  private String contactNumber;
  private String customerMailId;
  private String password;
}
