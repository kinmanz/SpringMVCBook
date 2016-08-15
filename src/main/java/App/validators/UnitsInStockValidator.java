package App.validators;

import java.math.BigDecimal;

import App.domain.Product;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UnitsInStockValidator implements Validator{

    //return true if we can to validate it
    public boolean supports(Class<?> clazz) {
        return Product.class.isAssignableFrom(clazz);
    }
    public void validate(Object target, Errors errors) {
        Product product = (Product) target;
        if(product.getUnitPrice()!= null && new BigDecimal(10000).compareTo(product.getUnitPrice())<=0
                && product.getUnitsInStock() > 99) {
            errors.rejectValue("unitsInStock","App.validators.UnitsInStockValidator.message");
        }
    }
}
