import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClearButtonListener implements ActionListener {
    View view;

    public ClearButtonListener(View view) {
        this.view = view;
    }

    public void actionPerformed(ActionEvent event)
    {
        view.textArea.setText(null);
    }
}
