package run.victor.api.codehouse.controller;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import run.victor.api.codehouse.model.Book;
import run.victor.api.codehouse.repository.BookRepository;
import run.victor.api.codehouse.response.BookDetailResponse;
import run.victor.api.codehouse.response.BooksListResponse;
import run.victor.api.codehouse.request.NewAuthorRequest;
import run.victor.api.codehouse.request.NewBookRequest;

/**
 * @author Victor Wardi - @victorwardi
 */
@RestController
@RequestMapping("/v1/books")
public class BooksController {

    @PersistenceContext
    private EntityManager entityManager;
    //1
    private final BookRepository bookRepository;

    public BooksController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @PostMapping
    @Transactional
    //1
    public Book registerBook(@RequestBody @Valid NewBookRequest newBookRequest) {
        Book book = newBookRequest.toModel(entityManager);
        bookRepository.save(book);
        NewAuthorRequest.builder().build();
        return book;
    }

    @GetMapping()
    public BooksListResponse findAll() {
        //1
        return new BooksListResponse(bookRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findBook(@PathVariable Long id) {

        final Optional<Book> optionalBook = bookRepository.findById(id);

        //1
        if(optionalBook.isEmpty()){
            return ResponseEntity.notFound().build();
       //1
        }else{
            //1
            BookDetailResponse bookDetailResponse = new BookDetailResponse(optionalBook.get());
           return ResponseEntity.ok().body(bookDetailResponse);
        }
    }
}