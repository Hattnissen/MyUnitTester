import se.umu.cs.unittest.TestClass;

public class Test5 implements TestClass{
    private MyInt myInt;
    public Test5() {

    }

    public void setUp() {
        myInt=new MyInt();
    }

    public void tearDown() {
        myInt = null;
    }

    public MyInt testNotBoolean() {
        myInt.increment();
        return myInt;
    }
}
