package ru.oz.demostatemachine.logistic.model;

import static ru.oz.demostatemachine.logistic.model.LogisticCardStatus.IN_WORK;
import static ru.oz.demostatemachine.logistic.model.LogisticCardStatus.NEW;
import static ru.oz.demostatemachine.logistic.model.LogisticCardStatus.PROCESSED;

import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.oz.demostatemachine.common.BaseAggregateRoot;
import ru.oz.demostatemachine.common.events.StatusChangedEvent;
import ru.oz.demostatemachine.common.lifecycle.LifecycleSupport;
import ru.oz.demostatemachine.common.lifecycle.StatusId;
import ru.oz.demostatemachine.common.model.Lock;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.Clock;
import java.util.Objects;

@Entity
@Table(name = "logistic_product_card")
@AllArgsConstructor
@Getter
@Builder
@EntityListeners(AuditingEntityListener.class)
public class LogisticCard extends BaseAggregateRoot<Long> implements LifecycleSupport<LogisticCard> {

    @Setter
    @Column(name = "product_id")
    private Long productId;

    @Enumerated(EnumType.STRING)
    @Column(name = "premoderation_status", nullable = false)
    private LogisticCardStatus premoderationStatus;

    @Column(name = "error_message")
    private String errorMessage;

    @Embedded
    private Lock lock;

    @Setter
    @Transient
    private Clock clock = Clock.systemUTC();

    public Either<Error, Void> toWorkBy(String userName) {
        return tryLock(userName, this.clock).flatMap(v -> changeStatus(IN_WORK));
    }

    public Either<Error, Void> toProcessBy(String userName) {
        return tryUnlock(userName).flatMap(v -> changeStatus(PROCESSED));
    }

    @Override
    public StatusId<LogisticCard> getStatus() {
        return this.premoderationStatus;
    }

    @Override
    public void setStatus(StatusId<LogisticCard> status) {
        if (!Objects.equals(this.premoderationStatus, status)) {
            changeStatus(status);
        }
    }

    protected LogisticCard() {
        this.premoderationStatus = NEW;
        this.lock = Lock.EMPTY_LOCK;
    }

    public static LogisticCard create() {
        return new LogisticCard();
    }

    public Either<Error, Void> tryLock(String userName, Clock clock) {
        if (isLocked()) {
            return Either.left(Error.ERROR_WHILE_LOCKING);
        }
        this.lock = Lock.createLock(clock, userName, this);
        return Either.right(null);
    }

    public Either<Error, Void> tryUnlock(String userName) {
        if (!isLocked()) {
            return Either.right(null);
        } else if (isLockedBy(userName)) {
            this.lock = Lock.empty();
            return Either.right(null);
        }

        return Either.left(Error.ERROR_WHILE_UNLOCK);
    }

    public boolean isLocked() {
        return !Lock.empty().equals(this.lock);
    }

    public boolean isLockedBy(String userName) {
        if (isLocked()) {
            return lock.isLockedBy(userName);
        }
        return false;
    }

    private Either<Error, Void> changeStatus(StatusId<LogisticCard> newStatus) {
        registerEvent(new StatusChangedEvent<>(this.getId(),
                this.productId,
                this.premoderationStatus,
                newStatus));
        this.premoderationStatus = (LogisticCardStatus) newStatus;
        return Either.right(null);
    }

    @Getter
    @RequiredArgsConstructor
    public enum Error {
        ERROR_WHILE_LOCKING("logisticCard already locked"),
        ERROR_WHILE_UNLOCK("error while unlock logistic card"),
        CHANGE_STATUS_ERROR("not applicable status in this case");

        private final String msg;
    }
}
