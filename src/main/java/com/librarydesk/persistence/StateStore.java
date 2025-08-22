package com.librarydesk.persistence;

import com.librarydesk.domain.Book;
import com.librarydesk.domain.Loan;
import com.librarydesk.domain.Member;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public interface StateStore {
    void saveAll(Collection<Book> books, Collection<Member> members, Collection<Loan> loans) throws IOException;
    LoadedData loadAll() throws IOException;

    class LoadedData {
        public final List<Book> books;
        public final List<Member> members;
        public final List<Loan> loans;

        public LoadedData(List<Book> books, List<Member> members, List<Loan> loans) {
            this.books = books;
            this.members = members;
            this.loans = loans;
        }
    }
}
