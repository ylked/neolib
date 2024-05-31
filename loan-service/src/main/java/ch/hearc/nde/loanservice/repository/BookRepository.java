package ch.hearc.nde.loanservice.repository;

import ch.hearc.nde.loanservice.repository.entity.BookEntity;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<BookEntity, Long> {
}
