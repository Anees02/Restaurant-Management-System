package tech.zeta.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import tech.zeta.exception.CustomerNotFoundException;
import tech.zeta.model.Customer;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerRepositoryTest {

  private CustomerRepository customerRepository;

  @BeforeAll
  void init() {
    customerRepository = CustomerRepository.getInstance();
  }


  @Test
  void testAddCustomer_success() {
    Customer customer = new Customer(
            0,
            "TestUser",
            "9999999999",
            "testuser@example.com",
            "secret"
    );
    boolean result = customerRepository.addCustomer(customer);
    assertTrue(result);
  }

  @Test
  void testIsUserAlreadyExists_true() {
    // First insert a customer
    Customer customer = new Customer(
            0,
            "Alice",
            "8888888888",
            "alice@example.com",
            "pwd"
    );
    customerRepository.addCustomer(customer);

    boolean exists = customerRepository.isUserAlreadyExists("alice@example.com");
    assertTrue(exists);
  }

  @Test
  void testIsUserAlreadyExists_false() {
    boolean exists = customerRepository.isUserAlreadyExists("ghost@example.com");
    assertFalse(exists);
  }

  @Test
  void testGetCustomerByEmail_success() throws Exception {
    Customer customer = new Customer(
            0,
            "Bob",
            "7777777777",
            "bob@example.com",
            "1234"
    );
    customerRepository.addCustomer(customer);

    Customer fetched = customerRepository.getCustomerByEmail("bob@example.com");
    assertNotNull(fetched);
    assertEquals("Bob", fetched.getCustomerName());
    assertEquals("bob@example.com", fetched.getCustomerMailId());
  }

  @Test
  void testGetCustomerByEmail_notFound() {
    assertThrows(CustomerNotFoundException.class, () ->
            customerRepository.getCustomerByEmail("missing@example.com")
    );
  }
}