package ru.oz.demostatemachine.consumer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.oz.demostatemachine.common.BaseAggregateRoot;
import ru.oz.demostatemachine.common.events.StatusChangedEvent;
import ru.oz.demostatemachine.common.lifecycle.LifecycleSupport;
import ru.oz.demostatemachine.common.lifecycle.StatusId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * Сущность "Карточка потребительских характеристик".
 */
@Entity
@Table(name = "consumer_product_card")
@AllArgsConstructor
@Builder
@Getter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class ConsumerCard extends BaseAggregateRoot<Long> implements LifecycleSupport<ConsumerCard> {

    @Setter
    @Column(name = "product_id")
    private Long productId;

    @Enumerated(EnumType.STRING)
    @Column(name = "premoderation_status")
    private ConsumerCardStatus premoderationStatus;

    public void changeStatus(StatusId<ConsumerCard> newStatus) {
        registerEvent(new StatusChangedEvent<>(this.getId(),
                                                this.productId,
                                                this.premoderationStatus,
                                                newStatus));
        this.premoderationStatus = (ConsumerCardStatus)newStatus;
    }

    @Override
    public StatusId<ConsumerCard> getStatus() {
        return this.premoderationStatus;
    }

    @Override
    public void setStatus(StatusId<ConsumerCard> status) {
        this.premoderationStatus = (ConsumerCardStatus) status;
    }

    protected ConsumerCard() {
        setStatus(ConsumerCardStatus.NEW);
    }

    public static ConsumerCard create() {
        ConsumerCard cpc = new ConsumerCard();
        return cpc;
    }
}

