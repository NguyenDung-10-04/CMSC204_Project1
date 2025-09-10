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

        for (UserAccount acc : accounts) {
            if (acc.getUserName().equals(userName)) {
                accounts.remove(acc);
                return; // Find userName automatically return back to the beginning
            }
            throw new UserNotFoundException("User " + userName + " not found.");
        }
    }
// find user confirm method check password
    // Nhap userName
    // Nhap password
    public boolean verifyAccess(String userName, String encryptedPassword) throws UserNotFoundException, AccountLockedException, InvalidCommandException, PasswordIncorrectException {
        if (userName == null || userName.trim().isEmpty()) {
            throw new InvalidCommandException("Invalid Command");
        }

        for (UserAccount acc : accounts) {
            if (!acc.getUserName().equals(userName)) {
                throw new UserNotFoundException("User " + userName + " not found");
            }
            acc.checkPassword(encryptedPassword);
        }
        return true;
    }

    /* public static void main(String[] args) throws FileNotFoundException {
         Scanner scanner = new Scanner(System.in);
 //        String password1 = "mypassword123";

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
                     userAccessManager.loadAccounts(fileName);
                     break;
                 }
 // load src/entries/accounts.txt
                 case "add": {
                     String addName = scanner.nextLine();
                     System.out.print("Password: ");
                     String password = scanner.nextLine();
                     String enc = Utilities.encryptPassword(password);
                     try {
                         userAccessManager.addUser(addName, enc);
                     } catch (InvalidCommandException | DuplicateUserException e) {
                         System.err.println(e.getMessage());
                     }
                     break;
                 }

                 case "remove": {
                     String removeName = scanner.next();
                     try {
                         userAccessManager.removeUser(removeName);
                         System.out.println("Remove successfully!");
                     } catch (UserNotFoundException | InvalidCommandException e) {
                         System.err.println(e.getMessage());
                     }
                     break;
                 }
                 case "verify": {
                     String verifyName = scanner.next();
                     System.out.println("Password: ");
                     String password = scanner.next();
                     String enc = Utilities.encryptPassword(password);
                     try {
                         userAccessManager.verifyAccess(verifyName, enc);
                     } catch (UserNotFoundException | AccountLockedException | InvalidCommandException |
                              PasswordIncorrectException e) {
                         System.err.println(e.getMessage());
                     }
                     break;
                 }
                 default:
                     System.err.println("Invalid Command");
             }
         } while (!choice.equals("exit"));

     }
 }*/
    public static void main(String[] args) throws FileNotFoundException {
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
                    String addName = scanner.next(); // FIXED (was nextLine())
                    System.out.print("Password: ");  // FIXED (print instead of println)
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
                    System.out.print("Password: "); // FIXED
                    String password = scanner.next();
                    String enc = Utilities.encryptPassword(password);
                    try {
                        userAccessManager.verifyAccess(verifyName, enc);
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



