package com.recap.self.springcourse.config.controllers;

import com.recap.self.springcourse.config.dao.PersonDAO;
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
    private final PersonValidator validator;

    public PeopleController(PersonDAO personDAO, PersonValidator validator) {
        this.personDAO = personDAO;
        this.validator = validator;
    }

    @GetMapping
    public String index(Model model) {
        // ---- retrieve all available data from DAO, pass data to the View
        List<Person> index = personDAO.index();
        model.addAttribute("people", index);
        return "people/index";
    }


    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        Person person = personDAO.show(id);
        model.addAttribute("person", person);
        return "people/show";
    }

    // --- ----------------------------------------------------
    // --- will pass new object Person to the view "people/new"
    // --- ----------------------------------------------------
    //     [object Person will be added as a Model attribute,
    //      then can be accessed and populated via thymeleaf]
    // --- ----------------------------------------------------
    // --- this object then will be populated with user's input
    // --- ----------------------------------------------------
    /*
    @GetMapping("/new")
    public String newPerson(Model model) {
        model.addAttribute("person", new Person());
        return "people/new";
    }
     */

    // --- ----------------------------------------------------
    // --- analog of the method above
    // --- ----------------------------------------------------
    //     1. creates an empty object [new Person]
    //     2. adds this object as a Model's attribute [person]
    //     3. Model with "person" attribute (new object)
    //        will be passed to the view "people/new"
    // --- ----------------------------------------------------
    // --- this object then will be populated with user's input
    // --- ----------------------------------------------------
    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    // --- ----------------------------------------------------
    // --- retrieve data from request's body
    // --- creates new Person object
    // --- populate object by request's data via setters
    // --- add object as an attribute "person" to the Model object
    // --- passes Model object to the view "people/success"
    // --- ----------------------------------------------------
    @PostMapping
    public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        // validation VIA custom Spring validator
        validator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            System.out.println("Logging, validation errors occurred while person creation ...");
            return "people/new";
        }
        personDAO.save(person);
        return "redirect:/people"; // --- perform redirect as an option of successful creation
    }

    // GET -- update
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
