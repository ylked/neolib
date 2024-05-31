package ch.hearc.nde.loanservice.repository;

import ch.hearc.nde.loanservice.repository.entity.LoanEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LoanRepository extends CrudRepository<LoanEntity, Long> {
    boolean existsByBookIdAndEndIsNull(Long bookId);
    Optional<LoanEntity> findByBookIdAndEndIsNull(Long bookId);
    Optional<LoanEntity> findByUserIdAndEndIsNull(Long userId);
}
