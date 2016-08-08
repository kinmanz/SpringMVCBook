package App.service;

import App.domain.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();

    Product getProductById(String productID);
}
