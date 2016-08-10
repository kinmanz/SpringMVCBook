package App.service.impl;

import App.domain.Product;
import App.domain.repository.ProductRepository;
import App.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.getAllProducts();
    }

    @Override
    public Product getProductById(String productID) {
        return productRepository.getProductById(productID);
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.getProductsByCategory(category);
    }

    @Override
    public Set<Product> getProductsByFilter(Map<String, List<String>> filterParams) {
        return productRepository.getProductsByFilter(filterParams);
    }

    @Override
    public List<Product> getProductsByManufacturer(String manufacturer) {
        return productRepository.getProductsByManufacturer(manufacturer);
    }

    @Override
    public List<Product> getProductPriceFilter(List<Product> products,
                                               BigDecimal low, BigDecimal high) {
        return products.stream()
                .filter(product ->
                        product.getUnitPrice().compareTo(low) > 0 &&
                                high.compareTo(product.getUnitPrice()) > 0)
                .collect(Collectors.toList());
    }

    @Override
    public void addProduct(Product product) {
        productRepository.addProduct(product);
    }
}
