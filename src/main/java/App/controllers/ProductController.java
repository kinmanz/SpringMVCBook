package App.controllers;


import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import App.domain.Product;
import App.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    //    @RequestMapping(value = "/type/{type}", method = RequestMethod.GET)
    @RequestMapping("/{category}")
    public String getProductsByCategory(Model model,
                                        @PathVariable("category") String productCategory) {
        model.addAttribute("products",
                productService.getProductsByCategory(productCategory));
        return "products";
    }

    @RequestMapping("/filter/{ByCriteria}")
    public String getProductsByFilter(@MatrixVariable(pathVar = "ByCriteria")
                                              Map<String, List<String>> filterParams, Model model) {
        model.addAttribute("products",
                productService.getProductsByFilter(filterParams));
        return "products";
    }

    //    http://localhost:8080/webstore/products/tablet/price;low=200;high=400?manufacturer="Google"
    @RequestMapping("/{category}/{price}")
    public String filterProducts(@RequestParam String manufacturer,
                                 @PathVariable("category") String productCategory,
                                 @MatrixVariable(value = "low", pathVar = "price") BigDecimal lowPrice,
                                 @MatrixVariable(value = "high", pathVar = "price") BigDecimal highPrice,
                                 Model model) {
        Map<String, List<String>> filterMap = new HashMap<>();
        filterMap.put("brand", Collections.singletonList(manufacturer));
        filterMap.put("category", Collections.singletonList(productCategory));
        model.addAttribute("products",
                productService.getProductPriceFilter(
                        productService.getProductsByFilter(filterMap).stream().collect(Collectors.toList()),
                        lowPrice,
                        highPrice)
        );
        return "products";
    }

    @RequestMapping("/all")
    public String allProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products";
    }

    //    also can just specify @RequestParam id but request should be ?id = ...
    @RequestMapping("/product")
    public String getProductById(@RequestParam("id") String productId, Model model) {
        model.addAttribute("product", productService.getProductById(productId));
        return "product";
    }

    @InitBinder
    public void initialiseBinder(WebDataBinder binder) {
        binder.setDisallowedFields("unitsInOrder", "discontinued");
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String getAddNewProductForm(Model model) {
        Product newProduct = new Product();
        newProduct.setName("New Product Name");
        model.addAttribute("newProduct", newProduct);
        return "addProduct";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddNewProductForm(@ModelAttribute("newProduct")
                                                   Product newProduct,
                                           BindingResult result) {
        String[] suppressedFields = result.getSuppressedFields();
        if (suppressedFields.length > 0) {
            throw new RuntimeException("Attempting to bind disallowed fields: "
                    + StringUtils.arrayToCommaDelimitedString(suppressedFields));
        }
        productService.addProduct(newProduct);
        return "redirect:/products";
    }

    @RequestMapping
    public String list(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products";
    }
}