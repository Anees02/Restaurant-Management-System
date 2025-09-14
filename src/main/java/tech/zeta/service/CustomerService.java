package tech.zeta.service;

import lombok.extern.slf4j.Slf4j;
import tech.zeta.model.Customer;
import tech.zeta.exception.CustomerNotFoundException;
import tech.zeta.exception.PasswordIncorrectException;
import tech.zeta.repository.CustomerRepository;

/**
 * Service class for customer-related operations.
 * Handles customer registration, fetching customer details,
 * and validating credentials with password checks.
 */
@Slf4j
public class CustomerService {
  private CustomerRepository customerRepository;
  public CustomerService(){
    customerRepository = CustomerRepository.getInstance();
  }

  /**
   * Adds a new customer if they do not already exist in the system.
   *
   * @param customer the Customer object to add
   * @return true if the customer is successfully added, false if the user already exists or addition fails
   */
  public boolean addCustomer(Customer customer){
    if(!customerRepository.isUserAlreadyExists(customer.getCustomerMailId())) {
      boolean result = customerRepository.addCustomer(customer);
      return result;
    }
    else{
      log.info("user already exists");
    }
    return false;
  }

  /**
   * Retrieves a customer by their email ID.
   *
   * @param mailId the email address of the customer
   * @return the Customer object if found
   * @throws CustomerNotFoundException if no customer exists with the given email
   */
  public Customer getCustomerByMailId(String mailId) throws  CustomerNotFoundException {
    return customerRepository.getCustomerByEmail(mailId);
  }

  /**
   * Retrieves a customer by email and validates the password.
   *
   * @param mailId the email address of the customer
   * @param password the password to validate
   * @return the Customer object if the email exists and password matches
   * @throws CustomerNotFoundException if no customer exists with the given email
   * @throws PasswordIncorrectException if the provided password does not match
   */
  public Customer getCustomer(String mailId, String password) throws CustomerNotFoundException, PasswordIncorrectException {
    Customer customer = getCustomerByMailId(mailId);
    if(customer != null && password.equals(customer.getPassword())){
      return customer;
    }
    throw new PasswordIncorrectException("Incorrect Password!");
  }
}
