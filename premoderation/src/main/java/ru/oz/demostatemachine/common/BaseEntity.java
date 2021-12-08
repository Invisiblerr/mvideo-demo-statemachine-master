package ru.oz.demostatemachine.common;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.Optional;

/**
 * BaseEntity.
 *
 * @author Igor_Ozol
 */
@MappedSuperclass
public abstract class BaseEntity<ID extends Serializable> extends AbstractPersistable<ID> {

    // TODO: 02.12.2021 ObjectOptimisticLockingFailureException Разобраться, почему оптимистические блокировки ломают
    // стейт машину
    @Version
    private Long version;

    public @NotNull Optional<Long> getVersion() {
        return Optional.ofNullable(version);
    }

    protected void setVersion(@Nullable Long version) {
        this.version = version;
    }

}

