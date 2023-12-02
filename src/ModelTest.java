import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ModelTest
 * JUnit tests of the model class.
 *
 * Version: v.2.0
 *
 * Author: Johan Hultbäck
 * CS-user: id18jhk
 *
 */
class ModelTest {
    private Model model;
    ArrayList<String> results;
    String exceptionString;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        model = new Model();
    }

    @Test
    public void correctTestClassTest(){
        model.runTest("Test1");
    }
    @Test
    public void testClassNotExistTest(){
        exceptionString = "Test class TestX not found";
        results = model.runTest("TestX");

        assertEquals(exceptionString, results.get(0));
    }

    @Test
    public void interfaceNotImplementedTest(){
        exceptionString = "Test6 does not implement the correct interface\n\n";
        results = model.runTest("Test6");
        assertEquals(exceptionString, results.get(0));
    }

    @Test
    public void testHasParametersTest(){
        exceptionString = "Test3 does not have a valid constructor\n\n";
        results = model.runTest("Test3");
        assertEquals(exceptionString, results.get(0));
    }

    @Test
    public void hasConstructorTest(){
        exceptionString = "Test7 does not have a valid constructor\n\n";
        results = model.runTest("Test7");
        assertEquals(exceptionString, results.get(0));
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        model = null;
    }
}