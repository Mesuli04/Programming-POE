
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package poepart1;
/**
 *
 * @author RC_Student_Lab
 */

import static org.testng.Assert.*;
import org.testng.annotations.Test;

public class MessageNGTest {
    
    @Test
    public void testMessageHashIsCorrect() {
        Message message = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight");
        String hash = message.createMessageHash();
        assertTrue(hash.contains(":HITONIGHT"));
    }
    
    @Test
    public void testMessageIDIsCreated() {
        Message message = new Message("+27718693002", "Test message");
        assertTrue(message.checkMessageID());
        assertEquals(message.getMessageID().length(), 10);
    }
    
    @Test
    public void testMessageNotMoreThan250CharactersSuccess() {
        String shortMessage = "Short message";
        Message message = new Message("+27718693002", shortMessage);
        String result = message.sentMessage();
        assertEquals(result, "Message sent");
    }
    
    @Test
    public void testMessageNotMoreThan250CharactersFailure() {
        StringBuilder longMessage = new StringBuilder();
        for (int i = 0; i < 300; i++) {
            longMessage.append("a");
        }
        
        Message message = new Message("+27718693002", longMessage.toString());
        String result = message.sentMessage();
        assertEquals(result, "Please enter a message of less than 250 characters.");
    }
    
    @Test
    public void testRecipientNumberCorrectlyFormattedSuccess() {
        Message message = new Message("+27718693002", "Test message");
        boolean result = message.checkRecipientCell();
        assertTrue(result);
    }
    
    @Test
    public void testRecipientNumberIncorrectlyFormatted() {
        Message message = new Message("08575975889", "Test message");
        boolean result = message.checkRecipientCell();
        assertFalse(result);
    }
    
    @Test
    public void testReturnTotalMessages() {
        int initialCount = Message.returnTotalMessages();
        Message message1 = new Message("+27718693002", "First message");
        Message message2 = new Message("27718693002", "Second message");
        assertEquals(Message.returnTotalMessages(), initialCount + 2);
    }
}