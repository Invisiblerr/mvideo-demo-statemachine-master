package ru.oz.demostatemachine.app.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.oz.demostatemachine.consumer.infrastructure.persistence.ConsumerCardRepository;
import ru.oz.demostatemachine.consumer.model.ConsumerCard;
import ru.oz.demostatemachine.logistic.infrastructure.persistence.LogisticCardRepository;
import ru.oz.demostatemachine.logistic.model.LogisticCard;
import ru.oz.demostatemachine.product.infrastructure.persistence.ProductRepository;
import ru.oz.demostatemachine.product.model.Product;

/**
 * DbConfig.
 *
 * @author Igor
 */
@Slf4j
@EnableJpaRepositories(basePackageClasses = {ProductRepository.class, ConsumerCardRepository.class, LogisticCardRepository.class})
@EntityScan(basePackageClasses = {Product.class, ConsumerCard.class, LogisticCard.class})
@Configuration
public class DbConfig {
}
