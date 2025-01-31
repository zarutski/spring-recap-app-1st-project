package com.recap.self.springcourse.config.controllers;

import com.recap.self.springcourse.config.dao.BookDAO;
import com.recap.self.springcourse.config.dao.PersonDAO;
import com.recap.self.springcourse.config.models.Book;
import com.recap.self.springcourse.config.models.Person;
import com.recap.self.springcourse.config.util.PersonValidator;
import jakarta.validation.Valid;
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

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonDAO personDAO;
    private final BookDAO bookDAO;
    private final PersonValidator validator;

    public PeopleController(PersonDAO personDAO, BookDAO bookDAO, PersonValidator validator) {
        this.personDAO = personDAO;
        this.bookDAO = bookDAO;
        this.validator = validator;
    }

    @GetMapping
    public String index(Model model) {
        List<Person> index = personDAO.index();
        model.addAttribute("people", index);
        return "people/index";
    }


    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        Person person = personDAO.show(id);
        List<Book> books = bookDAO.getBooksByPerson(id);
        model.addAttribute("person", person);
        model.addAttribute("books", books);
        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping
    public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        validator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            System.out.println("Logging, validation errors occurred while person creation ...");
            return "people/new";
        }
        personDAO.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String update(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", personDAO.show(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    private String update(@PathVariable("id") int id, @ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println("Logging, validation errors occurred while person update ...");
            return "people/edit";
        }
        personDAO.update(id, person);
        return "redirect:/people/{id}";
    }

    @DeleteMapping("/{id}")
    private String delete(@PathVariable("id") int id) {
        personDAO.delete(id);
        return "redirect:/people";
    }

}
