import se.umu.cs.unittest.TestClass;

public class Test4 implements TestClass{
    private MyInt myInt;

    public Test4() {
    }

    public void setUp() {
        myInt = new MyInt();
    }

    public void tearDown() {
        myInt = null;
    }

    // A test that uses a sleep method
    public boolean testSleep() throws InterruptedException {
        myInt.increment();
        Thread.sleep(2500);
        myInt.decrement();
        return myInt.value() == 0;
    }

    // A test that is run to check if the sleep method is respected
    public boolean testIncrement() {
        while (myInt.value() != 10) {
            myInt.increment();
        }
        return myInt.value() == 10;
    }
}
