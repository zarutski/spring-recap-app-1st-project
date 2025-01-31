package com.recap.self.springcourse.config.controllers;

import com.recap.self.springcourse.config.dao.BookDAO;
import com.recap.self.springcourse.config.dao.PersonDAO;
import com.recap.self.springcourse.config.models.Book;
import com.recap.self.springcourse.config.models.Person;
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

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookDAO bookDAO;
    private final PersonDAO personDAO;

    @Autowired
    public BookController(BookDAO bookDAO, PersonDAO personDAO) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
    }

    @GetMapping
    public String getBooks(Model model) {
        List<Book> books = bookDAO.getAll();
        model.addAttribute("books", books);
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        Book book = bookDAO.getBookById(id).orElse(null);
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
        bookDAO.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookDAO.getBookById(id).orElse(null));
        return "books/edit";
    }

    @PatchMapping("{id}")
    public String editBook(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if ((bindingResult.hasErrors())) {
            System.out.println("Logging, validation errors occurred while book update ...");
            return "books/edit";
        }
        bookDAO.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("{id}")
    public String deleteBook(@PathVariable("id") int id) {
        bookDAO.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("{id}/release")
    public String releaseBook(@PathVariable("id") int id) {
        bookDAO.release(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("{id}/assign")
    public String assignBook(@PathVariable("id") int id, @ModelAttribute("person") Person person) {
        bookDAO.assignToPerson(id, person.getId());
        return "redirect:/books/" + id;
    }

    private void populateOwnerAttribute(Model model, int id) {
        Optional<Person> personByBookId = personDAO.getPersonByBookId(id);
        if (personByBookId.isPresent()) {
            model.addAttribute("user", personByBookId.get());
        } else {
            model.addAttribute("people", personDAO.index());
        }
    }
}
