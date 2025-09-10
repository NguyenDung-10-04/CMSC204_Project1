/**
 * ----------------------------------------------------------------------
 * File: UserAccessManagerGFATest.java
 * Author: Montgomery College CMSC204 Staff
 * Course: CMSC204 - Computer Science II
 * Project: UserAccessManager
 * Institution: Montgomery College
 * Year: 2025
 *
 * Description:
 *     This test class provides a minimal set of JUnit 5 tests to check for
 *     basic functionality in student implementations of Project 1.
 *     It is used to verify that a Good Faith Attempt (GFA) was made.
 *
 * Notes:
 *     These are not full functional or edge-case tests.
 *     Passing these tests only confirms that core methods compile and run.
 *
 * License:
 *     This file is provided for educational use in CMSC204 at Montgomery College.
 *     Redistribution outside this course is not permitted.
 * ----------------------------------------------------------------------
 */

import org.example.UserAccessManager;
import org.example.Utilities;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Good Faith Attempt (GFA) tests for basic project validation.
 */
public class UserAccessManagerGFATest {

    /**
     * Confirms that addUser can be called without throwing an exception.
     */
    @Test
    public void testAddUserBasic() {
        UserAccessManager uam = new UserAccessManager();
        assertDoesNotThrow(() -> uam.addUser("alice", Utilities.encryptPassword("pass123")));
    }

    /**
     * Confirms that removeUser compiles and accepts a username,
     * even if the user doesn't exist (exception ignored).
     */
    @Test
    public void testRemoveUserCompiles() {
        UserAccessManager uam = new UserAccessManager();
        try {
            uam.removeUser("ghost");
        } catch (Exception ignored) {}
        assertTrue(true); // Passes as long as removeUser is callable
    }

    /**
     * Confirms that verifyAccess can be called and compiles.
     * This test does not validate correctness of password handling.
     */
    @Test
    public void testVerifyAccessCompiles() {
        UserAccessManager uam = new UserAccessManager();
        try {
            uam.addUser("bob", Utilities.encryptPassword("secret"));
            uam.verifyAccess("bob", Utilities.encryptPassword("secret"));
        } catch (Exception ignored) {}
        assertTrue(true); // Always passes — confirms verifyAccess is callable
    }

    /**
     * Confirms that the loadAccounts method can be invoked without crashing.
     * (Assumes the provided file may not exist — that's acceptable here.)
     */
    @Test
    public void testLoadAccountsCompiles() {
        UserAccessManager uam = new UserAccessManager();
        assertDoesNotThrow(() -> Utilities.readAccountFile("fakefile.txt", uam));
    }
}
