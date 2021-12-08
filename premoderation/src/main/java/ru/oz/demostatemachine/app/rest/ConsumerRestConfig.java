package ru.oz.demostatemachine.app.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.oz.demostatemachine.common.lifecycle.LifecycleService;
import ru.oz.demostatemachine.common.usecases.impl.DoAction;
import ru.oz.demostatemachine.common.usecases.impl.GetStatus;
import ru.oz.demostatemachine.consumer.infrastructure.persistence.ConsumerCardRepository;
import ru.oz.demostatemachine.consumer.interfaces.rest.ConsumerProductCardResource;
import ru.oz.demostatemachine.consumer.model.ConsumerCard;
import ru.oz.demostatemachine.consumer.model.ConsumerCardStatus.ConsumerActionIds;

/**
 * ConsumerRestConfig.
 *
 * @author Igor_Ozol
 */
@Configuration
public class ConsumerRestConfig {
    @Bean
    public DoAction<ConsumerCard, ConsumerActionIds> consumerCardDoActionUseCase(
            ConsumerCardRepository repository,
            LifecycleService<ConsumerCard> lifecycleService) {
        return new DoAction<>(repository, lifecycleService);
    }

    @Bean
    public GetStatus<ConsumerCard> consumerGetStatusUseCase(ConsumerCardRepository repository) {
        return new GetStatus<>(repository);
    }

    @Bean
    public ConsumerProductCardResource consumerDoActionResource(
            DoAction<ConsumerCard, ConsumerActionIds> consumerCardDoActionUseCase,
            GetStatus<ConsumerCard> getStatus) {
        return new ConsumerProductCardResource(consumerCardDoActionUseCase, getStatus);
    }
}
