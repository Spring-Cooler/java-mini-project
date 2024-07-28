package main.visualgo.aggregate;

import java.io.File;
import java.util.ArrayList;

public record Problem() {
    static ArrayList<Algorithm> algorithms;
    static String url;
    static String notes;
    static ArrayList<File> testCase;
    static ArrayList<File> results;
}
