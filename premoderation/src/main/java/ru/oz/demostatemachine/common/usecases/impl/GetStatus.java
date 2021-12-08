package ru.oz.demostatemachine.common.usecases.impl;

import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.oz.demostatemachine.common.lifecycle.LifecycleSupport;
import ru.oz.demostatemachine.common.lifecycle.StatusId;
import ru.oz.demostatemachine.common.usecases.GetStatusUsecase;

/**
 * GetStatus.
 *
 * @author Igor_Ozol
 */
@RequiredArgsConstructor
public class GetStatus<T extends LifecycleSupport<T>> implements GetStatusUsecase<T> {

    private final JpaRepository<T, Long> repository;

    @Override
    public Either<GetStatueError, StatusId<T>> execute(Long id) {
        return Option.ofOptional(repository.findById(id))
                .map(LifecycleSupport::getStatus)
                .toEither(GetStatueError.UNKNOWN_ERROR);
    }
}
