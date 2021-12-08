package ru.oz.demostatemachine.common;

/**
 * AbstractEntity.
 *
 * @author Igor_Ozol
 */

import java.io.Serializable;

public abstract class AbstractEntity<ID extends Serializable> implements Serializable {
    private static final long serialVersionUID = -7320444841510887400L;

    public AbstractEntity() {
    }

    public final boolean equals(Object other) {
        return other != null && (this == other || this.getClass().equals(other.getClass()) && this.identity().equals(((AbstractEntity)other).identity()));
    }

    public final int hashCode() {
        return this.identity().hashCode();
    }

    public abstract ID identity();
}
