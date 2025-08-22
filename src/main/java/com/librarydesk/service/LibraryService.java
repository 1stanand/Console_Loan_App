package com.librarydesk.service;

import com.librarydesk.domain.Book;
import com.librarydesk.domain.Loan;
import com.librarydesk.domain.Member;
import com.librarydesk.domain.ValidationException;
import com.librarydesk.repo.BookRepository;
import com.librarydesk.repo.LoanRepository;
import com.librarydesk.repo.MemberRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class LibraryService {
    private final BookRepository bookRepo;
    private final MemberRepository memberRepo;
    private final LoanRepository loanRepo;

    public LibraryService(BookRepository bookRepo, MemberRepository memberRepo, LoanRepository loanRepo) {
        this.bookRepo = bookRepo;
        this.memberRepo = memberRepo;
        this.loanRepo = loanRepo;
    }

    // region Books
    public Book addBook(String title, String author, int year, String isbn, int copies) {
        bookRepo.findByIsbn(isbn).ifPresent(b -> {throw new ValidationException("ISBN already exists");});
        Book b = new Book(title, author, year, isbn, copies);
        bookRepo.add(b);
        return b;
    }

    public List<Book> listBooks() { return bookRepo.findAll(); }
    public List<Book> searchBooks(String q) { return bookRepo.search(q); }

    public void removeBook(String id) {
        if (!loanRepo.findActiveByBook(id).isEmpty()) {
            throw new ValidationException("Cannot remove book with active loans");
        }
        bookRepo.remove(id);
    }
    // endregion

    // region Members
    public Member registerMember(String name, String email, String phone) {
        Member m = new Member(name, email, phone);
        memberRepo.add(m);
        return m;
    }

    public List<Member> listMembers() { return memberRepo.findAll(); }
    public List<Member> searchMembers(String q) { return memberRepo.search(q); }
    // endregion

    // region Loans
    public Loan borrowBook(String memberId, String bookId) {
        Member member = memberRepo.findById(memberId).orElseThrow(() -> new ValidationException("Member not found"));
        Book book = bookRepo.findById(bookId).orElseThrow(() -> new ValidationException("Book not found"));
        if (book.getCopiesAvailable() <= 0) throw new ValidationException("No copies available");

        book.setCopiesAvailable(book.getCopiesAvailable() - 1);
        LocalDate today = LocalDate.now();
        Loan loan = new Loan(bookId, memberId, today, today.plusDays(14));
        loanRepo.add(loan);
        return loan;
    }

    public void returnBook(String loanId) {
        Loan loan = loanRepo.findById(loanId).orElseThrow(() -> new ValidationException("Loan not found"));
        if (loan.isReturned()) throw new ValidationException("Already returned");
        Book book = bookRepo.findById(loan.getBookId()).orElseThrow(() -> new ValidationException("Book not found"));
        loan.setReturnDate(LocalDate.now());
        book.setCopiesAvailable(book.getCopiesAvailable() + 1);
    }

    public List<Loan> listActiveLoans() { return loanRepo.findActive(); }
    public List<Loan> listOverdueLoans(LocalDate today) { return loanRepo.findOverdue(today); }
    // endregion

    // Reports
    public InventorySummary inventorySummary() {
        List<Book> books = bookRepo.findAll();
        long titles = books.size();
        long totalCopies = books.stream().mapToLong(Book::getCopiesTotal).sum();
        long available = books.stream().mapToLong(Book::getCopiesAvailable).sum();
        return new InventorySummary(titles, totalCopies, available);
    }

    public List<Map.Entry<Book, Long>> topLoanedBooks(int limit) {
        Map<String, Long> counts = loanRepo.findAll().stream()
                .collect(Collectors.groupingBy(Loan::getBookId, Collectors.counting()));
        List<Map.Entry<Book, Long>> list = counts.entrySet().stream()
                .map(e -> Map.entry(bookRepo.findById(e.getKey()).orElse(null), e.getValue()))
                .filter(e -> e.getKey() != null)
                .sorted(Map.Entry.<Book, Long>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toList());
        return list;
    }

    public static class InventorySummary {
        public final long totalTitles;
        public final long totalCopies;
        public final long availableCopies;
        public InventorySummary(long totalTitles, long totalCopies, long availableCopies) {
            this.totalTitles = totalTitles;
            this.totalCopies = totalCopies;
            this.availableCopies = availableCopies;
        }
    }
}
