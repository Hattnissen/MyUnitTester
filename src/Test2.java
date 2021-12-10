import se.umu.cs.unittest.TestClass;

public class Test2 implements TestClass {
    private MyString string;
    private String str;
    public Test2() {
    }

    public void setUp()
    {
        string = new MyString();
        str = "test";
    }

    public void tearDown()
    {
        string = null;
        str = null;
    }

    // Test that should succeed
    public boolean testInitialization()
    {
        return string.getStr().equals("test");
    }

    // Test that should succeed
    public boolean testStringEqual()
    {
        return string.getStr().equals(str);
    }

    // Test that should fail
    public boolean testStringNotEqual()
    {
        string.addTestToString();
        return string.getStr().equals(str);
    }

    // Test that should fail by exception
    public boolean testStringNull()
    {
        string.nullString();
        return string.getStr().equals(str);
    }
}
