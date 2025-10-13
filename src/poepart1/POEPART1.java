/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package poepart1;

import java.util.Scanner;
import java.util.Scanner;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author RC_Student_lab
 */

public class POEPART1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Message> messages = new ArrayList<>();
        
        System.out.println("== Registration and Login ==");
        
        System.out.print("Hi, enter your first name: ");
        String firstName = scanner.nextLine();
        
        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine();
        
        Login userLogin = new Login(firstName, lastName);
        
        boolean registrationSuccessful = false;
        
        while (!registrationSuccessful) {
            System.out.println("\n== REGISTRATION ==");
            System.out.print("Enter username (must contain '_' and 5 characters max): ");
            String username = scanner.nextLine();
            
            System.out.print("Enter password (more than 8 characters, capital, number, special character): ");
            String password = scanner.nextLine();
            
            System.out.print("Enter South African cell phone (with international code +27): ");
            String cellPhone = scanner.nextLine();
            
            String registrationResult = userLogin.registerUser(username, password, cellPhone);
            System.out.println("\nRegistration Result:");
            System.out.println(registrationResult);
            
            if (userLogin.isRegistered()) {
                registrationSuccessful = true;
            } else {
                System.out.println("\nRegistration failed. Please try again with correct formatting.");
            }
        }
        
        System.out.println("\n=== LOGIN ===");
        System.out.print("Enter username: ");
        String loginUsername = scanner.nextLine();
        
        System.out.print("Enter password: ");
        String loginPassword = scanner.nextLine();
        
        String loginResult = userLogin.returnLoginStatus(loginUsername, loginPassword);
        System.out.println("\nResult:");
        System.out.println(loginResult);
        
        if (loginResult.contains("Welcome")) {
            runMessagingApp(scanner, messages);
        }
        
        scanner.close();
    }
    
    private static void runMessagingApp(Scanner scanner, ArrayList<Message> messages) {
        System.out.println("\nWelcome to QuickChat.");
        
        System.out.print("How many messages do you wish to send? ");
        int maxMessages = scanner.nextInt();
        scanner.nextLine();
        
        int messagesSent = 0;
        
        while (messagesSent < maxMessages) {
            System.out.println("\n=== MENU ===");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    if (messagesSent < maxMessages) {
                        sendMessage(scanner, messages);
                        messagesSent++;
                    } else {
                        System.out.println("You have reached your message limit.");
                    }
                    break;
                    
                case 2:
                    System.out.println("Coming Soon.");
                    break;
                    
                case 3:
                    System.out.println("Total messages sent: " + Message.returnTotalMessages());
                    System.out.println("Goodbye!");
                    return;
                    
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        
        System.out.println("You have sent all " + maxMessages + " messages.");
        System.out.println("Total messages accumulated: " + Message.returnTotalMessages());
    }
    
    private static void sendMessage(Scanner scanner, ArrayList<Message> messages) {
        System.out.print("Enter recipient's cell number (with international code +27): ");
        String recipient = scanner.nextLine();
        
        System.out.print("Enter your message (max 250 characters): ");
        String messageText = scanner.nextLine();
        
        Message message = new Message(recipient, messageText);
        String sendResult = message.sentMessage();
        
        if (sendResult.equals("Message sent")) {
            messages.add(message);
            System.out.println("Message sent successfully!");
            
            JOptionPane.showMessageDialog(null, 
                message.printMessage(),
                "Message Details",
                JOptionPane.INFORMATION_MESSAGE);
                
            System.out.println("\nMessage Details:");
            System.out.println(message.printMessage());
        } else {
            System.out.println(sendResult);
        }
    }
}