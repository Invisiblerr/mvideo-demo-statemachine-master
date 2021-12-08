package ru.oz.demostatemachine.app.lifecycle.product;

import static ru.oz.demostatemachine.product.model.ProductStatus.DRAFT;
import static ru.oz.demostatemachine.product.model.ProductStatus.PREMODERATION_PASSED;
import static ru.oz.demostatemachine.product.model.ProductStatus.ProductActionIds.TO_PROCESS;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.guard.Guard;
import ru.oz.demostatemachine.app.lifecycle.LifecycleUtil;
import ru.oz.demostatemachine.app.lifecycle.SpringLifecycleService;
import ru.oz.demostatemachine.common.lifecycle.ActionId;
import ru.oz.demostatemachine.common.lifecycle.Lifecycle;
import ru.oz.demostatemachine.common.lifecycle.LifecycleService;
import ru.oz.demostatemachine.common.lifecycle.StatusId;
import ru.oz.demostatemachine.consumer.infrastructure.persistence.ConsumerCardRepository;
import ru.oz.demostatemachine.consumer.model.ConsumerCardStatus;
import ru.oz.demostatemachine.logistic.infrastructure.persistence.LogisticCardRepository;
import ru.oz.demostatemachine.logistic.model.LogisticCardStatus;
import ru.oz.demostatemachine.product.infrastructure.persistence.ProductRepository;
import ru.oz.demostatemachine.product.interfaces.handlers.StatusChangeHandler;
import ru.oz.demostatemachine.product.model.Product;
import ru.oz.demostatemachine.product.model.ProductStatus;

import java.util.EnumSet;
import java.util.HashSet;

/**
 * ProductConfig.
 *
 * @author Igor_Ozol
 */
@Slf4j
@Configuration
public class ProductLifecycleConfig {

    @Bean
    public StatusChangeHandler handler(LifecycleService<Product> productLifecycleService,
                                       ProductRepository productRepository) {
        return new StatusChangeHandler(productRepository, productLifecycleService);
    }

    @Bean
    public LifecycleService<Product> productLifecycleService(Lifecycle<Product> productLifecycle,
                                                             ProductRepository productRepository) {
        return new SpringLifecycleService<>(productLifecycle, productRepository);
    }

    @Configuration
    static class ProductLifecycle implements Lifecycle<Product> {
        @Override
        public StateMachineBuilder.Builder<StatusId<Product>, ActionId<Product>> stateMachineBuilder() {
            StateMachineBuilder.Builder<StatusId<Product>, ActionId<Product>> builder = null;
            try {
                builder = StateMachineBuilder.builder();
                builder.configureStates()
                        .withStates().states(new HashSet<>(EnumSet.allOf(ProductStatus.class)))
                        .initial(DRAFT, draftAction());

                builder.configureTransitions()
                        .withExternal()
//                        .source(DRAFT).target(PREMODERATION_PROCESS).event(TO_PREMODERATE).action(productDoProcessAction());
                        .source(DRAFT)
                        .target(PREMODERATION_PASSED)
                        .guard(premoderationGassedGuard())
                        .event(TO_PROCESS).action(productDoProcessAction());

            } catch (Exception e) {
                log.error("Error in StateMachine build process", e);
            }
            return builder;
        }

        @Autowired
        private ProductRepository productRepository;

        @Bean
        public ProductAction draftAction() {
            return cxt -> log.info("draftAction invoked");
        }

        @Bean
        public ProductAction productDoProcessAction() {
            return cxt -> {
                log.info("doProcessAction begin...");

                try {
                    Product p = LifecycleUtil.getLifecycleSupportFromContext(cxt);
                    p.setStatus(PREMODERATION_PASSED);
                    productRepository.save(p);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
                log.info("doProcessAction end.");
            };
        }

        @Autowired
        private ConsumerCardRepository consumerCardRepository;
        @Autowired
        private LogisticCardRepository logisticCardRepository;

        @Bean
        public ProductGuard premoderationGassedGuard() {
            return cxt -> {
                Product p = LifecycleUtil.getLifecycleSupportFromContext(cxt);
                var cpcProcessed = consumerCardRepository.findById(p.getConsumerCardId())
                        .filter(cpc -> cpc.getPremoderationStatus() == ConsumerCardStatus.PROCESSED)
                        .map(cpc -> Boolean.TRUE).orElse(Boolean.FALSE);

                var lpcProcessed = logisticCardRepository.findById(p.getLogisticCardId())
                        .filter(cpc -> cpc.getStatus() == LogisticCardStatus.PROCESSED)
                        .map(cpc -> Boolean.TRUE).orElse(Boolean.FALSE);

                return cpcProcessed && lpcProcessed;
            };
        }

        interface ProductAction extends Action<StatusId<Product>, ActionId<Product>> {

        }

        interface ProductGuard extends Guard<StatusId<Product>, ActionId<Product>> {

        }
    }
}