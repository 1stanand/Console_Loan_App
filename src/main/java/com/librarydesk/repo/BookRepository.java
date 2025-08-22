package com.librarydesk.repo;

import com.librarydesk.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    void add(Book book);
    Optional<Book> findById(String id);
    Optional<Book> findByIsbn(String isbn);
    List<Book> findAll();
    List<Book> search(String query);
    void remove(String id);
}
