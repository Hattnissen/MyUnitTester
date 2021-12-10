import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SwingWorkerTask extends SwingWorker<Integer,Integer> {
    SwingWorkerModel model;

    //Constructor
    public SwingWorkerTask(SwingWorkerModel model) {
        this.model = model;
    }

    //eventuellt returnera meddelanden
    protected Integer doInBackground() {
        if (correctTestClass(model.test)) {
            try {
                runTest(model.test);
            } catch (Exception e) {
                System.out.println("Exception in doInBackground");
                e.printStackTrace();
            }
        }

        return 0;
    }

    public boolean correctTestClass(String test) {
        try {
            Class<?> c = Class.forName(test);
            if (se.umu.cs.unittest.TestClass.class.isAssignableFrom(c)) {
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
            model.exceptions.add("The class does not exist");
        } catch (NoSuchMethodException e) {
            model.exceptions.add("The class does not have a constructor");
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
                            model.resultsMessages.add(method.getName() + ": SUCCESS\n");
                            success++;
                            System.out.println("Success");
                        } else {
                            model.resultsMessages.add(method.getName() + ": FAIL\n");
                            fail++;
                            System.out.println("Fail");
                        }
                        if (tearDown != null) {
                            tearDown.invoke(instance);
                        }
                    }
                } catch (NullPointerException e) {
                    model.resultsMessages.add(method.getName() + ": FAIL Generated a " + e.getCause() + "\n");
                    failByException++;
                } catch (IllegalAccessException e) {
                    model.resultsMessages.add(method.getName() + ": FAIL Generated a " + e.getCause() + "\n");
                    failByException++;
                } catch (InvocationTargetException e) {
                    model.resultsMessages.add(method.getName() + ": FAIL Generated a " + e.getCause() + "\n");
                    failByException++;
                }
            }
        } catch (ClassNotFoundException e) {
            model.resultsMessages.add("Generated a " + e.getCause() + "\n");
        } catch (NoSuchMethodException e) {
            model.resultsMessages.add("Generated a " + e.getCause() + "\n");
        } catch (InstantiationException e) {
            model.resultsMessages.add("Generated a " + e.getCause() + "\n");
        } catch (IllegalAccessException e) {
            model.resultsMessages.add("Generated a " + e.getCause() + "\n");
        } catch (InvocationTargetException e) {
            model.resultsMessages.add("Generated a " + e.getCause() + "\n");
        }


        System.out.println("Körs finally?");
        model.results.add("\n");
        model.results.add(Integer.toString(success));
        model.results.add(" tests succeeded\n");
        model.results.add(Integer.toString(fail));
        model.results.add(" tests failed\n");
        model.results.add(Integer.toString(failByException));
        model.results.add(" tests failed because of an exception\n");
    }
}