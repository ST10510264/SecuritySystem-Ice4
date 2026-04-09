
package com.mycompany.securitysystem;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.time.LocalDateTime;


public class SecuritySystem {

static ArrayList<User> users = new ArrayList<>();
static Scanner input = new Scanner(System.in);

public static void main(String[] args) {

    // Preload users
    users.add(new User("admin", "1234", 1, LocalDateTime.now().toString()));
    users.add(new User("staff", "1111", 2, LocalDateTime.now().toString()));
    users.add(new User("visitor", "0000", 3, LocalDateTime.now().toString()));

    User currentUser = null;
    int attempts = 0;

    while (attempts < 3 && currentUser == null) {
        currentUser = login();
        attempts++;
    }

    if (currentUser == null) {
        System.out.println("Too many attempts. Exiting...");
        return;
    }

    switch (currentUser.role) {
        case 1:
            adminMenu();
            break;
        case 2:
            System.out.println("Welcome Staff");
            break;
        case 3:
            System.out.println("Welcome Visitor");
            break;
    }
}
public static User login() {

    System.out.print("Username: ");
    String username = input.nextLine();

    System.out.print("PIN: ");
    String pin = input.nextLine();

    for (User user : users) {

        if (user.username.equals(username)) {

            // Prevent admin lockout
            if (user.role != 1 && user.isLocked) {
                System.out.println("User is locked.");
                return null;
            }

            if (user.pin.equals(pin)) {
                user.failedAttempts = 0;
                System.out.println("Login successful!");
                return user;
            } else {
                user.failedAttempts++;

                if (user.failedAttempts >= 3 && user.role != 1) {
                    user.isLocked = true;
                    System.out.println("System Locked!");

                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        public void run() {
                            user.isLocked = false;
                            user.failedAttempts = 0;
                            System.out.println("\nUser unlocked!");
                        }
                    }, 300000); // 5 minutes
                }

                System.out.println("Incorrect PIN");
                return null;
            }
        }
    }

    System.out.println("User not found.");
    return null;
}

public static void adminMenu() {

    int choice;

    do {
        System.out.println("\n--- ADMIN MENU ---");
        System.out.println("1. Create User");
        System.out.println("2. View Users");
        System.out.println("3. Exit");

        choice = input.nextInt();
        input.nextLine();

        switch (choice) {
            case 1:
                createUser();
                break;
            case 2:
                viewUsers();
                break;
        }

    } while (choice != 3);
}
public static void createUser() {

    System.out.print("New Username: ");
    String username = input.nextLine();

    // Prevent duplicates
    for (User u : users) {
        if (u.username.equals(username)) {
            System.out.println("Username already exists!");
            return;
        }
    }

    System.out.print("PIN: ");
    String pin = input.nextLine();

    System.out.print("Role (1=Admin, 2=Staff, 3=Visitor): ");
    int role = input.nextInt();
    input.nextLine();

    String time = LocalDateTime.now().toString();

    users.add(new User(username, pin, role, time));

    System.out.println("User created!");
}
public static void viewUsers() {
    for (User u : users) {
        System.out.println(
            "Username: " + u.username +
            " | Role: " + u.role +
            " | Created: " + u.timestamp
        );
    }
}
    
}
