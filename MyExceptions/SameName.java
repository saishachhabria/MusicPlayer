package MyExceptions;
import javax.swing.JOptionPane;

public class SameName extends Exception implements MyExceptions
{
    public void get()
    {
        JOptionPane.showMessageDialog(null,"Playlist with that name already exists! Choose another name");
    }
}
