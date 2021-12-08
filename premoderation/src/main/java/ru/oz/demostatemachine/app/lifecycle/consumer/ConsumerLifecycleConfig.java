package ru.oz.demostatemachine.app.lifecycle.consumer;

import static ru.oz.demostatemachine.consumer.model.ConsumerCardStatus.ConsumerActionIds.TO_PROCESS;
import static ru.oz.demostatemachine.consumer.model.ConsumerCardStatus.ConsumerActionIds.TO_WORK;
import static ru.oz.demostatemachine.consumer.model.ConsumerCardStatus.IN_WORK;
import static ru.oz.demostatemachine.consumer.model.ConsumerCardStatus.NEW;
import static ru.oz.demostatemachine.consumer.model.ConsumerCardStatus.PROCESSED;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineBuilder;
import ru.oz.demostatemachine.app.lifecycle.LifecycleUtil;
import ru.oz.demostatemachine.app.lifecycle.SpringLifecycleService;
import ru.oz.demostatemachine.common.lifecycle.ActionId;
import ru.oz.demostatemachine.common.lifecycle.Lifecycle;
import ru.oz.demostatemachine.common.lifecycle.LifecycleService;
import ru.oz.demostatemachine.common.lifecycle.StatusId;
import ru.oz.demostatemachine.consumer.infrastructure.persistence.ConsumerCardRepository;
import ru.oz.demostatemachine.consumer.model.ConsumerCard;
import ru.oz.demostatemachine.consumer.model.ConsumerCardStatus;

import java.util.EnumSet;
import java.util.HashSet;

/**
 * ConsumerConfig.
 *
 * @author Igor_Ozol
 */
@Slf4j
@Configuration
public class ConsumerLifecycleConfig {

    @Bean
    public LifecycleService<ConsumerCard> consumerCardLifecycleService(
            Lifecycle<ConsumerCard> consumerProductCardLifecycle,
            ConsumerCardRepository consumerCardRepository) {
        return new SpringLifecycleService<>(consumerProductCardLifecycle, consumerCardRepository);
    }

    @Configuration
    static class ConsumerProductCardLifecycle implements Lifecycle<ConsumerCard> {
        @Override
        public StateMachineBuilder.Builder<StatusId<ConsumerCard>, ActionId<ConsumerCard>> stateMachineBuilder() {
            StateMachineBuilder.Builder<StatusId<ConsumerCard>, ActionId<ConsumerCard>> builder = null;
            try {
                builder = StateMachineBuilder.builder();

                builder.configureStates()
                        .withStates().states(new HashSet<>(EnumSet.allOf(ConsumerCardStatus.class)))
                        .initial(NEW, consumerNewAction());

                builder.configureTransitions()
                        .withExternal()
                        .source(NEW).target(IN_WORK).event(TO_WORK).action(consumerDoWorkAction())
                        .and()
                        .withExternal()
                        .source(IN_WORK).target(PROCESSED).event(TO_PROCESS).action(consumerDoProcessAction());

            } catch (Exception e) {
                log.error("Error in StateMachine build process", e);
            }
            return builder;
        }

        @Autowired
        private ConsumerCardRepository consumerCardRepository;

        @Bean
        public ConsumerProductCardAction consumerNewAction() {
            return cxt -> log.info("newAction invoked");
        }

        @Bean
        public ConsumerProductCardAction consumerDoWorkAction() {
            return cxt -> {
                log.info("doWorkAction begin...");
                try {
                    ConsumerCard cpc = LifecycleUtil.getLifecycleSupportFromContext(cxt);
                    log.warn("cpc.version={}", cpc.getVersion());
                    cpc.changeStatus(IN_WORK);
                    consumerCardRepository.save(cpc);
                    log.warn("cpc.version={}", cpc.getVersion());
                    log.info("транзакция зафиксирована");
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
                log.info("doWorkAction end.");
            };
        }

        @Bean
        public ConsumerProductCardAction consumerDoProcessAction() {
            return cxt -> {
                log.info("doProcessAction begin...");
                try {
                    ConsumerCard cpc = LifecycleUtil.getLifecycleSupportFromContext(cxt);
                    log.warn("cpc.version={}", cpc.getVersion());
                    cpc.changeStatus(PROCESSED);
                    consumerCardRepository.save(cpc);
                    log.warn("cpc.version={}", cpc.getVersion());
                    log.info("транзакция зафиксирована");
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
                log.info("doProcessAction end.");
            };
        }
    }

    interface ConsumerProductCardAction extends Action<StatusId<ConsumerCard>, ActionId<ConsumerCard>> {

    }
}
