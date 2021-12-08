package ru.oz.demostatemachine.product.model;

import static ru.oz.demostatemachine.product.model.ProductStatus.DRAFT;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.oz.demostatemachine.common.BaseAggregateRoot;
import ru.oz.demostatemachine.common.lifecycle.LifecycleSupport;
import ru.oz.demostatemachine.common.lifecycle.StatusId;
import ru.oz.demostatemachine.consumer.model.ConsumerCard;
import ru.oz.demostatemachine.logistic.model.LogisticCard;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@Accessors(chain = true)
@Table(name = "product")
public class Product extends BaseAggregateRoot<Long> implements LifecycleSupport<Product> {

    @Basic
    @Column(name = "product_name")
    private String productName;

    @Enumerated(EnumType.STRING)
    @Column(name = "premoderation_status")
    private ProductStatus premoderationStatus;

    @Column(name = "consumer_card_id")
    private Long consumerCardId;

    @Column(name = "logistic_card_id")
    private Long logisticCardId;

    protected Product() {
        this.setStatus(DRAFT);
    }

    public void addCpc(ConsumerCard cpc) {
        cpc.setProductId(this.getId());
        this.setConsumerCardId(cpc.getId());
    }

    public void addLpc(LogisticCard lpc) {
        lpc.setProductId(this.getId());
        this.setLogisticCardId(lpc.getId());
    }

    public static Product create(ConsumerCard cpc,
                                 LogisticCard lpc) {
        Product product = new Product();
        product.addCpc(cpc);
        product.addLpc(lpc);
        return product;
    }

    @Override
    public StatusId<Product> getStatus() {
        return this.premoderationStatus;
    }

    @Override
    public void setStatus(StatusId<Product> status) {
        this.premoderationStatus = (ProductStatus) status;
    }
}
