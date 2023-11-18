package com.woowup.domain;

import java.util.List;

/**
 * Implementations of this interface must define the behavior of how an element
 * should be stacked in the queue
 */
@FunctionalInterface
public interface StackingStrategy<T> {
    void stackInQueue(List<T> queue, T element);
}
