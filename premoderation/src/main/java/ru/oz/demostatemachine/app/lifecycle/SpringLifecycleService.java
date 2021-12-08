package ru.oz.demostatemachine.app.lifecycle;

import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import ru.oz.demostatemachine.common.lifecycle.ActionId;
import ru.oz.demostatemachine.common.lifecycle.Lifecycle;
import ru.oz.demostatemachine.common.lifecycle.LifecycleService;
import ru.oz.demostatemachine.common.lifecycle.LifecycleSupport;
import ru.oz.demostatemachine.common.lifecycle.StatusId;

import java.util.Objects;
import java.util.Optional;

/**
 * SpringLifecycleService.
 *
 * @author Igor_Ozol
 */
@RequiredArgsConstructor
public class SpringLifecycleService<T extends LifecycleSupport<T>> implements LifecycleService<T> {

    private final Lifecycle<T> lifecycle;
    private final JpaRepository<T, Long> repository;

    // TODO: 30.11.2021 Refactor depricate methods.
    @Override
    public Try<T> executeAction(T lfs, ActionId<T> action) {
        StateMachine<StatusId<T>, ActionId<T>> stateMachine = creatStateMachine(lfs);
        OnError<T> stateMachineErrorHandler = initErrorHandler(stateMachine);
        try {
            stateMachine.start();
            stateMachine.sendEvent(action);
            return stateMachineErrorHandler.getException()
                    .<Try<T>>map(Try::failure)
                    .orElse(Try.success(lfs));
        } finally {
            stateMachine.stop();
        }
    }

    @Override
    public Try<T> executeAction(Long id, ActionId<T> action) {
        T lfs = repository.findById(id).orElseThrow(IllegalArgumentException::new);
        return executeAction(lfs, action);
    }

    @SneakyThrows
    private StateMachine<StatusId<T>, ActionId<T>> creatStateMachine(T lfs) {
        StateMachine<StatusId<T>, ActionId<T>> stateMachine = lifecycle.stateMachineBuilder().build();
        setActualState(lfs, stateMachine);
        return stateMachine;
    }

    private OnError<T> initErrorHandler(StateMachine<StatusId<T>, ActionId<T>> stateMachine) {
        OnError<T> stateMachineErrorHandler = new OnError<>();
        stateMachine.getStateMachineAccessor()
                .doWithRegion(access -> access.addStateMachineInterceptor(stateMachineErrorHandler));
        return stateMachineErrorHandler;
    }

    private void setActualState(T lfs, StateMachine<StatusId<T>, ActionId<T>> stateMachine) {
        if (Objects.nonNull(lfs.getStatus())) {
            stateMachine.getExtendedState().getVariables().put(LIFECYCLE_SUPPORT_KEY, lfs);

            stateMachine
                    .getStateMachineAccessor()
                    .withRegion()
                    .resetStateMachine(new DefaultStateMachineContext<>(lfs.getStatus(), null, null, null));

        }
    }

    static class OnError<T extends LifecycleSupport<T>> extends StateMachineInterceptorAdapter<StatusId<T>, ActionId<T>> {
        private Exception exception;

        @Override
        public Exception stateMachineError(StateMachine<StatusId<T>, ActionId<T>> sm, Exception exception) {
            this.exception = exception;
            return exception;
        }

        public Optional<Exception> getException() {
            return Optional.ofNullable(exception);
        }
    }

}
