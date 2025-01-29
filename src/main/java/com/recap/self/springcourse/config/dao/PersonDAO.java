package com.recap.self.springcourse.config.dao;


import com.recap.self.springcourse.config.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // queries
    private static final String SELECT_SCRIPT = "SELECT * FROM person";
    private static final String SELECT_BY_ID_SCRIPT = "SELECT * FROM person WHERE id=?";


    // private static final String SELECT_BY_EMAIL_SCRIPT = "SELECT * FROM person WHERE email=?";


    private static final String INSERT_SCRIPT = "INSERT INTO person (name, yearOfBirth) VALUES(?, ?)";
    private static final String UPDATE_SCRIPT = "UPDATE person SET name=?, yearOfBirth=? WHERE id=?";
    private static final String DELETE_SCRIPT = "DELETE FROM person WHERE id=?";

    public List<Person> index() {
        return jdbcTemplate.query(SELECT_SCRIPT, new PersonMapper());
    }

    public Person show(int id) {
        // --- rowMapper: no need to implement own mapper, because BeanPropertyRowMapper --- is the base implementation for JavaBean property mapping
        return jdbcTemplate.query(SELECT_BY_ID_SCRIPT, new Object[]{id}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny().orElse(null);

    }

    /*
    public Optional<Person> show(String email) {
        return jdbcTemplate.query(SELECT_BY_EMAIL_SCRIPT, new Object[]{email}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny();
    }
     */

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
