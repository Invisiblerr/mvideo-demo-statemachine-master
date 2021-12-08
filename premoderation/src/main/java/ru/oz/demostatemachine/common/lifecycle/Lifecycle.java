/*
 * VTB Group. Do not reproduce without permission in writing.
 * Copyright (c) 2021 VTB Group. All rights reserved.
 */

package ru.oz.demostatemachine.common.lifecycle;


import org.springframework.statemachine.config.StateMachineBuilder;

/**
 * Contract for determining the life cycle graph.
 *
 * @param <T> domain object realizing LifecycleSupport
 * @author Igor_Ozol
 */
public interface Lifecycle<T extends LifecycleSupport<T>> {

    StateMachineBuilder.Builder<StatusId<T>, ActionId<T>> stateMachineBuilder();

}
