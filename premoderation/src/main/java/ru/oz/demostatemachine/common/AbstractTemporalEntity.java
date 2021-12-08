package ru.oz.demostatemachine.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

/**
 * Абстрактная сущность с временными метками.
 *
 * @param <ID> тип первичного ключа
 */
@Getter
@MappedSuperclass
@Cacheable(value = false)
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractTemporalEntity<ID extends Serializable> extends AbstractEntity<ID> {
    @Serial
    private static final long serialVersionUID = 3936322636314846194L;

    @Column(name = "SYS_CREATION_DATE", updatable = false, nullable = false)
    @CreatedDate
    protected LocalDateTime creationDate;

    @Column(name = "SYS_UPDATE_DATE", nullable = false)
    @LastModifiedDate
    protected LocalDateTime updateDate;
}
