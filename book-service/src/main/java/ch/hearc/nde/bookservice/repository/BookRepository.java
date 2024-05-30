package ch.hearc.nde.bookservice.repository;

import ch.hearc.nde.bookservice.repository.entity.BookEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<BookEntity, Long> {
}