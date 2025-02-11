package com.recap.self.springcourse.config.repository;

import com.recap.self.springcourse.config.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findByOwnerId(int id);
}
