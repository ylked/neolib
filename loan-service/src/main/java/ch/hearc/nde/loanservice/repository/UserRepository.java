package ch.hearc.nde.loanservice.repository;

import ch.hearc.nde.loanservice.repository.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    boolean existsByCardNumber(String cardNumber);
    boolean existsByEmail(String email);
}
