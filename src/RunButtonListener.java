import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RunButtonListener implements ActionListener
{
    View view;
    Model model;
    public RunButtonListener(View view, Model model)
    {
        this.view = view;
        this.model = model;
    }

    public void actionPerformed(ActionEvent event)
    {
        SwingUtilities.invokeLater(new Runnable() {public void run() {model.update(view.textField.getText());}});
    }
}
