package main.visualgo.run;

import main.visualgo.service.UserService;

import java.util.Scanner;

public class Application {
    private static UserService userService;
    private static Scanner sc = new Scanner(System.in);
    static {
        try {
            userService = UserService.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        while(true){
            System.out.println("=== Visualgo-JAVA ===");
            System.out.println("Check your Solutions:");
//            todo: print solutions in data folder
            System.out.println("Check your TestCases:");
//            todo: print testCases
            System.out.println("Run? (0: No, 1: Yes)");
//            todo: run every testCases and Solutions

        }
    }
}
