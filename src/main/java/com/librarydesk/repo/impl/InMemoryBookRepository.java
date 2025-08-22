package com.librarydesk.repo.impl;

import com.librarydesk.domain.Book;
import com.librarydesk.repo.BookRepository;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryBookRepository implements BookRepository {
    private final Map<String, Book> books = new LinkedHashMap<>();

    @Override
    public void add(Book book) {
        books.put(book.getId(), book);
    }

    @Override
    public Optional<Book> findById(String id) {
        return Optional.ofNullable(books.get(id));
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return books.values().stream().filter(b -> b.getIsbn().equalsIgnoreCase(isbn)).findFirst();
    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(books.values());
    }

    @Override
    public List<Book> search(String query) {
        String q = query.toLowerCase();
        return books.values().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(q) ||
                        b.getAuthor().toLowerCase().contains(q) ||
                        b.getIsbn().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    @Override
    public void remove(String id) {
        books.remove(id);
    }
}
