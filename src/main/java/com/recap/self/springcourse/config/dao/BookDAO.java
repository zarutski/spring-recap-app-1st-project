package com.recap.self.springcourse.config.dao;

import com.recap.self.springcourse.config.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {

    private final JdbcTemplate jdbcTemplate;

    // queries
    private static final String SELECT_SCRIPT = "SELECT * FROM book";
    private static final String SELECT_BY_ID_SCRIPT = "SELECT * FROM book WHERE id=?";
    private static final String INSERT_SCRIPT = "INSERT INTO book (title, author, publishYear) VALUES(?, ?, ?)";
    private static final String UPDATE_SCRIPT = "UPDATE book SET title=?, author=?, publishYear=? WHERE id=?";
    private static final String DELETE_SCRIPT = "DELETE FROM book WHERE id=?";

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> getAll() {
        return jdbcTemplate.query(SELECT_SCRIPT, new BeanPropertyRowMapper<>(Book.class));
    }

    // TODO: refactor PersonDao by deprecated methods replacement
    public Optional<Book> getBookById(int id) {
        return jdbcTemplate.query(SELECT_BY_ID_SCRIPT, new BeanPropertyRowMapper<>(Book.class), new Object[]{id})
                .stream().findAny();
    }

    public void save(Book book) {
        jdbcTemplate.update(INSERT_SCRIPT, book.getTitle(), book.getAuthor(), book.getPublishYear());
    }

    public void update(int id, Book book) {
        jdbcTemplate.update(UPDATE_SCRIPT, book.getTitle(), book.getAuthor(), book.getPublishYear(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update(DELETE_SCRIPT, id);
    }

}
