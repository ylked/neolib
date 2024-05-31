package ch.hearc.nde.loanservice.remote.model;

public record BookBody(
        Long id,
        String title,
        String author,
        String isbn,
        String status
) {
}
