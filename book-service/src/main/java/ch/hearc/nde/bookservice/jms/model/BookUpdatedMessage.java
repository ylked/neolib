package ch.hearc.nde.bookservice.jms.model;

public record BookUpdatedMessage(
        Long id,
        String title,
        String author
) {
}
