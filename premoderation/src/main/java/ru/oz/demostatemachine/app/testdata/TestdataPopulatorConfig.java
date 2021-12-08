package ru.oz.demostatemachine.app.testdata;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.oz.demostatemachine.consumer.infrastructure.persistence.ConsumerCardRepository;
import ru.oz.demostatemachine.consumer.model.ConsumerCard;
import ru.oz.demostatemachine.logistic.infrastructure.persistence.LogisticCardRepository;
import ru.oz.demostatemachine.logistic.model.LogisticCard;
import ru.oz.demostatemachine.product.infrastructure.persistence.ProductRepository;
import ru.oz.demostatemachine.product.model.Product;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

/**
 * TestdataPopulatorConfig.
 *
 * @author Igor
 */
@Slf4j
@ConditionalOnProperty(name = "testdata", matchIfMissing = false)
@Configuration
public class TestdataPopulatorConfig {

    @Autowired
    private ConsumerCardRepository consumerCardRepository;
    @Autowired
    private LogisticCardRepository logisticCardRepository;
    @Autowired
    private ProductRepository productRepository;

    @Transactional
    @Bean
    CommandLineRunner runner() {
        return ars -> {
            ConsumerCard cpc = consumerCardRepository.save(ConsumerCard.create());
            LogisticCard lpc = logisticCardRepository.save(LogisticCard.create());

            Product product = Product.create(cpc, lpc);
            product.setProductName("Бурбулятор");
            product.addCpc(cpc);
            product.addLpc(lpc);
            productRepository.save(product);
            product.addCpc(cpc);
            product.addLpc(lpc);
            consumerCardRepository.save(cpc);
            logisticCardRepository.save(lpc);
            log.info("Test product was created!");
        };
    }

    @PostConstruct
    public void info() {
        log.info(" *********  TestdataPopulatorConfig  ********* ");
    }
}
