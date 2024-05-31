package ch.hearc.nde.bookservice.api.jms.model;

public record BookBorrowedMessage(
        Long bookId
) {
}
