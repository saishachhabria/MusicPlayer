package MyExceptions;
import javax.swing.JOptionPane;

public class NoSongSelected extends Exception implements MyExceptions
{
    public void get()
    {
        JOptionPane.showMessageDialog(null,"No song(s) selected!");
    }
}
