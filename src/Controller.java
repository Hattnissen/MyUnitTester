import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class Controller implements ActionListener, ChangeListener, PropertyChangeListener {
    private final Model model;
    private final View view;
    private ArrayList<ChangeListener> listeners;
    private SwingWorker swingWorker;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        this.listeners = new ArrayList<>();
        setListeners();
    }

    public void addListener(ChangeListener listener) {
        listeners.add(listener);
    }

    private void setListeners()
    {
        SwingUtilities.invokeLater(() -> {
            view.setActionListenerTextField(e ->{
                SwingUtilities.invokeLater(new Runnable() {
                    synchronized public void run() {
                        swingWorker();
                    }
                });
            });

            view.setActionListenerRunButton(e -> {
                SwingUtilities.invokeLater(new Runnable() {
                    synchronized public void run() {
                        swingWorker();
                    }
                });
            });

            view.setActionListenerClearButton(e -> {
                view.textArea.setText(null);
            });

            addListener(this);
    });
    }

    private void swingWorker()
    {
        swingWorker = new SwingWorker()
        {
            @Override
            protected Integer doInBackground() {
                if (model.correctTestClass(view.textField.getText())) {
                    try {
                        model.runTest(view.textField.getText());
                    } catch (Exception e) {
                        System.out.println("Exception in doInBackground");
                        e.printStackTrace();
                    }
                }

                return 0;
            }
        };
        swingWorker.addPropertyChangeListener(this);
        swingWorker.execute();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void stateChanged(ChangeEvent e) {
        System.out.println("I stateChanged");

        if (!model.exceptions.isEmpty()) {
            view.writeListToView(model.exceptions);
        } else {
            view.writeToView("Summary of " + view.textField.getText() + ":\n");
            view.writeListToView(model.resultsMessages);
            view.writeListToView(model.results);
        }

        view.writeToView("\n");
        view.textField.setText(null);

        if(!model.exceptions.isEmpty())
        {
            model.exceptions.clear();
        }
        if (!model.resultsMessages.isEmpty()) {
            model.resultsMessages.clear();
        }
        if(!model.results.isEmpty())
        {
            model.results.clear();
        }
        System.out.println("Kört stateChanged");
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (swingWorker == null) {
            return;
        }
        if (swingWorker != event.getSource()) {
            return;
        }
        if(swingWorker.isDone())
        {
            swingWorker = null;
        }

        ChangeEvent e = new ChangeEvent(this);
        for (ChangeListener listener : listeners)
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    listener.stateChanged(e);
                }
            });
    }
}