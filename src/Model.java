import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class Model {
    public ArrayList<String> resultsMessages;
    public ArrayList<String> results;
    public ArrayList<String> exceptions;

    public Model() {
        this.resultsMessages = new ArrayList<>();
        this.results = new ArrayList<>();
        this.exceptions = new ArrayList<>();
    }

    public boolean correctTestClass(String test) {
        try {
            Class<?> c = Class.forName(test);
            if (TestClass.class.isAssignableFrom(c)) {
                System.out.println("Class " + c.getName() + " implements TestClass");
                if (c.getDeclaredConstructor().getParameterAnnotations().length == 0) {
                    System.out.println("Class " + c.getName() + " has a constructor without parameters");
                    return true;
                }
            } else {
                System.out.println("Not a real test class");
                return false;
            }
        } catch (ClassNotFoundException e) {
            exceptions.add("The class does not exist");
        } catch (NoSuchMethodException e) {
            exceptions.add("The class does not have a constructor");
        }
        return false;
    }

    public void runTest(String test) {
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

            for (Method method : methods) {
                if (method.getName().equals("setUp")) {
                    setUp = method;
                }
                if (method.getName().equals("tearDown")) {
                    tearDown = method;
                }
            }

            for (Method method : methods) {
                try {
                    subString = method.getName().substring(0, 4);
                    System.out.println(subString);

                    if ((subString.equals("test")) && (method.getParameterAnnotations().length == 0) && (method.getReturnType().equals(Boolean.TYPE))) {
                        if (setUp != null) {
                            setUp.invoke(instance);
                        }
                        if (method.invoke(instance).equals(true)) {
                            resultsMessages.add(method.getName() + ": SUCCESS\n");
                            success++;
                            System.out.println("Success");
                        } else {
                            resultsMessages.add(method.getName() + ": FAIL\n");
                            fail++;
                            System.out.println("Fail");
                        }
                        if (tearDown != null) {
                            tearDown.invoke(instance);
                        }
                    }
                } catch (NullPointerException e) {
                    resultsMessages.add(method.getName() + ": FAIL Generated a " + e.getCause() + "\n");
                    failByException++;
                } catch (IllegalAccessException e) {
                    resultsMessages.add(method.getName() + ": FAIL Generated a " + e.getCause() + "\n");
                    failByException++;
                } catch (InvocationTargetException e) {
                    resultsMessages.add(method.getName() + ": FAIL Generated a " + e.getCause() + "\n");
                    failByException++;
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


        System.out.println("Körs finally?");
        results.add("\n");
        results.add(Integer.toString(success));
        results.add(" tests succeeded\n");
        results.add(Integer.toString(fail));
        results.add(" tests failed\n");
        results.add(Integer.toString(failByException));
        results.add(" tests failed because of an exception\n");
    }
}