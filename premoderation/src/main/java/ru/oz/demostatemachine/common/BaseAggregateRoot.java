package ru.oz.demostatemachine.common;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * BaseAggregateRoot.
 *
 * @author Igor_Ozol
 */
@MappedSuperclass
public abstract class BaseAggregateRoot<ID extends Serializable> extends BaseEntity<ID> {

    private final @Transient
    List<Object> domainEvents = new ArrayList<>();

    protected void registerEvent(@NotNull Object event) {
        domainEvents.add(Objects.requireNonNull(event));
    }

    @AfterDomainEventPublication
    protected void clearDomainEvents() {
        this.domainEvents.clear();
    }

    @DomainEvents
    protected Collection<Object> domainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }
}
