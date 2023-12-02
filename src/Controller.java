import javax.swing.*;
import java.util.ArrayList;

/**
 * Controller
 * The controller class that handles the communication and
 * distributes information between the view and model
 * classes.
 *
 * Version: v.2.0
 *
 * Author: Johan Hultbäck
 * CS-user: id18jhk
 */
public class Controller {

    private final Model model;
    private final View view;
    private ArrayList<String> results = new ArrayList<>();

    public Controller() {
        this.model = new Model();
        this.view = new View();
        SwingUtilities.invokeLater(() -> {
            view.initView();
            setListeners();
        });
    }

    private void setListeners() {
        view.setActionListenerTextField(e -> {
            new Worker().execute();
        });

        view.setActionListenerRunButton(e -> {
            new Worker().execute();
        });

        view.setActionListenerClearButton(e -> {
            view.textArea.setText(null);
        });
    }

    class Worker extends SwingWorker<ArrayList<String>,Void> {
        @Override
        protected ArrayList<String> doInBackground() {
            results = model.runTest(view.textField.getText());
            return results;
        }

        @Override
        protected void done() {
            view.textField.setText(null);
            view.writeListToView(results);
            model.emptyResults();
        }
    }
}