package ch.hearc.nde.loanservice.api.jms.model;

public record BookUpdatedMessage(
        Long id,
        String title,
        String author
) {
}
