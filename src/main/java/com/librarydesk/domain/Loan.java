package com.librarydesk.domain;

import java.time.LocalDate;
import java.util.UUID;

public class Loan {
    private final String id;
    private final String bookId;
    private final String memberId;
    private final LocalDate loanDate;
    private final LocalDate dueDate;
    private LocalDate returnDate;

    public Loan(String bookId, String memberId, LocalDate loanDate, LocalDate dueDate) {
        this(UUID.randomUUID().toString(), bookId, memberId, loanDate, dueDate, null);
    }

    public Loan(String id, String bookId, String memberId, LocalDate loanDate, LocalDate dueDate, LocalDate returnDate) {
        this.id = id;
        this.bookId = bookId;
        this.memberId = memberId;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
    }

    public String getId() { return id; }
    public String getBookId() { return bookId; }
    public String getMemberId() { return memberId; }
    public LocalDate getLoanDate() { return loanDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    public boolean isReturned() { return returnDate != null; }

    @Override
    public String toString() {
        return String.format("Loan %s book=%s member=%s due=%s returned=%s", id, bookId, memberId, dueDate, returnDate);
    }
}
