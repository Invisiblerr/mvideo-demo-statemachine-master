package ru.oz.demostatemachine.common.events;

import lombok.AllArgsConstructor;
import lombok.Value;
import ru.oz.demostatemachine.common.lifecycle.LifecycleSupport;
import ru.oz.demostatemachine.common.lifecycle.StatusId;

/**
 * StatusChangedEvent.
 *
 * @author Igor_Ozol
 */
@AllArgsConstructor
@Value
public class StatusChangedEvent<T extends LifecycleSupport<T>> {
    long id;
    long productId;
    StatusId<T> previousStatus;
    StatusId<T> newStatus;
}
