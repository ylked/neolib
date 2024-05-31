package ch.hearc.nde.loanservice.api.web.dto.response;

public record UserResponseDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        String cardNumber
) {
}
