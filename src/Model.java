import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Model
 *
 * Version: v.1.0
 *
 * Author: Johan Hultbäck
 * CS-user: id18jhk
 *
 */
public class Model {
    public ArrayList<String> resultsMessages;
    public ArrayList<String> exceptions;

    public Model() {
        this.resultsMessages = new ArrayList<>();
        this.exceptions = new ArrayList<>();
    }

    public boolean correctTestClass(String test) {
        try {
            Class<?> c = Class.forName(test);
            if (se.umu.cs.unittest.TestClass.class.isAssignableFrom(c)) {
                if (c.getDeclaredConstructor().getParameterAnnotations().length == 0) {
                    return true;
                }
            } else {
                exceptions.add(test + " is not a correct test class\n\n");
                return false;
            }
        } catch (ClassNotFoundException e) {
            exceptions.add(test + " does not exist\n\n");
        } catch (NoSuchMethodException e) {
            exceptions.add(test + " does not have a valid constructor\n\n");
        }
        return false;
    }

    public ArrayList<String> runTest(String test) {
        int success = 0;
        int fail = 0;
        int failByException = 0;

        try {
            Class<?> c = Class.forName(test);
            Method[] methods = c.getMethods();
            Object instance = c.getDeclaredConstructor().newInstance();
            Method setUp = null;
            Method tearDown = null;
            String subString;
            resultsMessages.add("Summary of " + test + ":\n");

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
                } catch (NullPointerException e) {
                    resultsMessages.add(method.getName() + ": FAIL Generated a " + e.getCause() + "\n");
                    failByException++;
                    tearDown.invoke(instance);
                } catch (IllegalAccessException e) {
                    resultsMessages.add(method.getName() + ": FAIL Generated a " + e.getCause() + "\n");
                    failByException++;
                    tearDown.invoke(instance);
                } catch (InvocationTargetException e) {
                    resultsMessages.add(method.getName() + ": FAIL Generated a " + e.getCause() + "\n");
                    failByException++;
                    tearDown.invoke(instance);
                }
            }
        } catch (ClassNotFoundException e) {
            resultsMessages.add("Generated a " + e.getCause() + "\n");
        } catch (NoSuchMethodException e) {
            resultsMessages.add("Generated a " + e.getCause() + "\n");
        } catch (InstantiationException e) {
            resultsMessages.add("Generated a " + e.getCause() + "\n");
        } catch (IllegalAccessException e) {
            resultsMessages.add("Generated a " + e.getCause() + "\n");
        } catch (InvocationTargetException e) {
            resultsMessages.add("Generated a " + e.getCause() + "\n");
        }

        resultsMessages.add("\n");
        resultsMessages.add(Integer.toString(success));
        resultsMessages.add(" tests succeeded\n");
        resultsMessages.add(Integer.toString(fail));
        resultsMessages.add(" tests failed\n");
        resultsMessages.add(Integer.toString(failByException));
        resultsMessages.add(" tests failed because of an exception\n\n");

        return resultsMessages;
    }

    public void emptyLists() {
        if (!resultsMessages.isEmpty()) {
            resultsMessages.clear();
        }
        if (!exceptions.isEmpty()) {
            exceptions.clear();
        }
    }
}