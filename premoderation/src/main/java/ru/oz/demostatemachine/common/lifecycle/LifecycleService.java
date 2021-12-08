/*
 * VTB Group. Do not reproduce without permission in writing.
 * Copyright (c) 2021 VTB Group. All rights reserved.
 */

package ru.oz.demostatemachine.common.lifecycle;

import io.vavr.control.Try;

/**
 * LifecycleService.
 *
 * @author Igor_Ozol
 */
public interface LifecycleService<T extends LifecycleSupport<T>> {

    String LIFECYCLE_SUPPORT_KEY = "LIFECYCLE_SUPPORT_KEY";
    String CONTEXT = "CONTEXT";
    String FAILED = "FAILED";

    /** сделать приватным **/
    Try<T> executeAction(T lfs, ActionId<T> action);

    Try<T> executeAction(Long id, ActionId<T> action);
}
