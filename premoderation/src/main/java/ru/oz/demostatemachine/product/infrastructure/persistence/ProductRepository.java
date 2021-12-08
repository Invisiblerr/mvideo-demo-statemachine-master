package ru.oz.demostatemachine.product.infrastructure.persistence;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.oz.demostatemachine.product.model.Product;

/**
 * ConsumerCardRepository.
 *
 * @author Igor_Ozol
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
}
