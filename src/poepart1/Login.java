/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package poepart1;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 *
 * @author RC_Student_Lab
 */
public class Login {
    private String username;
    private String password;
    private String cellPhoneNumber;
    private String firstName;
    private String lastName;
    private boolean isRegistered;

    // Constructor
    public Login(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.isRegistered = false;
    }

    public boolean checkUserName() {
        if (username == null) return false;
        return username.contains("_") && username.length() <= 5;
    }

    public boolean checkPasswordComplexity() {
        if (password == null || password.length() < 8) return false;
        
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecialChar = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasCapital = true;
            if (Character.isDigit(c)) hasNumber = true;
            if (!Character.isLetterOrDigit(c) && !Character.isWhitespace(c)) hasSpecialChar = true;
        }
        
        return hasCapital && hasNumber && hasSpecialChar;
    }

    // CELL PHONE NUMBER CHECKING
    public boolean checkCellPhoneNumber() {
        if (cellPhoneNumber == null) return false;
        
        // Regular expression for South African cell phone numbers with international code
        // Pattern: +27 followed by 9 digits (total 12 characters)
        // Or 27 followed by 9 digits (total 11 characters)
        // AI Tool Reference: ChatGPT used for regex pattern refinement
        String regex = "^(\\+27|27)[0-9]{9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cellPhoneNumber);
        
        return matcher.matches();
    }

    public String registerUser(String username, String password, String cellPhoneNumber) {
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
        
        StringBuilder result = new StringBuilder();
        boolean allValid = true;
        
        // Check username
        if (!checkUserName()) {
            result.append("Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.\n");
            allValid = false;
        } else {
            result.append("Username successfully captured.\n");
        }
        
        // Check password
        if (!checkPasswordComplexity()) {
            result.append("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.\n");
            allValid = false;
        } else {
            result.append("Password successfully captured.\n");
        }
        
        // Check cell phone
        if (!checkCellPhoneNumber()) {
            result.append("Cell phone number incorrectly formatted or does not contain international code.\n");
            allValid = false;
        } else {
            result.append("Cell phone number successfully added.\n");
        }
        
        // Set registration status
        if (allValid) {
            this.isRegistered = true;
            result.append("User registered successfully!");
        } else {
            this.isRegistered = false;
            result.append("Registration failed. Please correct the errors above.");
        }
        
        return result.toString();
    }

    public boolean loginUser(String enteredUsername, String enteredPassword) {
        if (!isRegistered) return false;
        return this.username.equals(enteredUsername) && this.password.equals(enteredPassword);
    }

    public String returnLoginStatus(String enteredUsername, String enteredPassword) {
        if (loginUser(enteredUsername, enteredPassword)) {
            return "Welcome " + firstName + " " + lastName + ", it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }

    // Getters
    public String getUsername() { return username; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public boolean isRegistered() { return isRegistered; }
}