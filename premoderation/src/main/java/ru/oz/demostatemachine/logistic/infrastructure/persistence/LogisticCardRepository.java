package ru.oz.demostatemachine.logistic.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.oz.demostatemachine.logistic.model.LogisticCard;

/**
 * LogisticProductCardRepository.
 *
 * @author Igor_Ozol
 */
public interface LogisticCardRepository extends JpaRepository<LogisticCard, Long> {
}
