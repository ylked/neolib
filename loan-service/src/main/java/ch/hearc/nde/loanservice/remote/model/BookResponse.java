package ch.hearc.nde.loanservice.remote.model;

public record BookResponse(
        Long id,
        String title,
        String author,
        String isbn,
        String status
) {
}
