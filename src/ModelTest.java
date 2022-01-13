import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ModelTest {
    private Model model;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        model = new Model();
    }

    @Test
    public void correctTestClassTest(){
        assertTrue(model.correctTestClass("Test1"));
        assertFalse(model.correctTestClass("TestX"));
    }

    @Test
    public void listsNotNullTest(){
        model.runTest("Test1");
        assertFalse(model.resultsMessages == null);
        assertFalse(model.exceptions == null);
    }
/*
    @Test
    public void
*/
    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }
}