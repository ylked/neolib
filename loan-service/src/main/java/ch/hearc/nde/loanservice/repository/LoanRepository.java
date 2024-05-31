package ch.hearc.nde.loanservice.repository;

import ch.hearc.nde.loanservice.repository.entity.LoanEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LoanRepository extends CrudRepository<LoanEntity, Long> {
    boolean existsByBookIdAndEndDateIsNull(Long bookId);
    Optional<LoanEntity> findByBookIdAndEndDateIsNull(Long bookId);
    Optional<LoanEntity> findByUserIdAndEndDateIsNull(Long userId);
}
