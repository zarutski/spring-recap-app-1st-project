package com.recap.self.springcourse.config.controllers;

import com.recap.self.springcourse.config.dao.BookDAO;
import com.recap.self.springcourse.config.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {


    private final BookDAO bookDAO;

    @Autowired
    public BookController(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    @GetMapping
    public String getBooks(Model model) {
        List<Book> books = bookDAO.getAll();
        model.addAttribute("books", books);
        return "books/index";
    }

    @GetMapping("/{id}")
    public String getBook(@PathVariable("id") int id, Model model) {
        Book book = bookDAO.getBookById(id).orElse(null);
        model.addAttribute("book", book);
        return "books/show";
    }
}
