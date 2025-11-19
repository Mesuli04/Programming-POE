package poepart1;

import java.util.ArrayList;
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
    
    // Reference to the main messages list from the application
    private ArrayList<Message> userMessages;
    
    /**
     * Constructor that takes the actual user messages from the main application
     */
    public MessageHelper(ArrayList<Message> userMessages) {
        this.userMessages = userMessages;
        this.sentMessages = new ArrayList<>();
        this.disregardedMessages = new ArrayList<>();
        this.storedMessages = new ArrayList<>();
        this.messageHash = new ArrayList<>();
        this.messageID = new ArrayList<>();
        
        // Populate arrays with actual user data
        populateArraysFromUserMessages();
        loadStoredMessagesFromFiles();
    }
    
    /**
     * Populates arrays with actual messages from the user's messaging activity
     */
    private void populateArraysFromUserMessages() {
        // Since we don't have a flag system in the original Message class,
        // we'll consider all messages in userMessages as sent messages
        // and also load stored messages from JSON files
        
        for (Message message : userMessages) {
            addToSentMessages(message);
        }
    }
    
    /**
     * Loads stored messages from JSON files in the messages directory
     */
    private void loadStoredMessagesFromFiles() {
        try {
            File messagesDir = new File("messages");
            if (messagesDir.exists() && messagesDir.isDirectory()) {
                File[] files = messagesDir.listFiles((dir, name) -> name.endsWith(".json"));
                
                if (files != null) {
                    for (File file : files) {
                        try {
                            String content = new String(Files.readAllBytes(Paths.get(file.getPath())));
                            JSONObject json = new JSONObject(content);
                            
                            // Check if this message is already in sent messages
                            boolean alreadyInSent = false;
                            for (Message sentMsg : sentMessages) {
                                if (sentMsg.getMessageID().equals(json.getString("messageID"))) {
                                    alreadyInSent = true;
                                    break;
                                }
                            }
                            
                            // Only add if not already in sent messages
                            if (!alreadyInSent) {
                                Message storedMsg = new Message(
                                    json.getString("recipient"),
                                    json.getString("message")
                                );
                                storedMsg.setMessageID(json.getString("messageID"));
                                storedMsg.setMessageHash(json.getString("messageHash"));
                                storedMsg.setTimestamp(json.getString("timestamp"));
                                
                                addToStoredMessages(storedMsg);
                            }
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
        if (message.getMessageHash() != null) {
            messageHash.add(message.getMessageHash());
        }
        if (message.getMessageID() != null) {
            messageID.add(message.getMessageID());
        }
    }
    
    private void addToStoredMessages(Message message) {
        storedMessages.add(message);
        if (message.getMessageHash() != null) {
            messageHash.add(message.getMessageHash());
        }
        if (message.getMessageID() != null) {
            messageID.add(message.getMessageID());
        }
    }
    
    private void addToDisregardedMessages(Message message) {
        disregardedMessages.add(message);
        if (message.getMessageHash() != null) {
            messageHash.add(message.getMessageHash());
        }
        if (message.getMessageID() != null) {
            messageID.add(message.getMessageID());
        }
    }
    
    /**
     * 2a. Display the sender and recipient of all sent messages
     */
    public void displaySentMessagesSendersRecipients() {
        if (sentMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "No sent messages available.\nSend some messages first to see them here.", 
                "No Sent Messages", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        StringBuilder result = new StringBuilder();
        result.append("=== YOUR SENT MESSAGES ===\n\n");
        
        for (int i = 0; i < sentMessages.size(); i++) {
            Message msg = sentMessages.get(i);
            result.append("Message ").append(i + 1).append(":\n");
            result.append("To: ").append(msg.getRecipient()).append("\n");
            result.append("Message: ").append(truncateMessage(msg.getMessage(), 50)).append("\n");
            result.append("------------------------\n");
        }
        
        result.append("Total sent messages: ").append(sentMessages.size());
        
        JOptionPane.showMessageDialog(null, result.toString(), 
            "Your Sent Messages", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * 2b. Display the longest sent message
     */
    public void displayLongestSentMessage() {
        if (sentMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "No sent messages available.\nSend some messages first to use this feature.", 
                "No Sent Messages", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        Message longestMessage = sentMessages.get(0);
        
        for (Message msg : sentMessages) {
            if (msg.getMessage().length() > longestMessage.getMessage().length()) {
                longestMessage = msg;
            }
        }
        
        String result = "=== YOUR LONGEST SENT MESSAGE ===\n\n" +
                       "Message: \"" + longestMessage.getMessage() + "\"\n" +
                       "Length: " + longestMessage.getMessage().length() + " characters\n" +
                       "To: " + longestMessage.getRecipient() + "\n" +
                       "Message ID: " + longestMessage.getMessageID() + "\n" +
                       "Timestamp: " + longestMessage.getTimestamp();
        
        JOptionPane.showMessageDialog(null, result, 
            "Longest Sent Message", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * 2c. Search for a message ID and display the corresponding recipient and message
     */
    public void searchMessageByID() {
        if (sentMessages.isEmpty() && storedMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "No messages available to search.\nSend or store some messages first.", 
                "No Messages", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String searchID = JOptionPane.showInputDialog("Enter Message ID to search:");
        
        if (searchID == null || searchID.trim().isEmpty()) {
            return;
        }
        
        // Search in sent messages
        Message foundMessage = null;
        String messageType = "";
        
        for (Message msg : sentMessages) {
            if (msg.getMessageID().equals(searchID)) {
                foundMessage = msg;
                messageType = "Sent";
                break;
            }
        }
        
        // Search in stored messages if not found in sent
        if (foundMessage == null) {
            for (Message msg : storedMessages) {
                if (msg.getMessageID().equals(searchID)) {
                    foundMessage = msg;
                    messageType = "Stored";
                    break;
                }
            }
        }
        
        if (foundMessage != null) {
            String result = "=== MESSAGE FOUND ===\n\n" +
                           "Type: " + messageType + " Message\n" +
                           "Message ID: " + foundMessage.getMessageID() + "\n" +
                           "To: " + foundMessage.getRecipient() + "\n" +
                           "Message: " + foundMessage.getMessage() + "\n" +
                           "Hash: " + foundMessage.getMessageHash() + "\n" +
                           "Timestamp: " + foundMessage.getTimestamp();
            
            JOptionPane.showMessageDialog(null, result, 
                "Message Found", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, 
                "No message found with ID: " + searchID + 
                "\n\nCheck your Message IDs and try again.", 
                "Message Not Found", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * 2d. Search for all the messages sent to a particular recipient
     */
    public void searchMessagesByRecipient() {
        if (sentMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "No sent messages available.\nSend some messages first to use this feature.", 
                "No Sent Messages", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String recipient = JOptionPane.showInputDialog(
            "Enter recipient's number to search:\n(Format: +27831234567 or 0831234567)");
        
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
        
        if (!matchingMessages.isEmpty()) {
            StringBuilder result = new StringBuilder();
            result.append("=== MESSAGES TO: ").append(recipient).append(" ===\n\n");
            
            for (int i = 0; i < matchingMessages.size(); i++) {
                Message msg = matchingMessages.get(i);
                result.append("Message ").append(i + 1).append(":\n");
                result.append("ID: ").append(msg.getMessageID()).append("\n");
                result.append("Message: ").append(msg.getMessage()).append("\n");
                result.append("Time: ").append(msg.getTimestamp()).append("\n");
                result.append("------------------------\n");
            }
            
            result.append("Total messages to this recipient: ").append(matchingMessages.size());
            
            JOptionPane.showMessageDialog(null, result.toString(), 
                "Messages by Recipient", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, 
                "No messages found for recipient: " + recipient + 
                "\n\nCheck the recipient number and try again.", 
                "No Messages Found", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * 2e. Delete a message using the message hash
     */
    public void deleteMessageByHash() {
        if (sentMessages.isEmpty() && storedMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "No messages available to delete.\nSend or store some messages first.", 
                "No Messages", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String hashToDelete = JOptionPane.showInputDialog("Enter Message Hash to delete:");
        
        if (hashToDelete == null || hashToDelete.trim().isEmpty()) {
            return;
        }
        
        boolean deleted = false;
        Message deletedMessage = null;
        
        // Remove from sent messages
        for (int i = sentMessages.size() - 1; i >= 0; i--) {
            if (sentMessages.get(i).getMessageHash().equals(hashToDelete)) {
                deletedMessage = sentMessages.get(i);
                sentMessages.remove(i);
                deleted = true;
                break;
            }
        }
        
        // Remove from stored messages
        if (!deleted) {
            for (int i = storedMessages.size() - 1; i >= 0; i--) {
                if (storedMessages.get(i).getMessageHash().equals(hashToDelete)) {
                    deletedMessage = storedMessages.get(i);
                    storedMessages.remove(i);
                    deleted = true;
                    break;
                }
            }
        }
        
        // Remove from hash array
        messageHash.removeIf(hash -> hash.equals(hashToDelete));
        
        if (deleted && deletedMessage != null) {
            // Also remove from the main userMessages list if present
            userMessages.removeIf(msg -> msg.getMessageHash().equals(hashToDelete));
            
            JOptionPane.showMessageDialog(null, 
                "Message deleted successfully!\n\n" +
                "Message: " + truncateMessage(deletedMessage.getMessage(), 30) + "\n" +
                "To: " + deletedMessage.getRecipient() + "\n" +
                "Hash: " + hashToDelete, 
                "Message Deleted", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, 
                "No message found with hash: " + hashToDelete + 
                "\n\nCheck your message hash and try again.", 
                "Message Not Found", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * 2f. Display a report that lists the full details of all the sent messages
     */
    public void displaySentMessagesReport() {
        if (sentMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "No sent messages available for report.\nSend some messages first to generate a report.", 
                "No Sent Messages", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        StringBuilder report = new StringBuilder();
        report.append("=== YOUR SENT MESSAGES - FULL REPORT ===\n\n");
        
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
        
        report.append("SUMMARY:\n");
        report.append("Total Sent Messages: ").append(sentMessages.size()).append("\n");
        report.append("Total Stored Messages: ").append(storedMessages.size()).append("\n");
        report.append("Total Message Hashes: ").append(messageHash.size()).append("\n");
        
        JOptionPane.showMessageDialog(null, report.toString(), 
            "Your Sent Messages Report", JOptionPane.INFORMATION_MESSAGE);
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
            "Exit Message Helper"
        };
        
        boolean running = true;
        
        while (running) {
            int choice = JOptionPane.showOptionDialog(null,
                "=== MESSAGE HELPER ===\n\n" +
                "Manage and analyze your actual messages\n" +
                "Sent Messages: " + sentMessages.size() + "\n" +
                "Stored Messages: " + storedMessages.size() + "\n\n" +
                "Select an option:",
                "Message Helper - Your Messages",
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
                    JOptionPane.showMessageDialog(null, "Please select a valid option.");
            }
        }
    }
    
    /**
     * Helper method to truncate long messages for display
     */
    private String truncateMessage(String message, int maxLength) {
        if (message.length() <= maxLength) {
            return message;
        }
        return message.substring(0, maxLength) + "...";
    }
    
    // Getters for the arrays
    public ArrayList<Message> getSentMessages() { return sentMessages; }
    public ArrayList<Message> getDisregardedMessages() { return disregardedMessages; }
    public ArrayList<Message> getStoredMessages() { return storedMessages; }
    public ArrayList<String> getMessageHash() { return messageHash; }
    public ArrayList<String> getMessageID() { return messageID; }
}