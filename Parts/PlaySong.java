package Parts;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.util.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.*;
import javafx.embed.swing.*;

public class PlaySong implements ActionListener
{   
    public Song s; public int k; public ArrayList<Song> a;
    public static MediaPlayer media; public static PlaySong prev = new PlaySong(); 
    public static boolean alreadyPlaying = false; public JFrame nowplaying = new JFrame("Now Playing");
    public PlaySong()
    {
        alreadyPlaying = false;
        s = new Song("","","",0);
    }

    public PlaySong(Song s,int k,ArrayList<Song> a)
    {        
        try
        {
            this.s = s; this.a = a; this.k = k;
            new JFXPanel();
            Media hit = new Media(new File("/Users/saishachhabria/Desktop/Computer Science/Mini Project copy/Songs/"+s.getName()+".mp3").toURI().toString());            
            if(alreadyPlaying)
            {
                prev.nowplaying.dispose();
                if(media.getStatus() == MediaPlayer.Status.PLAYING)
                    media.stop();
            }
            alreadyPlaying = true; nowplaying.setLayout(null);
            JLabel song = new JLabel(s.getName(),SwingConstants.CENTER); 
            song.setToolTipText("Song Name"); song.setBounds(50,50,200,20); nowplaying.add(song);
            JLabel artist = new JLabel(s.getArtist(),SwingConstants.CENTER); 
            artist.setToolTipText("Artist"); artist.setBounds(50,80,200,20); nowplaying.add(artist);
            JLabel album = new JLabel(s.getAlbum(),SwingConstants.CENTER); 
            album.setToolTipText("Album"); album.setBounds(50,110,200,20); nowplaying.add(album);
            String st = "";
            for(int i=0;i<s.getStars();i++)
                st+="\u2605";
            JLabel stars = new JLabel(st,SwingConstants.CENTER); stars.setBounds(50,140,200,20); nowplaying.add(stars);

            JButton previous = new JButton("<<"); previous.setActionCommand("previous"); previous.addActionListener(this); 
            previous.setToolTipText("Previous Song"); previous.setBounds(50,190,50,40); nowplaying.add(previous);
            JButton play = new JButton("\u25B6 / ||"); play.setActionCommand("play"); play.addActionListener(this);
            play.setToolTipText("Play/Plause"); play.setBounds(120,190,70,40); nowplaying.add(play); 
            JButton next = new JButton(">>"); next.setActionCommand("next"); next.addActionListener(this);
            next.setToolTipText("Next Song"); next.setBounds(210,190,50,40); nowplaying.add(next);
            nowplaying.setVisible(true); nowplaying.setSize(300,300);
            
            media = new MediaPlayer(hit); media.play(); prev = this;        
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,"You must place the mp3 file in the folder first");
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        if(e.getActionCommand().equals("play"))
        {
            if(media.getStatus() == MediaPlayer.Status.PAUSED)
                media.play();
           else
                media.pause();
        }
        else if(e.getActionCommand().equals("previous"))
        {
            if(k-1 >= 0)
                new PlaySong(a.get(--k), k, a);
            else
            {
                nowplaying.dispose();
                media.stop();
            }
        }
        else if(e.getActionCommand().equals("next"))
        {
            if(k+1 < a.size())
                new PlaySong(a.get(++k), k, a);
            else
            {
                nowplaying.dispose(); media.stop();
            }
        }
    }
}

