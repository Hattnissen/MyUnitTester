import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Model
 * The model class that runs tests and creates lists
 * containing the results that is to be presented
 * in the GUI.
 *
 * Version: v.2.0
 *
 * Author: Johan Hultbäck
 * CS-user: id18jhk
 *
 */
public class Model {
    private ArrayList<String> resultsMessages = new ArrayList<>();
    Class<?> testClass;
    private int success, fail, failByException = 0;

    public Model() {
    }

    public ArrayList<String> runTest(String test) {
        try {
            testClass = Class.forName(test);
            if(correctTestClass(testClass)) {
                runMethods(testClass);
            } else {
                return resultsMessages;
            }
        } catch (ClassNotFoundException e) {
            resultsMessages.add("Test class " + test + " not found");
            return resultsMessages;
        }

        if((success == 0 && fail == 0 && failByException == 0)) {
            resultsMessages.add("No correct test methods for " + test + "was found");
            return resultsMessages;
        }

        assembleResults();
        return resultsMessages;
    }

    public boolean correctTestClass(Class<?> testClass) {
        try {
            if (se.umu.cs.unittest.TestClass.class.isAssignableFrom(testClass)) {
                if (testClass.getDeclaredConstructor().getParameterAnnotations().length == 0) {
                    return true;
                }
            } else {
                resultsMessages.add(testClass.getName() + " does not implement the correct interface\n\n");
                return false;
            }
        } catch (NoSuchMethodException e) {
            resultsMessages.add(testClass.getName() + " does not have a valid constructor\n\n");
        }
        return false;
    }

    public void runMethods(Class<?> testClass) {
        Method setUp = null;
        Method tearDown = null;
        String subString;
        Object instance;

        Method[] methods = testClass.getMethods();
        try {
            instance = testClass.getDeclaredConstructor().newInstance();
            resultsMessages.add("Summary of " + testClass + ":\n");

            for (Method method : methods) {
                if (Objects.equals(method.getName(), "setUp")) {
                    setUp = method;
                }
                if (Objects.equals(method.getName(), "tearDown")) {
                    tearDown = method;
                }
            }

            for (Method method : methods) {
                try {
                    subString = method.getName().substring(0, 4);

                    if ((Objects.equals(subString, "test")) && (method.getParameterAnnotations().length == 0)
                            && (method.getReturnType().equals(Boolean.TYPE))) {
                        if (setUp != null) {
                            setUp.invoke(instance);
                        }
                        if (method.invoke(instance).equals(true)) {
                            resultsMessages.add(method.getName() + ": SUCCESS\n");
                            success++;
                        } else {
                            resultsMessages.add(method.getName() + ": FAIL\n");
                            fail++;
                        }
                        if (tearDown != null) {
                            tearDown.invoke(instance);
                        }
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    resultsMessages.add(method.getName() + ": FAIL Generated a " + e.getCause() + "\n");
                    failByException++;
                    if (tearDown != null) {
                        tearDown.invoke(instance);
                    }
                }
            }
        } catch (NoSuchMethodException e) {
            resultsMessages.add(testClass.getName() + " has no constructor");
        } catch (InvocationTargetException e) {
            resultsMessages.add("The constructor of " + testClass.getName() + " threw an exception");
        } catch (InstantiationException e) {
            resultsMessages.add("The " + testClass.getName() + " could not be instantiated");
        } catch (IllegalAccessException e) {
            resultsMessages.add("Access to " + testClass.getName() + " was denied");
        }
    }

    private void assembleResults() {
        resultsMessages.add("\n");
        resultsMessages.add(Integer.toString(success));
        resultsMessages.add(" tests succeeded\n");
        resultsMessages.add(Integer.toString(fail));
        resultsMessages.add(" tests failed\n");
        resultsMessages.add(Integer.toString(failByException));
        resultsMessages.add(" tests failed because of an exception\n\n");
    }

    public void emptyResults() {
        if (!resultsMessages.isEmpty()) {
            resultsMessages.clear();
        }
        if (success != 0) {
            success = 0;
        }
        if (fail != 0) {
            fail = 0;
        }
        if (failByException != 0) {
            failByException = 0;
        }
    }
}