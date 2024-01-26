package org.example.util;

import java.util.Scanner;

public class InputReader {
    private static Scanner scanner = new Scanner(System.in);

    public static String readInput() {
        return scanner.nextLine();
    }
}
