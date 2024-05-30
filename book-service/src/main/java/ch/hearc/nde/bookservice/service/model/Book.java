package ch.hearc.nde.bookservice.service.model;

import ch.hearc.nde.bookservice.repository.entity.BookEntity;
import ch.hearc.nde.bookservice.common.BookStatus;

public record Book(
        Long id,
        String title,
        String author,
        String isbn,
        BookStatus status
) {
    BookEntity toEntity() {
        return new BookEntity(id, title, author, isbn, status);
    }
}
