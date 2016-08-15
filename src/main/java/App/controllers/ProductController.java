package App.controllers;


import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import App.domain.Product;
import App.exceptions.NoProductsFoundUnderCategoryException;
import App.exceptions.ProductNotFoundException;
import App.service.ProductService;
import App.validators.ProductValidator;
import App.validators.UnitsInStockValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductValidator productValidator;

    //    @RequestMapping(value = "/type/{type}", method = RequestMethod.GET)
    @RequestMapping("/{category}")
    public String getProductsByCategory(Model model,
                                        @PathVariable("category") String productCategory) {
        List<Product> products = productService.getProductsByCategory(productCategory);

        if (products == null || products.isEmpty()) {
            throw new NoProductsFoundUnderCategoryException();
        }

        model.addAttribute("products", products);
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
        binder.setValidator(productValidator);
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String getAddNewProductForm(Model model) {
        Product newProduct = new Product();
        newProduct.setName("New Product Name");
        model.addAttribute("newProduct", newProduct);
        return "addProduct";
    }


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddNewProductForm(@ModelAttribute("productToBeAdded") @Valid
                                                   Product productToBeAdded,
                                           BindingResult result,
                                           HttpServletRequest request,
                                           Model model) {

        /*After validating the incoming form
        bean (productToBeAdded), Spring will store the results in the result object*/
        if(result.hasErrors()) {
            model.addAttribute("newProduct", new Product());

            return "addProduct";
        }

        String[] suppressedFields = result.getSuppressedFields();
        if (suppressedFields.length > 0) {
            throw new RuntimeException("Attempting to bind disallowed fields: "
                    + StringUtils.arrayToCommaDelimitedString(suppressedFields));
        }

        MultipartFile productImage = productToBeAdded.getProductImage();
        String rootDirectory = request.getSession().getServletContext().getRealPath("/");
        if (productImage != null && !productImage.isEmpty()) {
            try {
                productImage.transferTo(new File(rootDirectory + "resources\\images\\" +
                        productToBeAdded.getProductId() + ".jpg"));
            } catch (Exception e) {
                throw new RuntimeException("Product Image saving failed", e);
            }
        }

        productService.addProduct(productToBeAdded);
        return "redirect:/products";
    }

    @RequestMapping
    public String list(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products";
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ModelAndView handleError(HttpServletRequest req,
                                    ProductNotFoundException exception) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("invalidProductId", exception.getProductId());
        mav.addObject("exception", exception);
        mav.addObject("url", req.getRequestURL() + "?" + req.getQueryString());
        mav.setViewName("productNotFound");
        return mav;
    }

    @RequestMapping("/invalidPromoCode")
    public String invalidPromoCode() {
        return "invalidPromoCode";
    }

}