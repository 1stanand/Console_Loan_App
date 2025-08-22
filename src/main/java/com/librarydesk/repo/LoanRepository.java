package com.librarydesk.repo;

import com.librarydesk.domain.Loan;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LoanRepository {
    void add(Loan loan);
    Optional<Loan> findById(String id);
    List<Loan> findAll();
    List<Loan> findActive();
    List<Loan> findActiveByBook(String bookId);
    List<Loan> findActiveByMember(String memberId);
    List<Loan> findOverdue(LocalDate today);
}
