package ru.oz.demostatemachine.consumer.interfaces.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.oz.demostatemachine.app.rest.BaseResource;
import ru.oz.demostatemachine.common.usecases.impl.DoAction;
import ru.oz.demostatemachine.common.usecases.impl.GetStatus;
import ru.oz.demostatemachine.consumer.model.ConsumerCardStatus.ConsumerActionIds;
import ru.oz.demostatemachine.consumer.model.ConsumerCard;

/**
 * DoActionResource.
 *
 * @author Igor_Ozol
 */
@RestController
@RequestMapping("/consumer-product-card")
public class ConsumerProductCardResource extends BaseResource<ConsumerCard, ConsumerActionIds> {

    public ConsumerProductCardResource(DoAction<ConsumerCard, ConsumerActionIds> doAction,
                                       GetStatus<ConsumerCard> getStatus) {
        super(doAction, getStatus);
    }
}
