/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package poepart1;

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
        
        System.out.println("==Registration and Login==");
        
        System.out.print("Hi, enter your first name: ");
        String firstName = scanner.nextLine();
        
        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine();
        
        Login userLogin = new Login(firstName, lastName);
        
        boolean registrationSuccessful = false;
        
        while (!registrationSuccessful) {
            System.out.println("\n==Registration==");
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
        
        System.out.println("\n===LOGIN===");
        System.out.print("Enter username: ");
        String loginUsername = scanner.nextLine();
        
        System.out.print("Enter password: ");
        String loginPassword = scanner.nextLine();
        
        String loginResult = userLogin.returnLoginStatus(loginUsername, loginPassword);
        System.out.println("\n***************:");
        System.out.println(loginResult);
        
        if (loginResult.contains("Welcome")) {
            runMessagingApp(scanner, messages);
        }
        
        scanner.close();
    }
      private static void runMessagingApp(Scanner scanner, ArrayList<Message> messages) {
    JOptionPane.showMessageDialog(null, "Welcome to QuickChat.");
    
    String maxMessagesInput = JOptionPane.showInputDialog("How many messages do you wish to send?");
    int maxMessages = Integer.parseInt(maxMessagesInput);
    
    int messagesSent = 0;
    boolean running = true;
    
    while (running && messagesSent < maxMessages) {
        String[] options = {
            "Send Messages", 
            "Show recently sent messages", 
            "Message Helper Features",  
            "Quit"
        };
        
        int choice = JOptionPane.showOptionDialog(null,
            "=== QUICKCHAT MENU ===\nMessages sent: " + messagesSent + "/" + maxMessages,
            "QuickChat",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            options,
            options[0]);
        
        switch (choice) {
            case 0: // Send Messages
                if (messagesSent < maxMessages) {
                    sendMessageWithOptions(messages);
                    messagesSent++;
                } else {
                    JOptionPane.showMessageDialog(null, "You have reached your message limit.");
                }
                break;
                
            case 1: // Show recently sent messages
                showRecentlySentMessages(messages);
                break;
                
            case 2: // Message Helper Features
                MessageHelper messageHelper = new MessageHelper(messages);
                messageHelper.showMessageHelperMenu();
                break;
                
            case 3: // Quit
            case -1: // Close button
                running = false;
                break;
                
            default:
                JOptionPane.showMessageDialog(null, "Invalid option. Please try again.");
        }
    }
    
   
}

private static void showRecentlySentMessages(ArrayList<Message> messages) {
    if (messages.isEmpty()) {
        JOptionPane.showMessageDialog(null, "No messages sent yet.");
        return;
    }
    
    StringBuilder recentMessages = new StringBuilder();
    recentMessages.append("=== RECENTLY SENT MESSAGES ===\n\n");
    
    int count = Math.min(messages.size(), 5); // Show last 5 messages
    for (int i = messages.size() - 1; i >= Math.max(0, messages.size() - count); i--) {
        Message msg = messages.get(i);
        recentMessages.append("To: ").append(msg.getRecipient()).append("\n");
        recentMessages.append("Message: ").append(msg.getMessage()).append("\n");
        recentMessages.append("Time: ").append(msg.getTimestamp()).append("\n");
        recentMessages.append("------------------------\n");
    }
    
    recentMessages.append("Total messages: ").append(messages.size());
    
    JOptionPane.showMessageDialog(null, recentMessages.toString());
    }
    
    private static void sendMessageWithOptions(ArrayList<Message> messages) {
        String recipient = JOptionPane.showInputDialog("Enter recipient's cell number (with international code +27):");
        if (recipient == null) return; // User cancelled
        
        String messageText = JOptionPane.showInputDialog("Enter your message (max 250 characters):");
        if (messageText == null) return; // User cancelled
        
        Message message = new Message(recipient, messageText);
        String validationResult = message.sentMessage();
        
        if (!validationResult.equals("Message sent")) {
            JOptionPane.showMessageDialog(null, validationResult);
            return;
        }
        
        
        String[] messageOptions = {"Send Message", "Store Message", "Disregard Message"};
        int option = JOptionPane.showOptionDialog(null,
            "Message created successfully!\n\n" + message.printMessage() + 
            "\n\nWhat would you like to do with this message?",
            "Message Options",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            messageOptions,
            messageOptions[0]);
        
        switch (option) {
            case 0: // Send Message
                messages.add(message);
                JOptionPane.showMessageDialog(null, 
                    "Message sent successfully!\n\n" + message.printMessage(),
                    "Message Sent",
                    JOptionPane.INFORMATION_MESSAGE);
                break;
                
            case 1: // Store Message
                String storeResult = message.storeMessage();
                JOptionPane.showMessageDialog(null, 
                    storeResult + "\n\n" + message.printMessage(),
                    "Message Stored",
                    JOptionPane.INFORMATION_MESSAGE);
                break;
                
            case 2: // Disregard Message
                JOptionPane.showMessageDialog(null, 
                    "Message disregarded.",
                    "Message Disregarded",
                    JOptionPane.INFORMATION_MESSAGE);
                break;
                
            case -1: // Closed dialog
                JOptionPane.showMessageDialog(null, "Message cancelled.");
                break;
        }
    }
}