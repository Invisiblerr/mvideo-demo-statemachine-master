package ru.oz.demostatemachine.product.interfaces.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.oz.demostatemachine.app.rest.BaseResource;
import ru.oz.demostatemachine.common.usecases.impl.DoAction;
import ru.oz.demostatemachine.common.usecases.impl.GetStatus;
import ru.oz.demostatemachine.product.model.Product;
import ru.oz.demostatemachine.product.model.ProductStatus.ProductActionIds;

/**
 * ProductResource.
 *
 * @author Igor_Ozol
 */
@RestController
@RequestMapping("/product")
public class ProductResource extends BaseResource<Product, ProductActionIds> {

    public ProductResource(DoAction<Product, ProductActionIds> doAction,
                           GetStatus<Product> getStatus) {
        super(doAction, getStatus);
    }
}
