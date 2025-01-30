package com.recap.self.springcourse.config.dao;


import com.recap.self.springcourse.config.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String SELECT_SCRIPT = "SELECT * FROM person";
    private static final String SELECT_BY_ID_SCRIPT = "SELECT * FROM person WHERE id=?";
    private static final String INSERT_SCRIPT = "INSERT INTO person (name, year_of_birth) VALUES(?, ?)";
    private static final String UPDATE_SCRIPT = "UPDATE person SET name=?, year_of_birth=? WHERE id=?";
    private static final String DELETE_SCRIPT = "DELETE FROM person WHERE id=?";

    public List<Person> index() {
        return jdbcTemplate.query(SELECT_SCRIPT, new PersonMapper());
    }

    public Person show(int id) {
        return jdbcTemplate.query(SELECT_BY_ID_SCRIPT, new BeanPropertyRowMapper<>(Person.class), new Object[]{id})
                .stream().findAny().orElse(null);
    }

    public void save(Person person) {
        jdbcTemplate.update(INSERT_SCRIPT, person.getName(), person.getYearOfBirth());
    }

    public void update(int id, Person person) {
        jdbcTemplate.update(UPDATE_SCRIPT, person.getName(), person.getYearOfBirth(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update(DELETE_SCRIPT, id);
    }

}
