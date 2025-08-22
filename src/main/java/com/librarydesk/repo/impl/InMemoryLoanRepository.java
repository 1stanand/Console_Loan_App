package com.librarydesk.repo.impl;

import com.librarydesk.domain.Loan;
import com.librarydesk.repo.LoanRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryLoanRepository implements LoanRepository {
    private final Map<String, Loan> loans = new LinkedHashMap<>();

    @Override
    public void add(Loan loan) {
        loans.put(loan.getId(), loan);
    }

    @Override
    public Optional<Loan> findById(String id) {
        return Optional.ofNullable(loans.get(id));
    }

    @Override
    public List<Loan> findAll() {
        return new ArrayList<>(loans.values());
    }

    @Override
    public List<Loan> findActive() {
        return loans.values().stream().filter(l -> !l.isReturned()).collect(Collectors.toList());
    }

    @Override
    public List<Loan> findActiveByBook(String bookId) {
        return findActive().stream().filter(l -> l.getBookId().equals(bookId)).collect(Collectors.toList());
    }

    @Override
    public List<Loan> findActiveByMember(String memberId) {
        return findActive().stream().filter(l -> l.getMemberId().equals(memberId)).collect(Collectors.toList());
    }

    @Override
    public List<Loan> findOverdue(LocalDate today) {
        return findActive().stream().filter(l -> l.getDueDate().isBefore(today)).collect(Collectors.toList());
    }
}
