package ru.oz.demostatemachine.consumer.infrastructure.persistence;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.oz.demostatemachine.consumer.model.ConsumerCard;

/**
 * ConsumerCardRepository.
 *
 * @author Igor_Ozol
 */
public interface ConsumerCardRepository extends JpaRepository<ConsumerCard, Long> {
}
