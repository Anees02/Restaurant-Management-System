package tech.zeta.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
