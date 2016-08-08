package App.service.impl;

import App.domain.Customer;
import App.domain.repository.CustomerRepository;
import App.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.getAllCustomers();
    }

    @Override
    public Customer getCustomerById(String customerID) {
        return customerRepository.getCustomerById(customerID);
    }
}
