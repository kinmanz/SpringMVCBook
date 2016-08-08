package App.domain.repository;

import App.domain.Product;

import java.util.List;

public interface ProductRepository {

    List<Product> getAllProducts();

    Product getProductById(String productID);

}
