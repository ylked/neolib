package ch.hearc.nde.loanservice.service.impl;

import ch.hearc.nde.loanservice.repository.UserRepository;
import ch.hearc.nde.loanservice.repository.entity.UserEntity;
import ch.hearc.nde.loanservice.service.LoanService;
import ch.hearc.nde.loanservice.service.UserService;
import ch.hearc.nde.loanservice.service.exception.CardNumberConflict;
import ch.hearc.nde.loanservice.service.exception.EmailConflict;
import ch.hearc.nde.loanservice.service.exception.HasOngoingLoans;
import ch.hearc.nde.loanservice.service.exception.UserNotFound;
import ch.hearc.nde.loanservice.service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoanService loanService;

    @Override
    public User create(
            String firstName,
            String lastName,
            String email,
            String cardNumber
    ) throws EmailConflict, CardNumberConflict {
        if (userRepository.existsByEmail(email)) {
            throw new EmailConflict();
        }
        if (userRepository.existsByCardNumber(cardNumber)) {
            throw new CardNumberConflict();
        }

        UserEntity entity = new UserEntity();
        entity.setFirstName(firstName);
        entity.setLastName(lastName);
        entity.setEmail(email);
        entity.setCardNumber(cardNumber);

        entity = userRepository.save(entity);
        return getUserFromEntity(entity);
    }

    @Override
    public User update(
            Long id,
            String firstName,
            String lastName,
            String email,
            String cardNumber
    ) throws EmailConflict, CardNumberConflict, UserNotFound {
        UserEntity entity = userRepository.findById(id).orElseThrow(UserNotFound::new);

        if (email != null
                && userRepository.existsByEmail(email)
                && !entity.getEmail().equals(email)
        ) {
            throw new EmailConflict();
        }
        if (cardNumber != null
                && userRepository.existsByCardNumber(cardNumber)
                && !entity.getCardNumber().equals(cardNumber)
        ) {
            throw new CardNumberConflict();
        }

        if (firstName != null) {
            entity.setFirstName(firstName);
        }

        if (lastName != null) {
            entity.setLastName(lastName);
        }

        if (email != null) {
            entity.setEmail(email);
        }

        if (cardNumber != null) {
            entity.setCardNumber(cardNumber);
        }

        entity = userRepository.save(entity);
        return getUserFromEntity(entity);
    }

    @Override
    public void delete(Long id) throws UserNotFound, HasOngoingLoans {
        UserEntity entity = userRepository.findById(id).orElseThrow(UserNotFound::new);

        if (!loanService.borrowedBooks(entity.getId()).isEmpty()) {
            throw new HasOngoingLoans();
        }

        userRepository.delete(entity);
    }

    private User getUserFromEntity(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getCardNumber()
        );
    }
}
