package ru.oz.demostatemachine.app.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.oz.demostatemachine.common.lifecycle.LifecycleService;
import ru.oz.demostatemachine.common.usecases.impl.DoAction;
import ru.oz.demostatemachine.common.usecases.impl.GetStatus;
import ru.oz.demostatemachine.logistic.infrastructure.persistence.LogisticCardRepository;
import ru.oz.demostatemachine.logistic.interfaces.rest.LogisticCardResource;
import ru.oz.demostatemachine.logistic.model.LogisticCard;
import ru.oz.demostatemachine.logistic.model.LogisticCardStatus.LogisticActionIds;

/**
 * LogisticRestConfig.
 *
 * @author Igor_Ozol
 */
@Configuration
public class LogisticRestConfig {
    @Bean
    public DoAction<LogisticCard, LogisticActionIds> logisticCardDoActionUseCase(
            LogisticCardRepository repository,
            LifecycleService<LogisticCard> lifecycleService) {
        return new DoAction<>(repository, lifecycleService);
    }

    @Bean
    public GetStatus<LogisticCard> logisticCardGetStatusUseCase(LogisticCardRepository repository) {
        return new GetStatus<>(repository);
    }


    @Bean
    public LogisticCardResource logisticDoActionResource(
            DoAction<LogisticCard, LogisticActionIds> logisticCardDoActionUseCase,
            GetStatus<LogisticCard> getStatusUseCase) {
        return new LogisticCardResource(logisticCardDoActionUseCase, getStatusUseCase);
    }
}
