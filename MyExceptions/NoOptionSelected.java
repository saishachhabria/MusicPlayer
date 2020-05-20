package MyExceptions;
import javax.swing.JOptionPane;

public class NoOptionSelected extends Exception implements MyExceptions
{
    public void get()
    {
        JOptionPane.showMessageDialog(null,"Please choose an option to proceed");
    }
}
