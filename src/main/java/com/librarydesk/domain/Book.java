package com.librarydesk.domain;

import java.util.UUID;

public class Book {
    private final String id;
    private String title;
    private String author;
    private int publishedYear;
    private String isbn;
    private int copiesTotal;
    private int copiesAvailable;

    public Book(String title, String author, int publishedYear, String isbn, int copiesTotal) {
        this(UUID.randomUUID().toString(), title, author, publishedYear, isbn, copiesTotal, copiesTotal);
    }

    public Book(String id, String title, String author, int publishedYear, String isbn, int copiesTotal, int copiesAvailable) {
        this.id = id;
        setTitle(title);
        setAuthor(author);
        setPublishedYear(publishedYear);
        setIsbn(isbn);
        setCopiesTotal(copiesTotal);
        setCopiesAvailable(copiesAvailable);
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getPublishedYear() { return publishedYear; }
    public String getIsbn() { return isbn; }
    public int getCopiesTotal() { return copiesTotal; }
    public int getCopiesAvailable() { return copiesAvailable; }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) throw new ValidationException("Title required");
        this.title = title.trim();
    }

    public void setAuthor(String author) {
        if (author == null || author.trim().isEmpty()) throw new ValidationException("Author required");
        this.author = author.trim();
    }

    public void setPublishedYear(int year) {
        if (year < 0) throw new ValidationException("Year must be positive");
        this.publishedYear = year;
    }

    public void setIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) throw new ValidationException("ISBN required");
        this.isbn = isbn.trim();
    }

    public void setCopiesTotal(int copiesTotal) {
        if (copiesTotal < 0) throw new ValidationException("Total copies must be >=0");
        this.copiesTotal = copiesTotal;
    }

    public void setCopiesAvailable(int copiesAvailable) {
        if (copiesAvailable < 0 || copiesAvailable > copiesTotal) throw new ValidationException("Available copies must be between 0 and total");
        this.copiesAvailable = copiesAvailable;
    }

    @Override
    public String toString() {
        return String.format("%s - %s by %s (%d)", id, title, author, publishedYear);
    }
}
