package tech.zeta.service;

import lombok.extern.slf4j.Slf4j;
import tech.zeta.entity.Customer;
import tech.zeta.repository.CustomerRepository;
import tech.zeta.repository.TableRepository;

@Slf4j
public class CustomerService {
  private CustomerRepository customerRepository;
  public CustomerService(){
    customerRepository = CustomerRepository.getInstance();
  }

  public boolean addCustomer(String name, String contactNumber, String mailId, String password){
    if(!customerRepository.isUserAlreadyExists(mailId)) {
      boolean result = customerRepository.addCustomer(name, contactNumber, mailId,password);
      return result;
    }
    else{
      log.info("user already exists");
    }
    return false;
  }



  public Customer getCustomerByMailId(String mailId){
    return customerRepository.getCustomerByEmail(mailId);
  }

  public Customer getCustomer(String mailId, String password){
    Customer customer = getCustomerByMailId(mailId);
    if(customer != null && password.equals(customer.getPassword())){
      return customer;
    }
    return null;
  }
}
