package ru.oz.demostatemachine.common.usecases.impl;

import static ru.oz.demostatemachine.common.usecases.DoActionUsecase.DoActionError.DOMAIN_OBJECT_NOT_FOUND_ERROR;
import static ru.oz.demostatemachine.common.usecases.DoActionUsecase.DoActionError.UNKNOWN_ERROR;

import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.oz.demostatemachine.common.lifecycle.ActionId;
import ru.oz.demostatemachine.common.lifecycle.LifecycleService;
import ru.oz.demostatemachine.common.lifecycle.LifecycleSupport;
import ru.oz.demostatemachine.common.usecases.DoActionUsecase;

import java.util.Optional;

/**
 * DoAction.
 *
 * @author Igor_Ozol
 */
@Slf4j
@RequiredArgsConstructor
public class DoAction<T extends LifecycleSupport<T>, A extends ActionId<T>> implements DoActionUsecase<T, A> {

    private final JpaRepository<T, Long> repository;
    private final LifecycleService<T> lifecycleService;

    @Override
    public Either<DoActionError, Long> execute(DoActionRequest<T, A> request) {
        Optional<T> maybeT = repository.findById(request.lfsId);
        if (maybeT.isEmpty()) {
            return Either.left(DOMAIN_OBJECT_NOT_FOUND_ERROR);
        }

        return lifecycleService.executeAction(maybeT.get(), request.action)
                .andThen(() -> log.info("можно сделать side effect"))
                .toEither()
                .mapLeft(throwable -> UNKNOWN_ERROR)
                .map(LifecycleSupport::getId);
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class DoActionRequest<T extends LifecycleSupport<T>, A extends ActionId<T>> {
        private long lfsId;
        private A action;
    }
}
