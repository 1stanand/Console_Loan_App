package com.librarydesk.service;

import com.librarydesk.domain.Book;
import com.librarydesk.domain.Loan;
import com.librarydesk.domain.Member;
import com.librarydesk.repo.impl.InMemoryBookRepository;
import com.librarydesk.repo.impl.InMemoryLoanRepository;
import com.librarydesk.repo.impl.InMemoryMemberRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class LibraryServiceTest {
    @Test
    public void borrowReturnFlow() {
        InMemoryBookRepository br = new InMemoryBookRepository();
        InMemoryMemberRepository mr = new InMemoryMemberRepository();
        InMemoryLoanRepository lr = new InMemoryLoanRepository();
        LibraryService service = new LibraryService(br, mr, lr);
        Book book = service.addBook("Test", "A", 2000, "111", 1);
        Member mem = service.registerMember("M", "m@e", "123");
        Loan loan = service.borrowBook(mem.getId(), book.getId());
        assertEquals(0, book.getCopiesAvailable());
        service.returnBook(loan.getId());
        assertEquals(1, book.getCopiesAvailable());
    }

    @Test
    public void overdueCalculation() {
        InMemoryBookRepository br = new InMemoryBookRepository();
        InMemoryMemberRepository mr = new InMemoryMemberRepository();
        InMemoryLoanRepository lr = new InMemoryLoanRepository();
        LibraryService service = new LibraryService(br, mr, lr);
        Book book = service.addBook("Test", "A", 2000, "222", 1);
        Member mem = service.registerMember("M", "m@e", "123");
        Loan loan = new Loan(book.getId(), mem.getId(), LocalDate.now().minusDays(20), LocalDate.now().minusDays(10));
        lr.add(loan);
        assertEquals(1, service.listOverdueLoans(LocalDate.now()).size());
    }
}
