package ru.oz.demostatemachine.app.lifecycle;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.oz.demostatemachine.consumer.model.ConsumerCardStatus.ConsumerActionIds.TO_PROCESS;
import static ru.oz.demostatemachine.consumer.model.ConsumerCardStatus.ConsumerActionIds.TO_WORK;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.oz.demostatemachine.app.lifecycle.consumer.ConsumerLifecycleConfig;
import ru.oz.demostatemachine.app.lifecycle.product.ProductLifecycleConfig;
import ru.oz.demostatemachine.common.lifecycle.LifecycleService;
import ru.oz.demostatemachine.consumer.infrastructure.persistence.ConsumerCardRepository;
import ru.oz.demostatemachine.consumer.model.ConsumerCard;
import ru.oz.demostatemachine.consumer.model.ConsumerCardStatus;
import ru.oz.demostatemachine.consumer.model.ConsumerCardStatus.ConsumerActionIds;
import ru.oz.demostatemachine.logistic.infrastructure.persistence.LogisticCardRepository;
import ru.oz.demostatemachine.logistic.model.LogisticCard;
import ru.oz.demostatemachine.logistic.model.LogisticCardStatus;
import ru.oz.demostatemachine.logistic.model.LogisticCardStatus.LogisticActionIds;
import ru.oz.demostatemachine.product.infrastructure.persistence.ProductRepository;
import ru.oz.demostatemachine.product.model.Product;
import ru.oz.demostatemachine.product.model.ProductStatus;

/**
 * SpringLifecycleServiceTest.
 *
 * @author Igor_Ozol
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SpringLifecycleServiceTest.TestConfig.class)
class SpringLifecycleServiceTest {

    @Autowired
    private LifecycleService<Product> productLifecycleService;
    @Autowired
    private LifecycleService<ConsumerCard> consumerProductCardLifecycleService;
    @Autowired
    private LifecycleService<LogisticCard> logisticProductCardLifecycleService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ConsumerCardRepository consumerCardRepository;
    @Autowired
    private LogisticCardRepository logisticCardRepository;

    @Test
    void simpleTest() {
        // given
        Product product = createProductWithPremoderationBlocks();
        Long cpcId = product.getConsumerCardId();
        Long lpcId = product.getLogisticCardId();

        // when/then
        consumerProductCardLifecycleService.executeAction(cpcId, TO_PROCESS);
        assertState(product, ProductStatus.DRAFT, ConsumerCardStatus.NEW, LogisticCardStatus.NEW);

        consumerProductCardLifecycleService.executeAction(cpcId, ConsumerActionIds.TO_WORK);
        assertState(product, ProductStatus.DRAFT, ConsumerCardStatus.IN_WORK, LogisticCardStatus.NEW);

        consumerProductCardLifecycleService.executeAction(cpcId, ConsumerActionIds.TO_PROCESS);
        assertState(product, ProductStatus.DRAFT, ConsumerCardStatus.PROCESSED, LogisticCardStatus.NEW);

        logisticProductCardLifecycleService.executeAction(lpcId, LogisticActionIds.TO_WORK);
        assertState(product, ProductStatus.DRAFT, ConsumerCardStatus.PROCESSED, LogisticCardStatus.IN_WORK);

        logisticProductCardLifecycleService.executeAction(lpcId, LogisticActionIds.TO_PROCESS);
        assertState(product, ProductStatus.PREMODERATION_PASSED, ConsumerCardStatus.PROCESSED, LogisticCardStatus.PROCESSED);
    }

    private void assertState(Product p, ProductStatus productStatus, ConsumerCardStatus consumerCardStatus, LogisticCardStatus logisticCardStatus) {
        assertThat(productRepository.findById(p.getId()))
                .map(Product::getStatus)
                .hasValue(productStatus);
        assertThat(consumerCardRepository.findById(p.getConsumerCardId()))
                .map(ConsumerCard::getStatus)
                .hasValue(consumerCardStatus);
        assertThat(logisticCardRepository.findById(p.getLogisticCardId()))
                .map(LogisticCard::getStatus)
                .hasValue(logisticCardStatus);
    }

    private Product createProductWithPremoderationBlocks() {
        return createProductWithPremoderationBlocks("fake product",
                ProductStatus.DRAFT, ConsumerCardStatus.NEW, LogisticCardStatus.NEW);
    }

    private Product createProductWithPremoderationBlocks(String productName,
                                                         ProductStatus productStatus,
                                                         ConsumerCardStatus consumerCardStatus,
                                                         LogisticCardStatus logisticCardStatus) {
        LogisticCard lc = LogisticCard.create();
        lc.setStatus(logisticCardStatus);
        logisticCardRepository.save(lc);
        ConsumerCard cc = ConsumerCard.create();
        consumerCardRepository.save(cc);
        cc.setStatus(consumerCardStatus);

        Product product = Product.create(cc, lc);
        product.setStatus(productStatus);
        product.setProductName(productName);
        productRepository.save(product);
        product.addCpc(cc);
        product.addLpc(lc);
        consumerCardRepository.save(cc);
        logisticCardRepository.save(lc);

        return product;
    }

    @Import({ProductLifecycleConfig.class, ConsumerLifecycleConfig.class})
    @TestConfiguration
    static class TestConfig {

    }
}