package ru.oz.demostatemachine.logistic.interfaces.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.oz.demostatemachine.app.rest.BaseResource;
import ru.oz.demostatemachine.common.usecases.impl.DoAction;
import ru.oz.demostatemachine.common.usecases.impl.GetStatus;
import ru.oz.demostatemachine.logistic.model.LogisticCard;
import ru.oz.demostatemachine.logistic.model.LogisticCardStatus.LogisticActionIds;

/**
 * DoActionResource.
 *
 * @author Igor_Ozol
 */
@RestController
@RequestMapping("/logistic-product-card")
public class LogisticCardResource extends BaseResource<LogisticCard, LogisticActionIds> {

    public LogisticCardResource(DoAction<LogisticCard, LogisticActionIds> doAction,
                                GetStatus<LogisticCard> getStatus) {
        super(doAction, getStatus);
    }
}
