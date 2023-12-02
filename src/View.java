import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * View
 * The view class that handles the GUI and presents the
 * information from the Model that the Controller has
 * communicated.
 *
 * Version: v.2.0
 *
 * Author: Johan Hultbäck
 * CS-user: id18jhk
 *
 *
 */
public class View extends JPanel {
    private static final long serialVersionUID = 1L;

    public JPanel topPanel;
    public JPanel bottomPanel;
    public JTextField textField;
    public JButton runButton;
    public JButton clearButton;
    public JTextArea textArea;

    public View() {
    }

    public void initView() {
        textArea = new JTextArea(15,50);
        textArea.setBorder(BorderFactory.createTitledBorder("Output"));
        textArea.setLayout(new FlowLayout(FlowLayout.CENTER));
        textArea.setEditable(false);

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

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        JFrame frame = new JFrame("MyUnitTester");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.getContentPane();
        frame.pack();
        frame.setVisible(true);
    }

    public void writeListToView(ArrayList<String> list) {
        for (String string : list) {
            textArea.append(string);
        }
    }

    public void setActionListenerTextField(ActionListener actionListener) {
        textField.addActionListener(actionListener);
    }

    public void setActionListenerRunButton(ActionListener actionListener) {
        runButton.addActionListener(actionListener);
    }

    public void setActionListenerClearButton(ActionListener actionListener) {
        clearButton.addActionListener(actionListener);
    }
}