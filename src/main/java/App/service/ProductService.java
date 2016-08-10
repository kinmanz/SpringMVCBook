package App.service;

import App.domain.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ProductService {
    List<Product> getAllProducts();

    Product getProductById(String productID);

    List<Product> getProductsByCategory(String category);

    /** @return all products by specified filter */
    Set<Product> getProductsByFilter(Map<String, List<String>> filterParams);

    List <Product> getProductsByManufacturer(String manufacturer);

    List <Product> getProductPriceFilter(List<Product> products, BigDecimal low, BigDecimal high);

    void addProduct(Product product);
}
