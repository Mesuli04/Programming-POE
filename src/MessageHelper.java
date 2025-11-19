/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ICT 2022
 */
package poepart1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import org.json.JSONObject;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;


public class MessageHelper {
    // Arrays to store different types of messages
    private ArrayList<Message> sentMessages;
    private ArrayList<Message> disregardedMessages;
    private ArrayList<Message> storedMessages;
    private ArrayList<String> messageHash;
    private ArrayList<String> messageID;
    
    // Constructor
    public MessageHelper() {
        this.sentMessages = new ArrayList<>();
        this.disregardedMessages = new ArrayList<>();
        this.storedMessages = new ArrayList<>();
        this.messageHash = new ArrayList<>();
        this.messageID = new ArrayList<>();
        
        // Populate arrays
        populateTestData();
        loadStoredMessages();
    }
    
   
    private void populateTestData() {
        // Test Data Message 1
        Message msg1 = new Message("+27834557896", "Did you get the cake?");
        msg1.setFlag("Sent");
        addToSentMessages(msg1);
        
        // Test Data Message 2
        Message msg2 = new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.");
        msg2.setFlag("Stored");
        addToStoredMessages(msg2);
        
        // Test Data Message 3
        Message msg3 = new Message("+27834484567", "Yohoooo, I am at your gate.");
        // No flag specified, default to Sent
        msg3.setFlag("Sent");
        addToSentMessages(msg3);
        
        // Test Data Message 4
        Message msg4 = new Message("0838884567", "It is dinner time !");
        msg4.setFlag("Sent");
        addToSentMessages(msg4);
        
        // Test Data Message 5
        Message msg5 = new Message("+27838884567", "Ok, I am leaving without you.");
        msg5.setFlag("Stored");
        addToStoredMessages(msg5);
    }
    
    /**
     * Loads stored messages from JSON files
     */
    private void loadStoredMessages() {
        try {
            File messagesDir = new File("messages");
            if (messagesDir.exists() && messagesDir.isDirectory()) {
                File[] files = messagesDir.listFiles((dir, name) -> name.endsWith(".json"));
                
                if (files != null) {
                    for (File file : files) {
                        try {
                            String content = new String(Files.readAllBytes(Paths.get(file.getPath())));
                            JSONObject json = new JSONObject(content);
                            
                            Message storedMsg = new Message(
                                json.getString("recipient"),
                                json.getString("message")
                            );
                            storedMsg.setMessageID(json.getString("messageID"));
                            storedMsg.setMessageHash(json.getString("messageHash"));
                            storedMsg.setFlag("Stored");
                            
                            addToStoredMessages(storedMsg);
                        } catch (Exception e) {
                            System.out.println("Error reading stored message: " + e.getMessage());
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading stored messages: " + e.getMessage());
        }
    }
    
    // Methods to add messages to respective arrays
    private void addToSentMessages(Message message) {
        sentMessages.add(message);
        messageHash.add(message.getMessageHash());
        messageID.add(message.getMessageID());
    }
    
    private void addToStoredMessages(Message message) {
        storedMessages.add(message);
        messageHash.add(message.getMessageHash());
        messageID.add(message.getMessageID());
    }
    
    private void addToDisregardedMessages(Message message) {
        disregardedMessages.add(message);
        messageHash.add(message.getMessageHash());
        messageID.add(message.getMessageID());
    }
    
    /**
     * 2a. Display the sender and recipient of all sent messages
     */
    public void displaySentMessagesSendersRecipients() {
        StringBuilder result = new StringBuilder();
        result.append("=== SENT MESSAGES - SENDERS AND RECIPIENTS ===\n\n");
        
        for (int i = 0; i < sentMessages.size(); i++) {
            Message msg = sentMessages.get(i);
            result.append("Message ").append(i + 1).append(":\n");
            result.append("Recipient: ").append(msg.getRecipient()).append("\n");
            result.append("------------------------\n");
        }
        
        JOptionPane.showMessageDialog(null, result.toString(), 
            "Sent Messages - Senders and Recipients", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * 2b. Display the longest sent message
     */
    public void displayLongestSentMessage() {
        if (sentMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No sent messages available.", 
                "Longest Sent Message", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        Message longestMessage = sentMessages.get(0);
        
        for (Message msg : sentMessages) {
            if (msg.getMessage().length() > longestMessage.getMessage().length()) {
                longestMessage = msg;
            }
        }
        
        String result = "=== LONGEST SENT MESSAGE ===\n\n" +
                       "Message: " + longestMessage.getMessage() + "\n" +
                       "Length: " + longestMessage.getMessage().length() + " characters\n" +
                       "Recipient: " + longestMessage.getRecipient() + "\n" +
                       "Message ID: " + longestMessage.getMessageID();
        
        JOptionPane.showMessageDialog(null, result, 
            "Longest Sent Message", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * 2c. Search for a message ID and display the corresponding recipient and message
     */
    public void searchMessageByID() {
        String searchID = JOptionPane.showInputDialog("Enter Message ID to search:");
        
        if (searchID == null || searchID.trim().isEmpty()) {
            return;
        }
        
        // Search in all message arrays
        Message foundMessage = null;
        
        for (Message msg : sentMessages) {
            if (msg.getMessageID().equals(searchID)) {
                foundMessage = msg;
                break;
            }
        }
        
        if (foundMessage == null) {
            for (Message msg : storedMessages) {
                if (msg.getMessageID().equals(searchID)) {
                    foundMessage = msg;
                    break;
                }
            }
        }
        
        if (foundMessage == null) {
            for (Message msg : disregardedMessages) {
                if (msg.getMessageID().equals(searchID)) {
                    foundMessage = msg;
                    break;
                }
            }
        }
        
        if (foundMessage != null) {
            String result = "=== MESSAGE FOUND ===\n\n" +
                           "Message ID: " + foundMessage.getMessageID() + "\n" +
                           "Recipient: " + foundMessage.getRecipient() + "\n" +
                           "Message: " + foundMessage.getMessage() + "\n" +
                           "Hash: " + foundMessage.getMessageHash();
            
            JOptionPane.showMessageDialog(null, result, 
                "Message Search Result", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, 
                "No message found with ID: " + searchID, 
                "Message Not Found", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * 2d. Search for all messages sent to a particular recipient
     */
    public void searchMessagesByRecipient() {
        String recipient = JOptionPane.showInputDialog("Enter recipient's number to search:");
        
        if (recipient == null || recipient.trim().isEmpty()) {
            return;
        }
        
        ArrayList<Message> matchingMessages = new ArrayList<>();
        
        // Search in sent messages
        for (Message msg : sentMessages) {
            if (msg.getRecipient().equals(recipient)) {
                matchingMessages.add(msg);
            }
        }
        
        // Search in stored messages
        for (Message msg : storedMessages) {
            if (msg.getRecipient().equals(recipient)) {
                matchingMessages.add(msg);
            }
        }
        
        // Search in disregarded messages
        for (Message msg : disregardedMessages) {
            if (msg.getRecipient().equals(recipient)) {
                matchingMessages.add(msg);
            }
        }
        
        if (!matchingMessages.isEmpty()) {
            StringBuilder result = new StringBuilder();
            result.append("=== MESSAGES FOR RECIPIENT: ").append(recipient).append(" ===\n\n");
            
            for (int i = 0; i < matchingMessages.size(); i++) {
                Message msg = matchingMessages.get(i);
                result.append("Message ").append(i + 1).append(":\n");
                result.append("ID: ").append(msg.getMessageID()).append("\n");
                result.append("Message: ").append(msg.getMessage()).append("\n");
                result.append("Hash: ").append(msg.getMessageHash()).append("\n");
                result.append("------------------------\n");
            }
            
            result.append("Total messages: ").append(matchingMessages.size());
            
            JOptionPane.showMessageDialog(null, result.toString(), 
                "Messages by Recipient", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, 
                "No messages found for recipient: " + recipient, 
                "No Messages Found", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * 2e. Delete a message using the message hash
     */
    public void deleteMessageByHash() {
        String hashToDelete = JOptionPane.showInputDialog("Enter Message Hash to delete:");
        
        if (hashToDelete == null || hashToDelete.trim().isEmpty()) {
            return;
        }
        
        boolean deleted = false;
        
        // Remove from sent messages
        for (int i = sentMessages.size() - 1; i >= 0; i--) {
            if (sentMessages.get(i).getMessageHash().equals(hashToDelete)) {
                sentMessages.remove(i);
                deleted = true;
                break;
            }
        }
        
        // Remove from stored messages
        if (!deleted) {
            for (int i = storedMessages.size() - 1; i >= 0; i--) {
                if (storedMessages.get(i).getMessageHash().equals(hashToDelete)) {
                    storedMessages.remove(i);
                    deleted = true;
                    break;
                }
            }
        }
        
        // Remove from disregarded messages
        if (!deleted) {
            for (int i = disregardedMessages.size() - 1; i >= 0; i--) {
                if (disregardedMessages.get(i).getMessageHash().equals(hashToDelete)) {
                    disregardedMessages.remove(i);
                    deleted = true;
                    break;
                }
            }
        }
        
        // Remove from hash array
        messageHash.removeIf(hash -> hash.equals(hashToDelete));
        
        if (deleted) {
            JOptionPane.showMessageDialog(null, 
                "Message with hash '" + hashToDelete + "' has been deleted successfully.", 
                "Message Deleted", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, 
                "No message found with hash: " + hashToDelete, 
                "Message Not Found", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * 2f. Display a report that lists the full details of all sent messages
     */
    public void displaySentMessagesReport() {
        if (sentMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No sent messages available for report.", 
                "Sent Messages Report", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        StringBuilder report = new StringBuilder();
        report.append("=== SENT MESSAGES FULL REPORT ===\n\n");
        
        for (int i = 0; i < sentMessages.size(); i++) {
            Message msg = sentMessages.get(i);
            report.append("MESSAGE ").append(i + 1).append(":\n");
            report.append("Message ID: ").append(msg.getMessageID()).append("\n");
            report.append("Message Hash: ").append(msg.getMessageHash()).append("\n");
            report.append("Recipient: ").append(msg.getRecipient()).append("\n");
            report.append("Message: ").append(msg.getMessage()).append("\n");
            report.append("Timestamp: ").append(msg.getTimestamp()).append("\n");
            report.append("Message Number: ").append(msg.getMessageNumber()).append("\n");
            report.append("====================================\n\n");
        }
        
        report.append("TOTAL SENT MESSAGES: ").append(sentMessages.size());
        
        JOptionPane.showMessageDialog(null, report.toString(), 
            "Sent Messages Full Report", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Main method to display all MessageHelper functionalities
     */
    public void showMessageHelperMenu() {
        String[] options = {
            "Display Senders/Recipients of Sent Messages",
            "Display Longest Sent Message", 
            "Search Message by ID",
            "Search Messages by Recipient",
            "Delete Message by Hash",
            "Display Full Sent Messages Report",
            "Exit"
        };
        
        boolean running = true;
        
        while (running) {
            int choice = JOptionPane.showOptionDialog(null,
                "=== MESSAGE HELPER MENU ===\n\n" +
                "Select an option to perform message analysis and management:",
                "Message Helper",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);
            
            switch (choice) {
                case 0:
                    displaySentMessagesSendersRecipients();
                    break;
                case 1:
                    displayLongestSentMessage();
                    break;
                case 2:
                    searchMessageByID();
                    break;
                case 3:
                    searchMessagesByRecipient();
                    break;
                case 4:
                    deleteMessageByHash();
                    break;
                case 5:
                    displaySentMessagesReport();
                    break;
                case 6:
                case -1:
                    running = false;
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Invalid option selected.");
            }
        }
    }
    
    // Getters for the arrays
    public ArrayList<Message> getSentMessages() { return sentMessages; }
    public ArrayList<Message> getDisregardedMessages() { return disregardedMessages; }
    public ArrayList<Message> getStoredMessages() { return storedMessages; }
    public ArrayList<String> getMessageHash() { return messageHash; }
    public ArrayList<String> getMessageID() { return messageID; }
}