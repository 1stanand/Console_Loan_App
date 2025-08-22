package com.librarydesk.app;

import java.util.Scanner;

public class ConsoleIO {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static String readLine(String prompt) {
        System.out.print(prompt);
        return SCANNER.nextLine();
    }

    public static String readNonEmpty(String prompt) {
        while (true) {
            String s = readLine(prompt).trim();
            if (!s.isEmpty()) return s;
            System.out.println(Ansi.colorize("Input required", Ansi.RED));
        }
    }

    public static int readInt(String prompt) {
        while (true) {
            String s = readLine(prompt).trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println(Ansi.colorize("Invalid number", Ansi.RED));
            }
        }
    }

    public static int chooseFromList(int size, String prompt) {
        while (true) {
            int i = readInt(prompt);
            if (i >= 0 && i < size) return i;
            System.out.println(Ansi.colorize("Choice out of range", Ansi.RED));
        }
    }
}
