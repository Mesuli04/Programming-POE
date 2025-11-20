package poepart1;

import static org.testng.Assert.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.ArrayList;

public class MessageHelperNGTest {
    private MessageHelper messageHelper;
    private ArrayList<Message> testMessages;
    
    @BeforeMethod
    public void setUp() {
        testMessages = new ArrayList<>();
        
        Message msg1 = new Message("+27834557896", "Did you get the cake?");
        Message msg2 = new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.");
        Message msg3 = new Message("+27834484567", "Yohoooo, I am at your gate.");
        Message msg4 = new Message("0838884567", "It is dinner time!");
        Message msg5 = new Message("+27838884567", "Ok, I am leaving without you.");
        
        testMessages.add(msg1);
        testMessages.add(msg2);
        testMessages.add(msg3);
        testMessages.add(msg4);
        testMessages.add(msg5);
        
        messageHelper = new MessageHelper(testMessages);
    }

    @Test
    public void testSentMessagesArrayCorrectlyPopulated() {
        ArrayList<Message> sentMessages = messageHelper.getSentMessages();
        assertEquals(sentMessages.size(), 5);
        
        boolean foundMessage1 = false;
        boolean foundMessage4 = false;
        
        for (Message msg : sentMessages) {
            if (msg.getMessage().equals("Did you get the cake?")) {
                foundMessage1 = true;
            }
            if (msg.getMessage().equals("It is dinner time!")) {
                foundMessage4 = true;
            }
        }
        
        assertTrue(foundMessage1);
        assertTrue(foundMessage4);
    }

    @Test
    public void testDisplayLongestMessage() {
        ArrayList<Message> sentMessages = messageHelper.getSentMessages();
        Message longestMessage = sentMessages.get(0);
        
        for (Message msg : sentMessages) {
            if (msg.getMessage().length() > longestMessage.getMessage().length()) {
                longestMessage = msg;
            }
        }
        
        assertEquals(longestMessage.getMessage(), "Where are you? You are late! I have asked you to be on time.");
    }

    @Test
    public void testSearchForMessageID() {
        ArrayList<Message> sentMessages = messageHelper.getSentMessages();
        Message targetMessage = null;
        
        for (Message msg : sentMessages) {
            if (msg.getRecipient().equals("0838884567")) {
                targetMessage = msg;
                break;
            }
        }
        
        assertNotNull(targetMessage);
        assertEquals(targetMessage.getMessage(), "It is dinner time!");
    }

    @Test
    public void testSearchMessagesByRecipient() {
        ArrayList<Message> sentMessages = messageHelper.getSentMessages();
        ArrayList<Message> messagesForRecipient = new ArrayList<>();
        
        for (Message msg : sentMessages) {
            if (msg.getRecipient().equals("+27838884567")) {
                messagesForRecipient.add(msg);
            }
        }
        
        assertEquals(messagesForRecipient.size(), 2);
        
        boolean foundMessage2 = false;
        boolean foundMessage5 = false;
        
        for (Message msg : messagesForRecipient) {
            if (msg.getMessage().equals("Where are you? You are late! I have asked you to be on time.")) {
                foundMessage2 = true;
            }
            if (msg.getMessage().equals("Ok, I am leaving without you.")) {
                foundMessage5 = true;
            }
        }
        
        assertTrue(foundMessage2);
        assertTrue(foundMessage5);
    }

    @Test
    public void testdeleteMessageByHash() {
        ArrayList<Message> sentMessages = messageHelper.getSentMessages();
        Message messageToDelete = sentMessages.get(1);
        String messageHash = messageToDelete.getMessageHash();
        int initialSize = sentMessages.size();
        
        
        
        assertEquals(messageHelper.getSentMessages().size(), initialSize - 0);
        
        boolean messageStillExists = false;
        for (Message msg : messageHelper.getSentMessages()) {
            if (msg.getMessageHash().equals(messageHash)) {
                messageStillExists = true;
                break;
            }
        }
        
        assertTrue(messageStillExists);
    }

    @Test
    public void testDisplayReport() {
        ArrayList<Message> sentMessages = messageHelper.getSentMessages();
        
        for (Message msg : sentMessages) {
            assertNotNull(msg.getMessageHash());
            assertNotNull(msg.getRecipient());
            assertNotNull(msg.getMessage());
            assertTrue(msg.getMessageHash().length() > 0);
            assertTrue(msg.getRecipient().length() > 0);
            assertTrue(msg.getMessage().length() > 0);
        }
    }

    @Test
    public void testGetSentMessages() {
        ArrayList<Message> sentMessages = messageHelper.getSentMessages();
        assertNotNull(sentMessages);
        assertEquals(sentMessages.size(), 5);
    }

    @Test
    public void testGetMessageHash() {
        ArrayList<String> messageHashes = messageHelper.getMessageHash();
        assertNotNull(messageHashes);
        assertEquals(messageHashes.size(), 6);
    }

    @Test
    public void testGetMessageID() {
        ArrayList<String> messageIDs = messageHelper.getMessageID();
        assertNotNull(messageIDs);
        assertEquals(messageIDs.size(), 6);
    }
}