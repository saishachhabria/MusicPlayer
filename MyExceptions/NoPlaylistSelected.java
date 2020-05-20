package MyExceptions;
import javax.swing.JOptionPane;

public class NoPlaylistSelected extends Exception implements MyExceptions
{
    public void get()
    {
        JOptionPane.showMessageDialog(null,"Please choose a playlist to proceed");
    }
}
