package com.webcheckers.util;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Message} component
 *
 * @author <a href='mailto:csc9411@rit.edu'>Christopher Curtice</a>
 */
@Tag("Model-tier")
class MessageTest {
    //Test row
    private Message testInfo;
    private Message testError;

    @BeforeEach
    void setUp() {
        testInfo = Message.info ("This is a test info message");
        testError = Message.error ("This is a test error message");
    }

    /** AfterEach
    void tearDown() {
    } */

    @Test
    void error() {
        assertEquals(Message.Type.ERROR, testError.getType());
        assertEquals("This is a test error message", testError.getText());
    }

    @Test
    void info() {
        assertEquals(Message.Type.INFO, testInfo.getType());
        assertEquals("This is a test info message", testInfo.getText());
    }

    @Test
    void getText() {
        assertEquals("This is a test info message", testInfo.getText());
        assertEquals("This is a test error message", testError.getText());
    }

    @Test
    void getType() {
        assertEquals(Message.Type.ERROR, testError.getType());
        assertEquals(Message.Type.INFO, testInfo.getType());
    }

    @Test
    void isSuccessful() {
        assertTrue(testInfo.isSuccessful());
        assertFalse(testError.isSuccessful());
    }

    @Test
    void testToString() {
        assertEquals("{Msg INFO 'This is a test info message'}", testInfo.toString());
        assertEquals("{Msg ERROR 'This is a test error message'}", testError.toString());
        assertNotEquals("{Msg INFO 'This is a test info message'}", testError.toString());
        assertNotEquals("{Msg ERROR 'This is a test error message'}", testInfo.toString());
    }
}
