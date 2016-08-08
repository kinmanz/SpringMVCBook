package App.domain.repository;

import App.domain.Customer;

import java.util.List;


public interface CustomerRepository {

    List<Customer> getAllCustomers();

    Customer getCustomerById(String customerID);

}
