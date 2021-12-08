/*
 * VTB Group. Do not reproduce without permission in writing.
 * Copyright (c) 2021 VTB Group. All rights reserved.
 */

package ru.oz.demostatemachine.common.lifecycle;

/**
 * Life cycle support contract.
 *
 * @param <T> domain object realizing LifecycleSupport
 *
 * @author Igor_Ozol
 */
public interface LifecycleSupport<T extends LifecycleSupport<T>> {
    Long getId();

    StatusId<T> getStatus();

    void setStatus(StatusId<T> status);
}