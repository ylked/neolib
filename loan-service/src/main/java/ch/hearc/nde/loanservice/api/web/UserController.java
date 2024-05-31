package ch.hearc.nde.loanservice.api.web;

import ch.hearc.nde.loanservice.api.web.dto.request.UserRequestDTO;
import ch.hearc.nde.loanservice.api.web.dto.response.UserResponseDTO;
import ch.hearc.nde.loanservice.service.UserService;
import ch.hearc.nde.loanservice.service.exception.CardNumberConflict;
import ch.hearc.nde.loanservice.service.exception.EmailConflict;
import ch.hearc.nde.loanservice.service.exception.HasOngoingLoans;
import ch.hearc.nde.loanservice.service.exception.UserNotFound;
import ch.hearc.nde.loanservice.service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService service;

    @PostMapping()
    public @ResponseBody ResponseEntity<?> createUser(@RequestBody UserRequestDTO dto) {
        try {
            User user = service.create(
                    dto.firstName(),
                    dto.lastName(),
                    dto.email(),
                    dto.cardNumber()
            );
            return ResponseEntity.ok(new UserResponseDTO(
                    user.id(),
                    user.firstName(),
                    user.lastName(),
                    user.email(),
                    user.cardNumber()
            ));
        } catch (EmailConflict e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already taken");
        } catch (CardNumberConflict e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Library card already assigned to another user");
        }
    }

    @PatchMapping("/{id}")
    public @ResponseBody ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserRequestDTO dto) {
        try {
            User user = service.update(
                    id,
                    dto.firstName(),
                    dto.lastName(),
                    dto.email(),
                    dto.cardNumber()
            );
            return ResponseEntity.ok(new UserResponseDTO(
                    user.id(),
                    user.firstName(),
                    user.lastName(),
                    user.email(),
                    user.cardNumber()
            ));
        } catch (EmailConflict e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already taken");
        } catch (CardNumberConflict e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Library card already assigned to another user");
        } catch (UserNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @DeleteMapping("/{id}")
    public @ResponseBody ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (UserNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (HasOngoingLoans e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User has ongoing loans, cannot delete account yet");
        }
    }
}
