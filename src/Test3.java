import se.umu.cs.unittest.TestClass;

public class Test3 implements TestClass {

    public Test3(int parameters) {

    }

    // This class should not pass as a test class so
    // this test should not run
    public boolean testThatShouldNotRun() {
        int i = 0;
        while (i != 5) {
            i++;
        }
        return i == 5;
    }
}
