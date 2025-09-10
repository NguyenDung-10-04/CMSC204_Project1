package org.example;
/**
 * ----------------------------------------------------------------------
 * File: Utilities.java
 * Author: Prof. Sandro Fouche
 * Email: sandro.fouche@montgomerycollege.edu
 * Course: CMSC204 - Computer Science II
 * Project: UserAccessManager
 * Institution: Montgomery College
 * Year: 2025
 * @version 1.0 (Fall 2025)
 *
 * Description:
 *     Utility class providing helper methods for password encryption and
 *     file-based loading of user accounts into the UserAccessManager system.
 *
 * Notes:
 *     This class is provided as part of the CMSC204 Project 1 starter code.
 *     Students may use this file as-is and should not modify it unless directed.
 *
 * License:
 *     This file is provided for educational use only in CMSC204 at Montgomery College.
 *     Redistribution outside this course without permission is prohibited.
 * ----------------------------------------------------------------------
 * @exclude-from-submission: instructor-provided starter file
 */

import exceptions.DuplicateUserException;

/**
 * A utility class that provides helper methods for the User Access Manager system.
 * Includes methods for encrypting passwords and reading account data from a file.
 */
public final class Utilities {

    // Prevent instantiation
    private Utilities() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Encrypts a password using the SHA-256 hash algorithm.
     * <p>
     * The returned value is a lowercase hexadecimal representation of the SHA-256 digest.
     * This method is used to ensure passwords are stored and compared securely.
     *
     * @param password the plaintext password to encrypt
     * @return the SHA-256 hash of the password in hex string form
     */
    public static String encryptPassword(String password) {
        try {
            // Get a SHA-256 MessageDigest instance
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            // Apply the digest to the password's bytes
            byte[] hash = md.digest(password.getBytes(java.nio.charset.StandardCharsets.UTF_8));

            // Convert the resulting byte array into a hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0'); // pad with leading zero if needed
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            // SHA-256 should always be available in modern JVMs; rethrow as runtime error if not
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    /**
     * Reads account data from a file and adds each account to the provided UserAccessManager.
     * <p>
     * Each line of the file should contain a username and an encrypted password separated by whitespace.
     * Blank lines are skipped. If a user already exists in the system, a warning is printed.
     *
     * @param filename the name of the file to read
     * @param manager  the UserAccessManager instance to add accounts to
     */
    public static void readAccountFile(String filename, UserAccessManager manager) {
        try (java.util.Scanner fileScanner = new java.util.Scanner(new java.io.File(filename))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();

                // Skip blank lines
                if (line.isEmpty()) {
                    continue;
                }

                // Split line into username and encrypted password
                String[] parts = line.split("\\s+");
                if (parts.length >= 2) {
                    String username = parts[0];
                    String encryptedPassword = parts[1];

                    try {
                        manager.addUser(username, encryptedPassword);
                    } catch (DuplicateUserException e) {
                        // Print warning if duplicate user is encountered, but continue processing
                        System.out.println("Warning: User '" + username + "' already exists. Skipping.");
                    } catch (Exception e) {
                        System.out.println("Invalid Command: " + e.getMessage());
                    }
                }
            }
        } catch (java.io.FileNotFoundException e) {
            // Inform user if file cannot be found
            System.out.println("Unable to load file: " + filename);
        }
    }
}