package org.example;

import exceptions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserAccessManager {

    private List<UserAccount> accounts;

    public UserAccessManager() {
        this.accounts = new ArrayList<>();
    }

    //  loads user account information from the specified filename
    public void loadAccounts(String filename) throws FileNotFoundException {
        // Check fileName input - fileName txt in computer
        Utilities.readAccountFile(filename, this);
    }

    public void addUser(String userName, String encryptedPassword) throws DuplicateUserException, InvalidCommandException {
        if (userName == null || encryptedPassword == null || userName.trim().isEmpty() || encryptedPassword.trim().isEmpty()) {
            throw new InvalidCommandException("Invalid Command");
        }
        for (UserAccount acc : accounts) {
            if (acc.getUserName().equals(userName)) {
                throw new DuplicateUserException("User " + userName + " account already exists");
            }
        }
        accounts.add(new UserAccount(userName, encryptedPassword));
    }

    public void removeUser(String userName) throws UserNotFoundException, InvalidCommandException {
        if (userName == null || userName.trim().isEmpty()) {
            throw new InvalidCommandException("Invalid Command");
        }

        // Flag to check if user was removed
        boolean userFound = false;

        // Loop through accounts to find and remove user
        for (UserAccount acc : accounts) {
            if (acc.getUserName().equals(userName)) {
                accounts.remove(acc);
                userFound = true;
                break; // Exit loop once the user is found and removed
            }
        }

        // If user was not found, throw exception
        if (!userFound) {
            throw new UserNotFoundException("User " + userName + " not found.");
        }
    }

    public boolean verifyAccess(String userName, String encryptedPassword) throws UserNotFoundException, AccountLockedException, InvalidCommandException, PasswordIncorrectException {
        if (userName == null || userName.trim().isEmpty()) {
            throw new InvalidCommandException("Invalid Command");
        }
        for (UserAccount acc : accounts) {
            if (acc.getUserName().equals(userName)) {
                acc.checkPassword(encryptedPassword); // Pass encrypted password directly
                return true; // Indicate successful verification
            }
        }
        throw new UserNotFoundException("User " + userName + " not found");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserAccessManager userAccessManager = new UserAccessManager();

        System.out.println("$ java UserAccessManager\n");
        System.out.println("User access manager ready.\n");

        String choice = "";
        do {
            System.out.print("User Access Manager> ");
            choice = scanner.next();

            switch (choice) {
                case "load": {
                    String fileName = scanner.next();
                    try {
                        userAccessManager.loadAccounts(fileName);
                    } catch (FileNotFoundException e) {
                        System.out.println("Unable to load file: " + fileName);
                    }
                    break;
                }

                case "add": {
                    String addName = scanner.next();
                    System.out.print("Password: ");
                    String password = scanner.next();
                    String enc = Utilities.encryptPassword(password);
                    try {
                        userAccessManager.addUser(addName, enc);
                    } catch (InvalidCommandException | DuplicateUserException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }

                case "remove": {
                    String removeName = scanner.next();
                    try {
                        userAccessManager.removeUser(removeName);
                    } catch (UserNotFoundException | InvalidCommandException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }

                case "verify": {
                    String verifyName = scanner.next();
                    System.out.print("Password: ");
                    String password = scanner.next();
                    String enc = Utilities.encryptPassword(password);
                    try {
                        boolean verified = userAccessManager.verifyAccess(verifyName, enc);
                        if (verified) {
                            System.out.println("Access verified");
                        }
                    } catch (UserNotFoundException | AccountLockedException |
                             InvalidCommandException | PasswordIncorrectException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }

                case "exit":
                    break;

                default:
                    System.out.println("Invalid Command");
            }
        } while (!choice.equals("exit"));
    }
}