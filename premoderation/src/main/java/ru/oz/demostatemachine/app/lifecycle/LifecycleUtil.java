/*
 * VTB Group. Do not reproduce without permission in writing.
 * Copyright (c) 2021 VTB Group. All rights reserved.
 */

package ru.oz.demostatemachine.app.lifecycle;

import lombok.experimental.UtilityClass;
import org.springframework.statemachine.StateContext;
import ru.oz.demostatemachine.common.lifecycle.ActionId;
import ru.oz.demostatemachine.common.lifecycle.LifecycleService;
import ru.oz.demostatemachine.common.lifecycle.LifecycleSupport;
import ru.oz.demostatemachine.common.lifecycle.StatusId;

/**
 * LifecycleUtil.
 *
 * @author Igor_Ozol
 */
@UtilityClass
public class LifecycleUtil {

    public <T extends LifecycleSupport<T>> Object getObjectFromContextByKey(StateContext<StatusId<T>, ActionId<T>> context, String key) {
        return context.getExtendedState().getVariables().get(key);
    }

    public <T extends LifecycleSupport<T>> T getLifecycleSupportFromContext(StateContext<StatusId<T>, ActionId<T>> context) {
        Object obj = context.getExtendedState().getVariables().get(LifecycleService.LIFECYCLE_SUPPORT_KEY);
        if (obj instanceof LifecycleSupport) {
            return (T) obj;
        }
        String msg = String.format("obj was %s, but expected %s.", obj.getClass(), LifecycleSupport.class.getSimpleName());
        throw new IllegalArgumentException(msg);
    }
}
