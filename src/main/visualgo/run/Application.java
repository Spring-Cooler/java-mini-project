package src.main.visualgo.run;

import src.main.visualgo.service.UserService;

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

        while (true) {
            System.out.println("=== Welcome to Visualgo-JAVA ===");
            System.out.println("saved problems:");
//            todo: print problems in problems.dat
            System.out.println("1: add new algorithm problem");
            System.out.println("2: modify problem infos");
            System.out.println("3: run algorithm code");
            System.out.println("4: delete algorithm problem");
            System.out.println("-1: exit");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case -1:
                    return;
                default:
                    break;
            }
        }
    }
}
