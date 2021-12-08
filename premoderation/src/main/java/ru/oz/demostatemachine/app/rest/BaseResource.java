package ru.oz.demostatemachine.app.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.oz.demostatemachine.common.lifecycle.ActionId;
import ru.oz.demostatemachine.common.lifecycle.LifecycleSupport;
import ru.oz.demostatemachine.common.usecases.impl.DoAction;
import ru.oz.demostatemachine.common.usecases.impl.GetStatus;

/**
 * BaseResource.
 *
 * @author Igor_Ozol
 */
public abstract class BaseResource<T extends LifecycleSupport<T>, A extends ActionId<T>> {
    private final DoAction<T, A> doAction;
    private final GetStatus<T> getStatus;

    protected BaseResource(DoAction<T, A> doAction, GetStatus<T> getStatus) {
        this.doAction = doAction;
        this.getStatus = getStatus;
    }

    @PostMapping("/do-action")
    ResponseEntity<?> doAction(@RequestBody DoAction.DoActionRequest<T, A> request) {
        return doAction.execute(request).fold(
                error -> new ResponseEntity<>(error, HttpStatus.BAD_REQUEST),
                id -> new ResponseEntity<>(id, HttpStatus.OK)
        );
    }

    @PostMapping("/{id}")
    ResponseEntity<?> getStatus(@PathVariable long id) {
        return getStatus.execute(id).fold(
                error -> new ResponseEntity<>(error, HttpStatus.BAD_REQUEST),
                statusId -> new ResponseEntity<>(statusId, HttpStatus.OK)
        );
    }
}