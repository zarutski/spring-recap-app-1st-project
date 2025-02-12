package com.recap.self.springcourse.config.service;

import com.recap.self.springcourse.config.models.Book;
import com.recap.self.springcourse.config.models.Person;
import com.recap.self.springcourse.config.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;
    private static final String SORT_BY_YEAR = "publishYear";

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll(boolean sortByYear) {
        if (sortByYear) {
            return bookRepository.findAll(Sort.by(SORT_BY_YEAR));
        }
        return bookRepository.findAll();
    }

    public List<Book> findAll(int page, int booksPerPage, boolean sortByYear) {
        if (sortByYear) {
            return bookRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by(SORT_BY_YEAR))).getContent();
        }
        return bookRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }

    public Optional<Book> findById(int id) {
        return bookRepository.findById(id);
    }

    public List<Book> findByPersonId(int id) {
        List<Book> booksByOwner = bookRepository.findByOwnerId(id);
        markExpired(booksByOwner);
        return booksByOwner;
    }

    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void update(int id, Book book) {
        book.setId(id);
        bookRepository.save(book);
    }

    @Transactional
    public void delete(int id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    public void release(int id) {
        Book book = bookRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        book.setOwner(null);
        book.setTakenAt(null);
    }

    @Transactional
    public void assignToPerson(int id, Person person) {
        Book book = bookRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        book.setOwner(person);
        book.setTakenAt(new Date());
    }

    public List<Book> findByTitle(String title) {
        return bookRepository.findByTitleContainsIgnoreCase(title);
    }

    private Date getAllowedDate() {
        LocalDate expirationMark = LocalDate.now().minusDays(10);
        return Date.from(expirationMark.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private void markExpired(List<Book> books) {
        Date allowedDate = getAllowedDate();
        books.forEach(b -> {
            if (b.getTakenAt() != null && b.getTakenAt().before(allowedDate)) {
                b.setExpired(true);
            }
        });
    }
}
