/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package poepart1;

import java.util.Scanner;

/**
 *
 * @author RC_Student_lab
 */
public class POEPART1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("==Registration and Login==");
        
        // Get user details
        System.out.print("Hi, enter your first name: ");
        String firstName = scanner.nextLine();
        
        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine();
        
        // Create Login object
        Login userLogin = new Login(firstName, lastName);
        
        // Registration process with retry functionality
        boolean registrationSuccessful = false;
        
        while (!registrationSuccessful) {
            System.out.println("\n==REGISTRATION==");
            System.out.print("Enter username (must contain '_' and 5 characters max): ");
            String username = scanner.nextLine();
            
            System.out.print("Enter password (more than 8 characters, capital, number, special character): ");
            String password = scanner.nextLine();
            
            System.out.print("Enter South African cell phone (with international code +27): ");
            String cellPhone = scanner.nextLine();
            
            // Attempt registration
            String registrationResult = userLogin.registerUser(username, password, cellPhone);
            System.out.println("\nRegistration Result:");
            System.out.println(registrationResult);
            
            // Check if registration was successful
            if (userLogin.isRegistered()) {
                registrationSuccessful = true;
            } else {
                System.out.println("\nRegistration failed. Please try again with correct formatting.");
            }
        }
        
        // Login process
        System.out.println("\n==LOGIN==");
        System.out.print("Enter username: ");
        String loginUsername = scanner.nextLine();
        
        System.out.print("Enter password: ");
        String loginPassword = scanner.nextLine();
        
        // Attempt login
        String loginResult = userLogin.returnLoginStatus(loginUsername, loginPassword);
        System.out.println("\nResult:");
        System.out.println(loginResult);
        
        scanner.close();
    }
}