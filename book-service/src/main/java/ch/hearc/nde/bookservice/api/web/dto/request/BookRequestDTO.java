package ch.hearc.nde.bookservice.api.web.dto.request;

public record BookRequestDTO(
        String title,
        String author,
        String isbn
) {
}
