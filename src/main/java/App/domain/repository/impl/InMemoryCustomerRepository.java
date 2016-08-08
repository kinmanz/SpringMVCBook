package App.domain.repository.impl;

import App.domain.Customer;
import App.domain.repository.CustomerRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryCustomerRepository implements CustomerRepository {

    List<Customer> listOfCustomers = new ArrayList<>();

    public InMemoryCustomerRepository() {

        Customer JohnDoe = new Customer("C122", "John Elizabet Doe", "NY River 10A River Street");
        Customer AlterMac = new Customer("C123", "Alter Mackintosh Doe", "Ch Street 202 b");

        listOfCustomers.add(JohnDoe);
        listOfCustomers.add(AlterMac);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return listOfCustomers;
    }

    @Override
    public Customer getCustomerById(String customerID) {
        Customer customerById = null;
        for (Customer customer : listOfCustomers) {
            if (customer != null && customer.getCustomerId() != null &&
                    customer.getCustomerId().equals(customerID)) {
                customerById = customer;
                break;
            }
        }
        if (customerById == null) {
            throw new IllegalArgumentException("No products found with the " +
                    "product id: " + customerID);
        }

        return customerById;
    }
}
