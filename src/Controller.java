import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Controller
 * The controller class that handles the communication and
 * distributes information between the view and model
 * classes.
 *
 * Version: v.1.0
 * Author: Johan Hultbäck
 * CS-user: id18jhk
 */
public class Controller implements ActionListener, PropertyChangeListener {

    private final Model model;
    private final View view;
    private ArrayList<String> results;
    private SwingWorker swingWorker;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        this.results = new ArrayList<>();
        setListeners();
    }

    private void setListeners() {
        SwingUtilities.invokeLater(() -> {
            view.setActionListenerTextField(e -> {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        swingWorker();
                    }
                });
            });

            view.setActionListenerRunButton(e -> {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        swingWorker();
                    }
                });
            });

            view.setActionListenerClearButton(e -> {
                view.textArea.setText(null);
            });
        });
    }

    private void swingWorker() {
        if (swingWorker == null) {
            swingWorker = new SwingWorker() {
                @Override
                protected ArrayList<String> doInBackground() {
                    ArrayList<String> testResults = new ArrayList<>();

                    if (model.correctTestClass(view.textField.getText())) {
                        try {
                            testResults = model.runTest(view.textField.getText());
                        } catch (Exception e) {
                            model.exceptions.add("Could not run test");
                        }
                    } else {
                        return model.exceptions;
                    }
                    return testResults;
                }
            };
            swingWorker.addPropertyChangeListener(this);
            swingWorker.execute();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (swingWorker == null) {
            return;
        }
        if (swingWorker != event.getSource()) {
            return;
        }
        if (swingWorker.isDone()) {
            try {
                results = (ArrayList<String>) swingWorker.get();
                view.writeListToView(results);
                view.textField.setText(null);
                model.emptyLists();

            } catch (InterruptedException e) {
                model.exceptions.add("Interrupted while getting results from SwingWorker");
            } catch (ExecutionException e) {
                model.exceptions.add("Could not retrieve results");
            }
            swingWorker = null;
        }
    }
}