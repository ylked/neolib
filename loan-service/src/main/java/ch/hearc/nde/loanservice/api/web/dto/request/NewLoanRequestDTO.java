package ch.hearc.nde.loanservice.api.web.dto.request;

public record NewLoanRequestDTO(
        Long userId,
        Long bookId
) {
}
