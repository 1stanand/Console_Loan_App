package com.librarydesk.app;

import java.util.List;

public class TablePrinter {
    public static void printTable(String[] headers, List<String[]> rows) {
        int cols = headers.length;
        int[] widths = new int[cols];
        for (int i = 0; i < cols; i++) {
            widths[i] = headers[i].length();
        }
        for (String[] row : rows) {
            for (int i = 0; i < cols; i++) {
                if (row[i] != null) {
                    widths[i] = Math.min(30, Math.max(widths[i], row[i].length()));
                }
            }
        }
        // header
        for (int i = 0; i < cols; i++) {
            System.out.print(String.format("%-" + widths[i] + "s ", headers[i]));
        }
        System.out.println();
        // rows
        for (String[] row : rows) {
            for (int i = 0; i < cols; i++) {
                String cell = row[i] == null ? "" : row[i];
                if (cell.length() > widths[i]) {
                    cell = cell.substring(0, widths[i]-1) + "";
                }
                System.out.print(String.format("%-" + widths[i] + "s ", cell));
            }
            System.out.println();
        }
    }
}
