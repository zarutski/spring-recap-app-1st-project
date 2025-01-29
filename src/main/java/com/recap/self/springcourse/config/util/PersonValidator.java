package com.recap.self.springcourse.config.util;


import com.recap.self.springcourse.config.dao.PersonDAO;
import com.recap.self.springcourse.config.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component // --- so Spring can inject this as the part of the Application
public class PersonValidator implements Validator {

    private final PersonDAO personDAO;

    @Autowired
    public PersonValidator(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    // --- what class is supported by this certain Validator
    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    // --- method usually used with Controller
    @Override
    public void validate(Object target, Errors errors) {
        /*
        // --- supports() method defines only Person.class, so cast can be performed
        Person person = (Person) target;

        // check if email is already exists, if so - put data to the Errors object
        if (personDAO.show(person.getEmail()).isPresent()) {
            errors.rejectValue("email", "", "This email is already taken");
        }
         */
    }
}
