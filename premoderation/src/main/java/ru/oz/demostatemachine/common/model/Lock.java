package ru.oz.demostatemachine.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.annotation.CreatedDate;
import ru.oz.demostatemachine.common.lifecycle.LifecycleSupport;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Lock.
 *
 * @author Igor
 */
@EqualsAndHashCode
@Getter
@AllArgsConstructor
@Builder
@Embeddable
public class Lock {

    @Transient
    public static final Lock EMPTY_LOCK;

    static {
        Lock tmp = new Lock();
        tmp.lockStatus = Strings.EMPTY;
        tmp.lockUserName = Strings.EMPTY;
        EMPTY_LOCK = tmp;
    }

    protected Lock() {
    }

    //    @Id
//    @Column(name = "product_id", nullable = false)
//    private Long productId;

    @CreatedDate
    @Column(name = "lock_time")
    private LocalDateTime lockTime;

    @Column(name = "lock_status")
//    @Transient
//    @JsonIgnore
    private String lockStatus;

    //    @JsonIgnore
//    @Transient // TODO: 04.12.2021
    @Column(name = "lock_user_name")
    private String lockUserName;

//    public StatusId<T> getStatus() {
//        return status;
//    }

    public boolean isLockedBy(String userName) {
        return Objects.equals(this.lockUserName, userName);
    }

    public static Lock empty() {
        return EMPTY_LOCK;
    }

    public static <T extends LifecycleSupport<T>> Lock createLock(Clock clock, String userName, LifecycleSupport<T> premoderationBlock) {
        return Lock.builder()
                .lockTime(LocalDateTime.now(clock))
                .lockStatus(premoderationBlock.getStatus().toString())
                .lockUserName(userName)
                .build();
    }
}
