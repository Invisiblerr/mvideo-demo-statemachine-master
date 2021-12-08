package ru.oz.demostatemachine.common.usecases;

import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import ru.oz.demostatemachine.common.lifecycle.ActionId;
import ru.oz.demostatemachine.common.lifecycle.LifecycleSupport;
import ru.oz.demostatemachine.common.lifecycle.StatusId;

/**
 * GetStatusUsecase.
 *
 * @author Igor_Ozol
 */
public interface GetStatusUsecase<T extends LifecycleSupport<T>> {

    Either<GetStatueError, StatusId<T>> execute(Long id);

    @RequiredArgsConstructor
    @Getter
    enum GetStatueError {
        UNKNOWN_ERROR("error while occurred when get status: {}");

        private final String errorMsgTemplate;
    }
}
