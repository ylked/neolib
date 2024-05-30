package ch.hearc.nde.bookservice.api.web.dto.response;

public record BookResponseDTO(
        Long id,
        String title,
        String author,
        String isbn,
        String status
) {
}
