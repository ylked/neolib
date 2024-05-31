package ch.hearc.nde.bookservice.api.web;


import ch.hearc.nde.bookservice.api.web.dto.request.BookRequestDTO;
import ch.hearc.nde.bookservice.api.web.dto.response.StringMessage;
import ch.hearc.nde.bookservice.api.web.dto.response.BookResponseDTO;
import ch.hearc.nde.bookservice.service.BookService;
import ch.hearc.nde.bookservice.service.exception.IllegalOperation;
import ch.hearc.nde.bookservice.service.exception.NotFound;
import ch.hearc.nde.bookservice.service.model.Book;
import ch.hearc.nde.bookservice.common.BookStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService service;

    @GetMapping()
    public @ResponseBody ResponseEntity<List<BookResponseDTO>> index() {
        return ResponseEntity.ok(service.get().stream().map(this::getDTOFromModel).toList());
    }

    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<BookResponseDTO> get(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(getDTOFromModel(service.get(id)));
        } catch (NotFound e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping()
    public @ResponseBody ResponseEntity<BookResponseDTO> create(@RequestBody BookRequestDTO dto) {
        Book book = service.create(dto.title(), dto.author(), dto.isbn());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(book.id())
                .toUri();

        return ResponseEntity.created(location).body(getDTOFromModel(book));
    }

    @PatchMapping("/{id}")
    public @ResponseBody ResponseEntity<BookResponseDTO> update(@PathVariable Long id, @RequestBody BookRequestDTO dto) {
        try {
            if (dto.title() != null) {
                return ResponseEntity.ok(getDTOFromModel(service.rename(id, dto.title())));
            } else if (dto.author() != null) {
                return ResponseEntity.ok(getDTOFromModel(service.changeAuthor(id, dto.author())));
            } else if (dto.isbn() != null) {
                return ResponseEntity.ok(getDTOFromModel(service.changeIsbn(id, dto.isbn())));
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (NotFound e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/borrowed")
    public @ResponseBody ResponseEntity<BookResponseDTO> borrow(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(getDTOFromModel(service.changeStatus(id, BookStatus.BORROWED)));
        } catch (NotFound e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/returned")
    public @ResponseBody ResponseEntity<BookResponseDTO> returnBook(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(getDTOFromModel(service.changeStatus(id, BookStatus.AVAILABLE)));
        } catch (NotFound e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/lost")
    public @ResponseBody ResponseEntity<BookResponseDTO> lost(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(getDTOFromModel(service.changeStatus(id, BookStatus.LOST)));
        } catch (NotFound e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/blocked")
    public @ResponseBody ResponseEntity<BookResponseDTO> block(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(getDTOFromModel(service.changeStatus(id, BookStatus.BLOCKED)));
        } catch (NotFound e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public @ResponseBody ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (NotFound e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalOperation e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new StringMessage(
                            "Cannot delete a borrowed book. Please return it first or mark it as lost."
                    ));
        }
    }

    private BookResponseDTO getDTOFromModel(Book book) {
        return new BookResponseDTO(
                book.id(),
                book.title(),
                book.author(),
                book.isbn(),
                book.status().name()
        );
    }
}
