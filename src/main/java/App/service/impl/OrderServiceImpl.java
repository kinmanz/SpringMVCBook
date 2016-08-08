package App.service.impl;


import App.domain.Product;
import App.domain.repository.ProductRepository;
import App.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductRepository productRepository;

//    Better to do it with @Transaction see this link
// http://docs.spring.io/spring/docs/current/spring-framework-reference/html/transaction.html
//and guide https://spring.io/guides/gs/managing-transactions/
    @Override
    public void processOrder(String productId, long quantity) {
        Product productById = productRepository.getProductById(productId);
        if(productById.getUnitsInStock() < quantity){
            throw new IllegalArgumentException("Out of Stock. Available Units " +
                    "in stock"+ productById.getUnitsInStock());
        }
        productById.setUnitsInStock(productById.getUnitsInStock() -
                quantity);
    }

}