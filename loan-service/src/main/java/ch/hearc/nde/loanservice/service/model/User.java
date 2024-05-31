package ch.hearc.nde.loanservice.service.model;

public record User(
        Long id,
        String firstName,
        String lastName,
        String email,
        String cardNumber
) {
}
