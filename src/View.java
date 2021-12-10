import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class View extends JPanel implements ChangeListener
{
    private static final long serialVersionUID = 1L;

    public JPanel topPanel;
    public JPanel bottomPanel;
    public JTextField textField;
    public JButton runButton;
    public JButton clearButton;
    public JTextArea textArea;

    private Test1 test = new Test1();

    private final Model model;

    public View(SwingWorkerModel model)
    {
        this.model = model;

        textArea = new JTextArea(15,50);
        textArea.setBorder(BorderFactory.createTitledBorder("Output"));
        textArea.setLayout(new FlowLayout(FlowLayout.CENTER));

        topPanel = new JPanel();
        topPanel.setBorder(BorderFactory.createTitledBorder("Tester"));
        topPanel.setLayout(new FlowLayout((FlowLayout.LEFT)));

        bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        textField = new JTextField("", 30);
        textField.setLayout(new FlowLayout(FlowLayout.RIGHT));

        runButton = new JButton("Run");
        clearButton = new JButton("Clear");

        topPanel.add(textField);
        topPanel.add(runButton);
        bottomPanel.add(clearButton);
    }

    public void stateChanged(ChangeEvent event)
    {
        System.out.println("I stateChanged");

        if(!model.exceptions.isEmpty())
        {
            for(String exception : model.exceptions) {
                textArea.append(exception);
            }
        }
        else {
            textArea.append("Summary of " + model.test + ":\n");
            for (String resultMessage : model.resultsMessages) {
                textArea.append(resultMessage);
                System.out.println("Händer det här?");
            }
            for (String result : model.results) {
                textArea.append(result);
            }
        }

            textArea.append("\n");
            textField.setText(null);
            if (!model.exceptions.isEmpty()) {
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

}