/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package poepart1;

import java.util.Random;
/**
 *
 * @author RC_Student_Lab
 */
public class Message {
    private String messageID;
    private String recipient;
    private String message;
    private String messageHash;
    private static int messageCount = 0;
    private int messageNumber;

    public Message(String recipient, String message) {
        this.messageID = generateMessageID();
        this.recipient = recipient;
        this.message = message;
        this.messageNumber = ++messageCount;
        this.messageHash = createMessageHash();
    }

    public boolean checkMessageID() {
        return messageID != null && messageID.length() == 10;
    }

    public boolean checkRecipientCell() {
        if (recipient == null) return false;
        String cleanNumber = recipient.replace("+27", "27");
        return cleanNumber.matches("27[0-9]{9}");
    }

    public String createMessageHash() {
        String firstTwoID = messageID.substring(0, 2);
        String[] words = message.split("\\s+");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 1 ? words[words.length - 1] : firstWord;
        
        return (firstTwoID + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    public String sentMessage() {
        if (!checkRecipientCell()) {
            return "Invalid recipient number";
        }
        if (message.length() > 250) {
            return "Please enter a message of less than 250 characters.";
        }
        return "Message sent";
    }

    public String printMessage() {
        return "MessageID: " + messageID + "\n" +
               "Message Hash: " + messageHash + "\n" +
               "Recipient: " + recipient + "\n" +
               "Message: " + message;
    }

    public static int returnTotalMessages() {
        return messageCount;
    }

    public void storeMessage() {
        // JSON storage functionality would be implemented here
        // Using ChatGPT for JSON implementation as per requirements
    }

    private String generateMessageID() {
        Random rand = new Random();
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            id.append(rand.nextInt(10));
        }
        return id.toString();
    }

    public String getMessageID() { return messageID; }
    public String getRecipient() { return recipient; }
    public String getMessage() { return message; }
    public String getMessageHash() { return messageHash; }
    public int getMessageNumber() { return messageNumber; }
}