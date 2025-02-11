package com.recap.self.springcourse.config.controllers;

import com.recap.self.springcourse.config.models.Book;
import com.recap.self.springcourse.config.models.Person;
import com.recap.self.springcourse.config.service.BookService;
import com.recap.self.springcourse.config.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final PersonService personService;

    @Autowired
    public BookController(BookService bookService, PersonService personService) {
        this.bookService = bookService;
        this.personService = personService;
    }

    @GetMapping
    public String getBooks(@RequestParam(value = "page", required = false) Integer page,
                           @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                           @RequestParam(value = "sort_by_year", required = false) boolean sortByYear,
                           Model model) {
        List<Book> books = findBooks(page, booksPerPage, sortByYear);
        model.addAttribute("books", books);
        return "books/index";
    }

    @GetMapping("/search")
    public String searchPage(@RequestParam(value = "q", required = false) String query,
                             Model model) {
        List<Book> books = bookService.findByTitle(query);
        model.addAttribute("books", books);
        model.addAttribute("query", query);
        return "books/search";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        Book book = bookService.findById(id).orElse(null);
        populateOwnerAttribute(model, id);
        model.addAttribute("book", book);
        return "books/show";
    }

    @GetMapping("/new")
    public String newBookPage(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping
    public String newBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/new";
        }
        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookService.findById(id).orElse(null));
        return "books/edit";
    }

    @PatchMapping("{id}")
    public String editBook(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if ((bindingResult.hasErrors())) {
            System.out.println("Logging, validation errors occurred while book update ...");
            return "books/edit";
        }
        bookService.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("{id}")
    public String deleteBook(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("{id}/release")
    public String releaseBook(@PathVariable("id") int id) {
        bookService.release(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("{id}/assign")
    public String assignBook(@PathVariable("id") int id, @ModelAttribute("person") Person person) {
        bookService.assignToPerson(id, person);
        return "redirect:/books/" + id;
    }

    private void populateOwnerAttribute(Model model, int id) {
        Optional<Person> personByBookId = personService.findByBookId(id);
        if (personByBookId.isPresent()) {
            model.addAttribute("user", personByBookId.get());
        } else {
            model.addAttribute("people", personService.findAll());
        }
    }

    private List<Book> findBooks(Integer page, Integer booksPerPage, boolean sortByYear) {
        if (page == null || booksPerPage == null) {
            return bookService.findAll(sortByYear);
        }
        return bookService.findAll(page, booksPerPage, sortByYear);
    }
}
