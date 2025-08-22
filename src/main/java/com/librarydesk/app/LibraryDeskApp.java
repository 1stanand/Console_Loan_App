package com.librarydesk.app;

import com.librarydesk.domain.Book;
import com.librarydesk.domain.Loan;
import com.librarydesk.domain.Member;
import com.librarydesk.domain.ValidationException;
import com.librarydesk.repo.impl.InMemoryBookRepository;
import com.librarydesk.repo.impl.InMemoryLoanRepository;
import com.librarydesk.repo.impl.InMemoryMemberRepository;
import com.librarydesk.service.LibraryService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LibraryDeskApp {
    private final LibraryService service;

    public LibraryDeskApp() {
        service = new LibraryService(new InMemoryBookRepository(), new InMemoryMemberRepository(), new InMemoryLoanRepository());
        seedData();
    }

    private void seedData() {
        // books
        service.addBook("The Hobbit", "J.R.R. Tolkien", 1937, "9780547928227", 5);
        service.addBook("1984", "George Orwell", 1949, "9780451524935", 3);
        service.addBook("Clean Code", "Robert C. Martin", 2008, "9780132350884", 2);
        service.addBook("Effective Java", "Joshua Bloch", 2018, "9780134685991", 2);
        service.addBook("Dune", "Frank Herbert", 1965, "9780441013593", 4);
        service.addBook("The Pragmatic Programmer", "Andrew Hunt", 1999, "9780201616224", 1);
        // members
        service.registerMember("Alice", "alice@example.com", "111-1111");
        service.registerMember("Bob", "bob@example.com", "222-2222");
        service.registerMember("Carol", "carol@example.com", "333-3333");
    }

    public static void main(String[] args) {
        new LibraryDeskApp().run();
    }

    private void run() {
        showBanner();
        boolean running = true;
        while (running) {
            System.out.println(Ansi.colorize("\nMain Menu", Ansi.WHITE, Ansi.BG_BLUE));
            System.out.println(Ansi.colorize("1) Books", Ansi.CYAN));
            System.out.println(Ansi.colorize("2) Members", Ansi.CYAN));
            System.out.println(Ansi.colorize("3) Loans", Ansi.CYAN));
            System.out.println(Ansi.colorize("4) Reports", Ansi.CYAN));
            System.out.println(Ansi.colorize("0) Exit", Ansi.CYAN));
            int choice = ConsoleIO.readInt("Choose: ");
            switch (choice) {
                case 1 -> booksMenu();
                case 2 -> membersMenu();
                case 3 -> loansMenu();
                case 4 -> reportsMenu();
                case 0 -> running = false;
                default -> System.out.println(Ansi.colorize("Invalid option", Ansi.RED));
            }
        }
        System.out.println(Ansi.colorize("Goodbye!", Ansi.GREEN));
    }

    private void showBanner() {
        String banner = "*** LibraryDesk ***";
        System.out.println(Ansi.colorize(banner, Ansi.BLACK, Ansi.BG_CYAN));
    }

    // Books submenu
    private void booksMenu() {
        boolean back = false;
        while (!back) {
            System.out.println(Ansi.colorize("\nBooks", Ansi.WHITE, Ansi.BG_BLUE));
            System.out.println(Ansi.colorize("1) Add", Ansi.CYAN));
            System.out.println(Ansi.colorize("2) List", Ansi.CYAN));
            System.out.println(Ansi.colorize("3) Search", Ansi.CYAN));
            System.out.println(Ansi.colorize("4) Remove", Ansi.CYAN));
            System.out.println(Ansi.colorize("0) Back", Ansi.CYAN));
            int ch = ConsoleIO.readInt("Choose: ");
            try {
                switch (ch) {
                    case 1 -> addBook();
                    case 2 -> listBooks(service.listBooks());
                    case 3 -> {
                        String q = ConsoleIO.readNonEmpty("Search query: ");
                        listBooks(service.searchBooks(q));
                    }
                    case 4 -> {
                        String id = ConsoleIO.readNonEmpty("Book id: ");
                        service.removeBook(id.trim());
                        System.out.println(Ansi.colorize("Removed", Ansi.GREEN));
                    }
                    case 0 -> back = true;
                    default -> System.out.println(Ansi.colorize("Invalid option", Ansi.RED));
                }
            } catch (ValidationException e) {
                System.out.println(Ansi.colorize(e.getMessage(), Ansi.RED));
            }
        }
    }

    private void addBook() {
        String title = ConsoleIO.readNonEmpty("Title: ");
        String author = ConsoleIO.readNonEmpty("Author: ");
        int year = ConsoleIO.readInt("Published year: ");
        String isbn = ConsoleIO.readNonEmpty("ISBN: ");
        int copies = ConsoleIO.readInt("Total copies: ");
        try {
            Book b = service.addBook(title, author, year, isbn, copies);
            System.out.println(Ansi.colorize("Added book id=" + b.getId(), Ansi.GREEN));
        } catch (ValidationException e) {
            System.out.println(Ansi.colorize(e.getMessage(), Ansi.RED));
        }
    }

    private void listBooks(List<Book> books) {
        if (books.isEmpty()) {
            System.out.println(Ansi.colorize("No books found", Ansi.YELLOW));
            return;
        }
        int page = 0;
        int pageSize = 10;
        while (page * pageSize < books.size()) {
            int from = page * pageSize;
            int to = Math.min(from + pageSize, books.size());
            List<String[]> rows = new ArrayList<>();
            for (Book b : books.subList(from, to)) {
                rows.add(new String[]{b.getId(), b.getTitle(), b.getAuthor(), String.valueOf(b.getCopiesAvailable()) + "/" + b.getCopiesTotal()});
            }
            TablePrinter.printTable(new String[]{"ID", "Title", "Author", "Avail"}, rows);
            page++;
            if (to < books.size()) {
                String next = ConsoleIO.readLine("--More-- press Enter for next page or q to quit: ");
                if (next.equalsIgnoreCase("q")) break;
            }
        }
    }

    // Members submenu
    private void membersMenu() {
        boolean back = false;
        while (!back) {
            System.out.println(Ansi.colorize("\nMembers", Ansi.WHITE, Ansi.BG_BLUE));
            System.out.println(Ansi.colorize("1) Register", Ansi.CYAN));
            System.out.println(Ansi.colorize("2) List", Ansi.CYAN));
            System.out.println(Ansi.colorize("3) Search", Ansi.CYAN));
            System.out.println(Ansi.colorize("0) Back", Ansi.CYAN));
            int ch = ConsoleIO.readInt("Choose: ");
            switch (ch) {
                case 1 -> registerMember();
                case 2 -> listMembers(service.listMembers());
                case 3 -> {
                    String q = ConsoleIO.readNonEmpty("Search query: ");
                    listMembers(service.searchMembers(q));
                }
                case 0 -> back = true;
                default -> System.out.println(Ansi.colorize("Invalid option", Ansi.RED));
            }
        }
    }

    private void registerMember() {
        String name = ConsoleIO.readNonEmpty("Name: ");
        String email = ConsoleIO.readNonEmpty("Email: ");
        String phone = ConsoleIO.readNonEmpty("Phone: ");
        try {
            Member m = service.registerMember(name, email, phone);
            System.out.println(Ansi.colorize("Member id=" + m.getId(), Ansi.GREEN));
        } catch (ValidationException e) {
            System.out.println(Ansi.colorize(e.getMessage(), Ansi.RED));
        }
    }

    private void listMembers(List<Member> members) {
        if (members.isEmpty()) {
            System.out.println(Ansi.colorize("No members found", Ansi.YELLOW));
            return;
        }
        List<String[]> rows = new ArrayList<>();
        for (Member m : members) {
            rows.add(new String[]{m.getId(), m.getName(), m.getEmail(), m.getPhone()});
        }
        TablePrinter.printTable(new String[]{"ID", "Name", "Email", "Phone"}, rows);
    }

    // Loans submenu
    private void loansMenu() {
        boolean back = false;
        while (!back) {
            System.out.println(Ansi.colorize("\nLoans", Ansi.WHITE, Ansi.BG_BLUE));
            System.out.println(Ansi.colorize("1) Borrow", Ansi.CYAN));
            System.out.println(Ansi.colorize("2) Return", Ansi.CYAN));
            System.out.println(Ansi.colorize("3) List Active", Ansi.CYAN));
            System.out.println(Ansi.colorize("4) List Overdue", Ansi.CYAN));
            System.out.println(Ansi.colorize("0) Back", Ansi.CYAN));
            int ch = ConsoleIO.readInt("Choose: ");
            switch (ch) {
                case 1 -> borrowFlow();
                case 2 -> returnFlow();
                case 3 -> listLoans(service.listActiveLoans());
                case 4 -> listOverdue();
                case 0 -> back = true;
                default -> System.out.println(Ansi.colorize("Invalid option", Ansi.RED));
            }
        }
    }

    private void borrowFlow() {
        String memberId = pickMember();
        if (memberId == null) return;
        String bookId = pickBook();
        if (bookId == null) return;
        try {
            Loan loan = service.borrowBook(memberId, bookId);
            System.out.println(Ansi.colorize("Due date: " + loan.getDueDate(), Ansi.GREEN));
        } catch (ValidationException e) {
            System.out.println(Ansi.colorize(e.getMessage(), Ansi.RED));
        }
    }

    private String pickMember() {
        String input = ConsoleIO.readNonEmpty("Member id or 's' to search: ");
        if (input.equalsIgnoreCase("s")) {
            String q = ConsoleIO.readNonEmpty("Search member: ");
            List<Member> ms = service.searchMembers(q);
            listMembers(ms);
            if (ms.isEmpty()) return null;
            int idx = ConsoleIO.chooseFromList(ms.size(), "Choose index: ");
            return ms.get(idx).getId();
        }
        return input;
    }

    private String pickBook() {
        String input = ConsoleIO.readNonEmpty("Book id or 's' to search: ");
        if (input.equalsIgnoreCase("s")) {
            String q = ConsoleIO.readNonEmpty("Search book: ");
            List<Book> bs = service.searchBooks(q);
            listBooks(bs);
            if (bs.isEmpty()) return null;
            int idx = ConsoleIO.chooseFromList(bs.size(), "Choose index: ");
            return bs.get(idx).getId();
        }
        return input;
    }

    private void returnFlow() {
        String input = ConsoleIO.readNonEmpty("Loan id or 's' to search by member: ");
        String loanId = input;
        if (input.equalsIgnoreCase("s")) {
            String q = ConsoleIO.readNonEmpty("Member search: ");
            List<Member> ms = service.searchMembers(q);
            listMembers(ms);
            if (ms.isEmpty()) return;
            int idx = ConsoleIO.chooseFromList(ms.size(), "Choose member index: ");
            String memberId = ms.get(idx).getId();
            List<Loan> loans = new ArrayList<>();
            for (Loan l : service.listActiveLoans()) if (l.getMemberId().equals(memberId)) loans.add(l);
            if (loans.isEmpty()) {
                System.out.println(Ansi.colorize("No active loans", Ansi.YELLOW));
                return;
            }
            listLoans(loans);
            int lidx = ConsoleIO.chooseFromList(loans.size(), "Choose loan index: ");
            loanId = loans.get(lidx).getId();
        }
        try {
            service.returnBook(loanId);
            System.out.println(Ansi.colorize("Returned", Ansi.GREEN));
        } catch (ValidationException e) {
            System.out.println(Ansi.colorize(e.getMessage(), Ansi.RED));
        }
    }

    private void listLoans(List<Loan> loans) {
        if (loans.isEmpty()) {
            System.out.println(Ansi.colorize("No loans", Ansi.YELLOW));
            return;
        }
        List<String[]> rows = new ArrayList<>();
        for (Loan l : loans) {
            rows.add(new String[]{l.getId(), l.getBookId(), l.getMemberId(), l.getDueDate().toString(), l.getReturnDate() == null ? "" : l.getReturnDate().toString()});
        }
        TablePrinter.printTable(new String[]{"ID", "Book", "Member", "Due", "Returned"}, rows);
    }

    private void listOverdue() {
        List<Loan> loans = service.listOverdueLoans(LocalDate.now());
        if (loans.isEmpty()) {
            System.out.println(Ansi.colorize("No overdue loans", Ansi.YELLOW));
            return;
        }
        for (Loan l : loans) {
            long days = java.time.temporal.ChronoUnit.DAYS.between(l.getDueDate(), LocalDate.now());
            String color = days > 7 ? Ansi.RED : Ansi.YELLOW;
            System.out.println(Ansi.colorize(l.getId() + " book=" + l.getBookId() + " member=" + l.getMemberId() + " days=" + days, color));
        }
    }

    // Reports submenu
    private void reportsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println(Ansi.colorize("\nReports", Ansi.WHITE, Ansi.BG_BLUE));
            System.out.println(Ansi.colorize("1) Inventory summary", Ansi.CYAN));
            System.out.println(Ansi.colorize("2) Top 5 books", Ansi.CYAN));
            System.out.println(Ansi.colorize("0) Back", Ansi.CYAN));
            int ch = ConsoleIO.readInt("Choose: ");
            switch (ch) {
                case 1 -> inventoryReport();
                case 2 -> topBooksReport();
                case 0 -> back = true;
                default -> System.out.println(Ansi.colorize("Invalid option", Ansi.RED));
            }
        }
    }

    private void inventoryReport() {
        LibraryService.InventorySummary s = service.inventorySummary();
        System.out.println(Ansi.colorize("Titles: " + s.totalTitles + ", total copies: " + s.totalCopies + ", available: " + s.availableCopies, Ansi.GREEN));
    }

    private void topBooksReport() {
        List<Map.Entry<Book, Long>> list = service.topLoanedBooks(5);
        if (list.isEmpty()) {
            System.out.println(Ansi.colorize("No loans yet", Ansi.YELLOW));
            return;
        }
        List<String[]> rows = new ArrayList<>();
        for (Map.Entry<Book, Long> e : list) {
            rows.add(new String[]{e.getKey().getTitle(), e.getKey().getAuthor(), e.getValue().toString()});
        }
        TablePrinter.printTable(new String[]{"Title", "Author", "Loans"}, rows);
    }
}
