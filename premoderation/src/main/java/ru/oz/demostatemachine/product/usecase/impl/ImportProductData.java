package ru.oz.demostatemachine.product.usecase.impl;

import static ru.oz.demostatemachine.product.usecase.ImportProductDataUseCase.ImportDataError.IMPORT_DATA_ERROR;

import io.vavr.control.Either;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.oz.demostatemachine.consumer.infrastructure.persistence.ConsumerCardRepository;
import ru.oz.demostatemachine.consumer.model.ConsumerCard;
import ru.oz.demostatemachine.logistic.infrastructure.persistence.LogisticCardRepository;
import ru.oz.demostatemachine.logistic.model.LogisticCard;
import ru.oz.demostatemachine.product.infrastructure.persistence.ProductRepository;
import ru.oz.demostatemachine.product.model.Product;
import ru.oz.demostatemachine.product.usecase.ImportProductDataUseCase;

import javax.transaction.Transactional;

/**
 * ImportProductData.
 *
 * @author Igor_Ozol
 */
@Slf4j
@RequiredArgsConstructor
public class ImportProductData implements ImportProductDataUseCase {

    private final ConsumerCardRepository consumerCardRepository;
    private final LogisticCardRepository logisticCardRepository;
    private final ProductRepository productRepository;

    @Transactional
    @Override
    public Either<ImportDataError, Void> execute(ImportDataRequest request) {
        log.debug("execute begin: {}", request);
        return Try.run(() -> createProduct(request))
                .toEither()
                .mapLeft(throwable -> IMPORT_DATA_ERROR)
                .peek(noError -> log.debug("execute end."));
    }

    private void createProduct(ImportDataRequest request) {
        LogisticCard lc = LogisticCard.create();
        logisticCardRepository.save(lc);
        ConsumerCard cc = ConsumerCard.create();
        consumerCardRepository.save(cc);

        Product product = Product.create(cc, lc);
        product.setProductName(request.getProductName());
        productRepository.save(product);
        product.addCpc(cc);
        product.addLpc(lc);
        consumerCardRepository.save(cc);
        logisticCardRepository.save(lc);
    }
}
