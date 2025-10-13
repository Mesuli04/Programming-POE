package poepart1;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.Assert;


public class LoginNGTest {
    
    private Login login;
    
    @BeforeMethod
    public void setUp() {
       
        login = new Login("John", "Doe");
    }
    
    
    @Test
    public void testUsernameCorrectlyFormatted() {
      
        String username = "k_yl1";
        login.registerUser(username, "ValidPass1!", "+27123456789");
        Assert.assertTrue(login.checkUserName(), "Username should be correctly formatted");
    }
    
    @Test
    public void testUsernameIncorrectlyFormattedNoUnderscore() {
        
        String username = "kyle!!!!!!!";
        login.registerUser(username, "ValidPass1!", "+27123456789");
        Assert.assertFalse(login.checkUserName(), "Username should be incorrectly formatted - no underscore");
    }

    
    
    @Test
    public void testPasswordMeetsComplexityRequirements() {
        
        String password = "Ch&&sec@ke99!";
        login.registerUser("u_ser", password, "+27123456789");
        Assert.assertTrue(login.checkPasswordComplexity(), "Password should meet complexity requirements");
    }
    
    @Test
    public void testPasswordDoesNotMeetComplexityRequirements() {
       
        String password = "password";
        login.registerUser("u_ser", password, "+27123456789");
        Assert.assertFalse(login.checkPasswordComplexity(), "Password should not meet complexity requirements");
    }
    
    @Test
    public void testPasswordTooShort() {
        String password = "Pass1!";
        login.registerUser("u_ser", password, "+27123456789");
        Assert.assertFalse(login.checkPasswordComplexity(), "Password should be too short");
    }
    
    @Test
    public void testPasswordNoCapital() {
        String password = "password1!";
        login.registerUser("u_ser", password, "+27123456789");
        Assert.assertFalse(login.checkPasswordComplexity(), "Password should have no capital letter");
    }
    
  
    @Test
    public void testCellPhoneNumberCorrectlyFormattedWithoutPlus() {
        String cellPhone = "27123456789";
        login.registerUser("u_ser", "ValidPass1!", cellPhone);
        Assert.assertTrue(login.checkCellPhoneNumber(), "Cell phone should be correctly formatted with 27");
    }
    
    @Test
    public void testCellPhoneNumberIncorrectlyFormatted() {
        // Test Data: "08966553"
        String cellPhone = "08966553";
        login.registerUser("u_ser", "ValidPass1!", cellPhone);
        Assert.assertFalse(login.checkCellPhoneNumber(), "Cell phone should be incorrectly formatted");
    }
    
    @Test
    public void testRegistrationSuccessMessage() {
        String result = login.registerUser("k_yl1", "Ch&&sec@ke99!", "+27123456789");
        Assert.assertTrue(result.contains("Username successfully captured"));
        Assert.assertTrue(result.contains("Password successfully captured"));
        Assert.assertTrue(result.contains("Cell phone number successfully added"));
        Assert.assertTrue(result.contains("User registered successfully"));
    }
    
    @Test
    public void testRegistrationUsernameErrorMessage() {
        String result = login.registerUser("kyle!!!!!!!", "Ch&&sec@ke99!", "+27123456789");
        Assert.assertTrue(result.contains("Username is not correctly formatted"));
        Assert.assertFalse(login.isRegistered(), "User should not be registered");
    }
    
    
    // Test Login Functionality
    
    @Test
    public void testLoginSuccessful() {
        // First register a user
        login.registerUser("k_yl1", "Ch&&sec@ke99!", "+27123456789");
        
        // Then test login with correct credentials
        Assert.assertTrue(login.loginUser("k_yl1", "Ch&&sec@ke99!"), "Login should be successful");
    }
    
    @Test
    public void testLoginFailedWrongUsername() {
        // First register a user
        login.registerUser("k_yl1", "Ch&&sec@ke99!", "+27123456789");
        
        // Then test login with wrong username
        Assert.assertFalse(login.loginUser("wronguser", "Ch&&sec@ke99!"), "Login should fail with wrong username");
    }
    
    @Test
    public void testLoginFailedWrongPassword() {
        // First register a user
        login.registerUser("k_yl1", "Ch&&sec@ke99!", "+27123456789");
        
        // Then test login with wrong password
        Assert.assertFalse(login.loginUser("k_yl1", "wrongpassword"), "Login should fail with wrong password");
    }
 
    
    // Test Login Status Messages
    
    @Test
    public void testReturnLoginStatusSuccessful() {
        // First register a user
        login.registerUser("k_yl1", "Ch&&sec@ke99!", "+27123456789");
        
        String result = login.returnLoginStatus("k_yl1", "Ch&&sec@ke99!");
        Assert.assertTrue(result.contains("Welcome John Doe, it is great to see you again."), 
                         "Login status should show welcome message");
    }
    
    @Test
    public void testReturnLoginStatusFailed() {
        // First register a user
        login.registerUser("k_yl1", "Ch&&sec@ke99!", "+27123456789");
        
        String result = login.returnLoginStatus("k_yl1", "wrongpassword");
        Assert.assertTrue(result.contains("Username or password incorrect, please try again."), 
                         "Login status should show error message");
    }
}