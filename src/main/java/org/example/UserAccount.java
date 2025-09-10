package org.example;

import exceptions.AccountLockedException;
import exceptions.PasswordIncorrectException;

import java.util.Objects;

public class UserAccount {
    private String userName;
    private String encryptedPassword;
    private int failureCount = 0;
    private boolean locked = false;
    public static final int MAX_FAILURES = 2;

    public UserAccount(String userName, String encryptedPassword) {
        this.userName = userName;
        this.encryptedPassword = encryptedPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public int getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(int failureCount) {
        this.failureCount = failureCount;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void incrementFailure() {
        failureCount++;// starts from 0
    }

    public void resetFailure() {
        failureCount = 0;
    }

    public boolean checkLocked() {
        return locked;
    }


    public void checkPassword(String password) throws AccountLockedException, PasswordIncorrectException {
        if (locked) {
            throw new AccountLockedException("User " + userName + " is locked");
        }
        String enc = Utilities.encryptPassword(password); // B1: ma hoa mat khau dang nhap vao
        // accounts
        if (!enc.equals((encryptedPassword))) {
            incrementFailure();
            if (failureCount >= MAX_FAILURES) {
                setLocked(true); // setter
//                lock = true;
            }
            throw new PasswordIncorrectException("Incorrect Password");
        }
        resetFailure();
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserAccount that = (UserAccount) o;
        return Objects.equals(userName, that.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userName);
    }

    @Override
    public String toString() {
        return userName;
    }
}