package ru.oz.demostatemachine.app.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.oz.demostatemachine.common.lifecycle.LifecycleService;
import ru.oz.demostatemachine.common.usecases.impl.DoAction;
import ru.oz.demostatemachine.common.usecases.impl.GetStatus;
import ru.oz.demostatemachine.consumer.infrastructure.persistence.ConsumerCardRepository;
import ru.oz.demostatemachine.logistic.infrastructure.persistence.LogisticCardRepository;
import ru.oz.demostatemachine.product.infrastructure.persistence.ProductRepository;
import ru.oz.demostatemachine.product.interfaces.rest.ProductImportResource;
import ru.oz.demostatemachine.product.interfaces.rest.ProductResource;
import ru.oz.demostatemachine.product.model.Product;
import ru.oz.demostatemachine.product.model.ProductStatus.ProductActionIds;
import ru.oz.demostatemachine.product.usecase.ImportProductDataUseCase;
import ru.oz.demostatemachine.product.usecase.impl.ImportProductData;

/**
 * ProductRestConfig.
 *
 * @author Igor_Ozol
 */
@Configuration
public class ProductRestConfig {
    @Bean
    public DoAction<Product, ProductActionIds> productDoActionUseCase(
            ProductRepository repository,
            LifecycleService<Product> lifecycleService) {
        return new DoAction<>(repository, lifecycleService);
    }

    @Bean
    public GetStatus<Product> productGetStatusUseCase(ProductRepository repository) {
        return new GetStatus<>(repository);
    }

    @Bean
    public ProductResource productDoActionResource(DoAction<Product, ProductActionIds> productDoActionUseCase,
                                                   GetStatus<Product> getStatusUseCase) {
        return new ProductResource(productDoActionUseCase, getStatusUseCase);
    }

    @Bean
    public ImportProductDataUseCase importProductDataUseCase(ConsumerCardRepository consumerCardRepository,
                                                             LogisticCardRepository logisticCardRepository,
                                                             ProductRepository productRepository) {
        return new ImportProductData(consumerCardRepository, logisticCardRepository, productRepository);
    }

    @Bean
    public ProductImportResource productImportResource(ImportProductDataUseCase importProductDataUseCase) {
        return new ProductImportResource(importProductDataUseCase);
    }
}
