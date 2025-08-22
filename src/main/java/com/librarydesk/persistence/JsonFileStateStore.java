package com.librarydesk.persistence;

import com.librarydesk.domain.Book;
import com.librarydesk.domain.Loan;
import com.librarydesk.domain.Member;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

/**
 * Placeholder implementation for future JSON persistence.
 */
public class JsonFileStateStore implements StateStore {
    private final Path file;

    public JsonFileStateStore(Path file) {
        this.file = file;
    }

    @Override
    public void saveAll(Collection<Book> books, Collection<Member> members, Collection<Loan> loans) throws IOException {
        // TODO: implement JSON serialization
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public LoadedData loadAll() throws IOException {
        // TODO: implement JSON loading
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
