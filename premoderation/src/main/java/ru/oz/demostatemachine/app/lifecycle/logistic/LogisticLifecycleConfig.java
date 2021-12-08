package ru.oz.demostatemachine.app.lifecycle.logistic;


import static ru.oz.demostatemachine.logistic.model.LogisticCardStatus.IN_WORK;
import static ru.oz.demostatemachine.logistic.model.LogisticCardStatus.LogisticActionIds.TO_PROCESS;
import static ru.oz.demostatemachine.logistic.model.LogisticCardStatus.LogisticActionIds.TO_WORK;
import static ru.oz.demostatemachine.logistic.model.LogisticCardStatus.NEW;
import static ru.oz.demostatemachine.logistic.model.LogisticCardStatus.PROCESSED;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineBuilder;
import ru.oz.demostatemachine.app.lifecycle.LifecycleUtil;
import ru.oz.demostatemachine.app.lifecycle.SpringLifecycleService;
import ru.oz.demostatemachine.app.rest.UserContext;
import ru.oz.demostatemachine.common.lifecycle.ActionId;
import ru.oz.demostatemachine.common.lifecycle.Lifecycle;
import ru.oz.demostatemachine.common.lifecycle.LifecycleService;
import ru.oz.demostatemachine.common.lifecycle.StatusId;
import ru.oz.demostatemachine.logistic.infrastructure.persistence.LogisticCardRepository;
import ru.oz.demostatemachine.logistic.model.LogisticCard;
import ru.oz.demostatemachine.logistic.model.LogisticCardStatus;

import java.util.EnumSet;
import java.util.HashSet;

/**
 * LogisticConfig.
 *
 * @author Igor_Ozol
 */
@Slf4j
@Configuration
public class LogisticLifecycleConfig {

    @Bean
    public LifecycleService<LogisticCard> logisticCardLifecycleService(
            Lifecycle<LogisticCard> logisticCardLifecycle,
            LogisticCardRepository logisticCardRepository) {
        return new SpringLifecycleService<>(logisticCardLifecycle, logisticCardRepository);
    }

    @Configuration
    static class LogisticCardLifecycle implements Lifecycle<LogisticCard> {
        @Override
        public StateMachineBuilder.Builder<StatusId<LogisticCard>, ActionId<LogisticCard>> stateMachineBuilder() {
            StateMachineBuilder.Builder<StatusId<LogisticCard>, ActionId<LogisticCard>> builder = null;
            try {
                builder = StateMachineBuilder.builder();

                builder.configureStates()
                        .withStates().states(new HashSet<>(EnumSet.allOf(LogisticCardStatus.class)))
                        .initial(NEW, logisticNewAction());

                builder.configureTransitions()
                        .withExternal()
                        .source(NEW).target(IN_WORK).event(TO_WORK).action(logisticDoWorkAction())
                        .and()
                        .withExternal()
                        .source(IN_WORK).target(PROCESSED).event(TO_PROCESS).action(logisticDoProcessAction());

            } catch (Exception e) {
                log.error("Error in StateMachine build process", e);
            }
            return builder;
        }

        @Autowired
        private LogisticCardRepository logisticCardRepository;

        @Autowired
        private UserContext currntContext;

        @Bean
        public LogisticCardAction logisticNewAction() {
            return cxt -> log.info("newAction invoked");
        }

        @Bean
        public LogisticCardAction logisticDoWorkAction() {
            return cxt -> {
                log.info("logisticDoWorkAction begin...");
                try {
                    LogisticCard lpc = LifecycleUtil.getLifecycleSupportFromContext(cxt);
                    log.warn("cpc.version={}", lpc.getVersion());
                    lpc.toWorkBy(currntContext.getUserName())
                            .peek(nothing -> logisticCardRepository.save(lpc))
                            .peek(nothing -> log.warn("cpc.version={}", lpc.getVersion()))
                            .peekLeft(error -> log.error(error.getMsg()))
                            .peekLeft(error -> cxt.getStateMachine().setStateMachineError(new RuntimeException(error.getMsg())));
                    log.info("транзакция зафиксирована");
                } catch (Exception e) {
                    log.error("error while executing logisticDoWorkAction", e);
                }
                log.info("logisticDoWorkAction end.");
            };
        }

        @Bean
        public LogisticCardAction logisticDoProcessAction() {
            return cxt -> {
                log.info("doProcessAction begin...");
                try {
                    LogisticCard lpc = LifecycleUtil.getLifecycleSupportFromContext(cxt);
                    log.warn("cpc.version={}", lpc.getVersion());
                    lpc.toProcessBy(currntContext.getUserName())
                            .peek(nothing -> logisticCardRepository.save(lpc))
                            .peek(nothing -> log.warn("cpc.version={}", lpc.getVersion()))
                            .peekLeft(error -> log.error(error.getMsg()))
                            .peekLeft(error -> cxt.getStateMachine().setStateMachineError(new RuntimeException(error.getMsg())));

                    log.warn("cpc.version={}", lpc.getVersion());
                    log.info("транзакция зафиксирована");
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
                log.info("doProcessAction end.");
            };
        }
    }

    interface LogisticCardAction extends Action<StatusId<LogisticCard>, ActionId<LogisticCard>> {

    }

}
