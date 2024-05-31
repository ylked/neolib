package ch.hearc.nde.loanservice.service;

import ch.hearc.nde.loanservice.exception.CardNumberConflict;
import ch.hearc.nde.loanservice.exception.EmailConflict;
import ch.hearc.nde.loanservice.exception.HasOngoingLoans;
import ch.hearc.nde.loanservice.exception.UserNotFound;
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
    User get(Long id) throws UserNotFound;
}
