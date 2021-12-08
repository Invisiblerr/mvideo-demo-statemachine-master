package ru.oz.demostatemachine.common.usecases;

import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import ru.oz.demostatemachine.common.lifecycle.ActionId;
import ru.oz.demostatemachine.common.lifecycle.LifecycleSupport;
import ru.oz.demostatemachine.common.usecases.impl.DoAction;

/**
 * DoActionUsecase.
 *
 * @author Igor_Ozol
 */
public interface DoActionUsecase<T extends LifecycleSupport<T>, A extends ActionId<T>> {

    Either<DoActionError, Long> execute(DoAction.DoActionRequest<T, A> request);

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    class DoActionRequest<T extends LifecycleSupport<T>> {
        private long lfsId;
        private ActionId<T> action;
    }

    @RequiredArgsConstructor
    @Getter
    enum DoActionError {
        DOMAIN_OBJECT_NOT_FOUND_ERROR("error while get domain object for lifecycle transition: {}"),
        UNKNOWN_ERROR("error while occurred when status transition: {}");

        private final String errorMsgTemplate;
    }
}
