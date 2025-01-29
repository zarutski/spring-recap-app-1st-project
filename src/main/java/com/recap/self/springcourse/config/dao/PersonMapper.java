package com.recap.self.springcourse.config.dao;

import com.recap.self.springcourse.config.models.Person;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

// --- ----------------------------------------------------------------------------------
// --- mapping of the SQL query results (just for example, how it can work)
// --- ----------------------------------------------------------------------------------
// --- actually, there's no need to implement own mapper, because BeanPropertyRowMapper
// --- is the base implementation for JavaBean property mapping
// --- ----------------------------------------------------------------------------------
public class PersonMapper implements RowMapper<Person> {

    @Override
    public Person mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Person person = new Person();
        person.setId(resultSet.getInt("id"));
        person.setName(resultSet.getString("name"));
        person.setYearOfBirth(resultSet.getInt("yearOfBirth"));
        return person;
    }

}
