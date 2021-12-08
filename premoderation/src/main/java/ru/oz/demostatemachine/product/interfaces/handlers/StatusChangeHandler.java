package ru.oz.demostatemachine.product.interfaces.handlers;

import static ru.oz.demostatemachine.product.model.ProductStatus.ProductActionIds.TO_PROCESS;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import ru.oz.demostatemachine.common.events.StatusChangedEvent;
import ru.oz.demostatemachine.common.lifecycle.LifecycleService;
import ru.oz.demostatemachine.common.lifecycle.StatusId;
import ru.oz.demostatemachine.consumer.model.ConsumerCardStatus;
import ru.oz.demostatemachine.logistic.model.LogisticCardStatus;
import ru.oz.demostatemachine.product.infrastructure.persistence.ProductRepository;
import ru.oz.demostatemachine.product.model.Product;

import java.util.Set;

/**
 * StatusChangeHandler.
 *
 * @author Igor_Ozol
 */
@RequiredArgsConstructor
@Slf4j
public class StatusChangeHandler {

    private final ProductRepository productRepository;
    private final LifecycleService<Product> lifecycleService;

    private Set<StatusId<?>> processed = Set.of(ConsumerCardStatus.PROCESSED, LogisticCardStatus.PROCESSED);

    //    @EventListener
//    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener()
    public void handle(StatusChangedEvent<?> event) {
        log.info("Начало обработки эвента: event.id={}, {} -> {}", event.getId(), event.getPreviousStatus(), event.getNewStatus());

        if (notProcessed(event.getNewStatus())) {
            log.info("Это не наш статус: {}", event.getNewStatus());
            return;
        }
        Product product = productRepository.findById(event.getProductId()).orElseThrow(IllegalAccessError::new);
        lifecycleService
                .executeAction(product, TO_PROCESS)
                .onSuccess(p -> log.info("Продукт обработан: id={} status={}", p.getId(), p.getPremoderationStatus()))
                .onFailure(e -> log.error(e.getMessage(), e));

        log.info("Эвент обработан");
    }

    private boolean notProcessed(StatusId<?> status) {
        return !processed.contains(status);
    }
}
