package ch.hearc.nde.loanservice.service;

import ch.hearc.nde.loanservice.service.exception.CardNumberConflict;
import ch.hearc.nde.loanservice.service.exception.EmailConflict;
import ch.hearc.nde.loanservice.service.exception.HasOngoingLoans;
import ch.hearc.nde.loanservice.service.exception.UserNotFound;
import ch.hearc.nde.loanservice.service.model.User;

public interface UserService {
    User create(
            String firstName,
            String lastName,
            String email,
            String cardNumber
    ) throws EmailConflict, CardNumberConflict;

    User update(
            Long id,
            String firstName,
            String lastName,
            String email,
            String cardNumber
    ) throws EmailConflict, CardNumberConflict, UserNotFound;

    void delete(Long id) throws UserNotFound, HasOngoingLoans;
}
