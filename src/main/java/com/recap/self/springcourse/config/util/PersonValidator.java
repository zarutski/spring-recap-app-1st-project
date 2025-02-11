package com.recap.self.springcourse.config.util;

import com.recap.self.springcourse.config.models.Person;
import com.recap.self.springcourse.config.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {

    private final PersonService personService;

    @Autowired
    public PersonValidator(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if (personService.findByName(person.getName()).isPresent()) {
            errors.rejectValue("name", "", "Person with this name already exists");
        }
    }
}
