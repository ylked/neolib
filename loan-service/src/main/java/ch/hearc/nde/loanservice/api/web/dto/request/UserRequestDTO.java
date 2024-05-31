package ch.hearc.nde.loanservice.api.web.dto.request;

public record UserRequestDTO(
        String firstName,
        String lastName,
        String email,
        String cardNumber
) {
}
