import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.util.*;
import java.io.*;
import Parts.*;
import MyExceptions.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Choice implements ActionListener
{
    private JFrame thisIsIt; 
    private JButton create,edit,listsongs,delete;
    static int i = -1; boolean flag;
    int indices[];
    ArrayList<Playlist> p = new ArrayList<>();
    static ArrayList<Song> allSongs = new ArrayList<>();
    public Choice() 
    {
        thisIsIt = new JFrame("Playlists"); 
        thisIsIt.setLocation(450,100); 
        thisIsIt.setLayout(new FlowLayout());   
        if(p.size() == 0)
        {
            getAllSongs(); loadPlaylists();
        }
        String ll[][] = new String[p.size()][1];
        String hh[] = {"Playlists"};
        for(int j = 0;j<p.size();j++)
            ll[j][0] = p.get(j).name;
        JTable table1 = new JTable(new DefaultTableModel(ll, hh) {
                    public boolean isCellEditable(int row, int col) {
                        return false;
                    }
                });
        JScrollPane jsp = new JScrollPane(table1); thisIsIt.add(jsp);
        ListSelectionModel select = table1.getSelectionModel();
        select.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        select.addListSelectionListener(new ListSelectionListener(){
                public void valueChanged(ListSelectionEvent e)
                {
                    if(!(e.getValueIsAdjusting()))
                        i = table1.getSelectedRow();
                }
            });
        create = new JButton("+ New Playlist"); 
        create.setToolTipText("Create new playlist");
        edit = new JButton("Edit"); 
        edit.setToolTipText("Modify existing playlist");
        listsongs = new JButton("Show all songs"); 
        listsongs.setToolTipText("Displays a list of the songs");
        delete = new JButton("Delete"); 
        delete.setToolTipText("Delete existing playlist");
        create.setActionCommand("create"); 
        create.addActionListener(this); thisIsIt.add(create);            
        edit.setActionCommand("edit"); 
        edit.addActionListener(this); thisIsIt.add(edit);
        listsongs.setActionCommand("list"); 
        listsongs.addActionListener(this); thisIsIt.add(delete);
        delete.setActionCommand("delete"); 
        delete.addActionListener(this); 
        thisIsIt.add(listsongs); thisIsIt.setVisible(true);
        thisIsIt.setResizable(false); thisIsIt.setSize(500,520);
        thisIsIt.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
    }  

    public void loadPlaylists() 
    {
        try
        {
            File file = new File("Playlists.txt");
            Scanner sc = new Scanner(file);
            while(sc.hasNextLine())
            {
                String s = sc.nextLine();
                load(s,s+".txt");
            }
            sc.close();
        }
        catch(IOException e) {;}
    }

    public void load(String name,String fileName) 
    {
        Playlist p1 = new Playlist(name);
        try
        {
            File file = new File(fileName);
            Scanner sc = new Scanner(file).useDelimiter("\t\t\t");
            while(sc.hasNextLine())
            {
                p1.addSong(sc.next(),sc.next(),sc.next(),sc.nextInt());
                sc.nextLine();
            }
            sc.close();
        }
        catch(FileNotFoundException e)
        {
            JOptionPane.showMessageDialog(null,name+" file not found!");
        }
        p.add(p1);
    }

    public void getAllSongs()
    {
        File file = new File("all.txt");
        try
        {
            Scanner sc = new Scanner(file).useDelimiter("\t\t\t");
            while(sc.hasNextLine())
            {
                allSongs.add(new Song(sc.next(),sc.next(),sc.next(),sc.nextInt()));
                sc.nextLine();
            }            
            sc.close();
        }
        catch(FileNotFoundException e)
        {
            JOptionPane.showMessageDialog(null,"File not found!");
        }
    }

    public void listOfSongs()
    {
        String listall[][] = new String[allSongs.size()][4];
        for(int j = 0;j<allSongs.size();j++)
            listall[j] = allSongs.get(j).display();
        JFrame f = new JFrame("All Songs"); f.setLocation(400,75); f.setLayout(new FlowLayout());
        String headers[] = {"Name","Artist","Album","Stars"};
        JTable table2 = new JTable(new DefaultTableModel(listall, headers) {
                    public boolean isCellEditable(int row, int col) 
                    {
                        return false;
                    }
                });
        JScrollPane jsp = new JScrollPane(table2); f.add(jsp);
        ListSelectionModel select = table2.getSelectionModel();
        select.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        flag = false;
        select.addListSelectionListener(new ListSelectionListener(){
                public void valueChanged(ListSelectionEvent e)
                {
                    if(!(e.getValueIsAdjusting()))
                    {
                        flag = true;
                        indices = table2.getSelectedRows();
                    }
                }
            });
        table2.addMouseListener(new MouseAdapter(){
                public void mouseClicked(MouseEvent e)
                {
                    if(e.getClickCount() == 2 && !e.isConsumed()) 
                    {
                        e.consume();
                        int k = table2.getSelectedRow(); 
                        new PlaySong(allSongs.get(k),k,allSongs);
                    }
                }
            });
        JButton b1 = new JButton("Add");
        JButton b2 = new JButton("Remove");
        JButton b3 = new JButton("Edit");
        JButton b4 = new JButton("Done");

        Playlist all = new Playlist("All");
        for(int j = 0;j<allSongs.size();j++)
            all.addSong(allSongs.get(j).getName(),allSongs.get(j).getArtist(),allSongs.get(j).getAlbum(),allSongs.get(j).getStars());
        String options[]={"Sort by:","Name","Artist","Album","Stars"};
        JComboBox sort = new JComboBox(options);
        sort.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {
                    try
                    {
                        if(sort.getSelectedIndex()  ==  0);
                        else if(sort.getSelectedIndex()  ==  1)
                            all.sortByName();
                        else if(sort.getSelectedIndex()  ==  2)
                            all.sortByArtist();
                        else if(sort.getSelectedIndex()  ==  3)
                            all.sortByAlbum();
                        else if(sort.getSelectedIndex() ==  4)    
                            all.sortByStars();
                        for(int j = 0;j<allSongs.size();j++)
                            allSongs.set(j,all.list.get(j));
                        FileWriter fw = new FileWriter("all.txt");
                        BufferedWriter bw = new BufferedWriter(fw);
                        for(int j = 0;j< allSongs.size();j++)
                            bw.write(all.list.get(j).writeFile());
                        bw.close(); fw.close();
                    }
                    catch(IOException ee)
                    {
                        JOptionPane.showMessageDialog(null,"File not found");
                    }                  
                    f.dispose(); listOfSongs();
                }
            });
        f.add(b1); f.add(b2); 
        f.add(sort); f.add(b3); f.add(b4);
        b2.setToolTipText("Click on the songs, then click on this button to remove them");
        b1.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {
                    JFrame newSong = new JFrame("Add new song");
                    newSong.setLocation(425,175);newSong.setLayout(null);

                    JLabel name = new JLabel("Name: "); 
                    name.setBounds(50,50,100,20);
                    JLabel artist = new JLabel("Artist: "); 
                    artist.setBounds(50,100,100,20);
                    JLabel album = new JLabel("Album: "); 
                    album.setBounds(50,150,100,20);
                    JLabel rating = new JLabel("Rating: "); 
                    rating.setBounds(50,200,100,20);
                    JTextField name1 = new JTextField(); 
                    name1.setBounds(150,50,200,20);
                    JTextField artist1 = new JTextField(); 
                    artist1.setBounds(150,100,200,20);
                    JTextField album1 = new JTextField(); 
                    album1.setBounds(150,150,200,20);
                    JTextField rating1 = new JTextField(); 
                    rating1.setBounds(150,200,200,20);
                    newSong.add(name); newSong.add(name1); 
                    newSong.add(artist); newSong.add(artist1);
                    newSong.add(album); newSong.add(album1);
                    newSong.add(rating); newSong.add(rating1);

                    JButton done = new JButton("Done"); 
                    done.setBounds(140,275,85,30); newSong.add(done);
                    JButton cancel = new JButton("Cancel"); 
                    cancel.setBounds(240,275,85,30); newSong.add(cancel); 
                    done.addActionListener(new ActionListener(){
                            public void actionPerformed(ActionEvent e)
                            {
                                try
                                {
                                    String aaaa = rating1.getText();
                                    if(aaaa.equals(""))
                                        aaaa = "0";

                                    Song s = new Song(name1.getText(),artist1.getText(),album1.getText(),Integer.parseInt(aaaa));
                                    newSong.dispose(); allSongs.add(s);

                                    FileWriter fw = new FileWriter("all.txt",true);
                                    BufferedWriter bw = new BufferedWriter(fw);
                                    bw.write(s.getName()+"\t\t\t"+s.getArtist()+"\t\t\t"+s.getAlbum()+"\t\t\t"+s.getStars()+"\t\t\t\n");
                                    bw.close(); fw.close();
                                }
                                catch(IOException ee)
                                {
                                    JOptionPane.showMessageDialog(null,"IOException");
                                }
                                catch(NumberFormatException ae)
                                {
                                    JOptionPane.showMessageDialog(null,"Rating must be a number");
                                }
                                f.dispose(); listOfSongs();
                            }
                        });
                    cancel.addActionListener(new ActionListener(){
                            public void actionPerformed(ActionEvent e)
                            {
                                newSong.dispose();
                            }
                        });
                    newSong.setVisible(true); newSong.setSize(425,375);
                }
            });
        b2.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {
                    try
                    {
                        if(!flag)
                            throw new NoSongSelected();                              
                        if(indices.length > 1)
                            indices = sort(indices);
                        for(int j = 0; j<indices.length; j++)
                        {
                            if(allSongs.get(indices[j]-j).getName().equals(PlaySong.prev.s.getName()))
                            {
                                PlaySong.prev.nowplaying.dispose();
                                if(PlaySong.media.getStatus() == MediaPlayer.Status.PLAYING)
                                {
                                    PlaySong.media.stop();
                                    PlaySong.alreadyPlaying = false;
                                }
                            }
                            allSongs.remove(indices[j]-j);
                        }
                        FileWriter fw = new FileWriter("all.txt");
                        BufferedWriter bw = new BufferedWriter(fw);
                        for(int j = 0; j<allSongs.size(); j++)
                            bw.write(allSongs.get(j).writeFile());
                        bw.close(); fw.close(); 
                        f.dispose(); listOfSongs();
                    }                    
                    catch(NoSongSelected ae)
                    {
                        ae.get();
                    }
                    catch(IOException ee)
                    {
                        JOptionPane.showMessageDialog(null,"IOException");
                    }
                }
            });
        b3.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {
                    try
                    {
                        if(!flag)
                            throw new NoSongSelected();
                        JFrame newSong = new JFrame("Song information"); newSong.setLocation(425,175); newSong.setLayout(null);
                        JLabel name = new JLabel("Name: "); name.setBounds(50,50,100,20);
                        JLabel artist = new JLabel("Artist: "); artist.setBounds(50,100,100,20);
                        JLabel album = new JLabel("Album: "); album.setBounds(50,150,100,20);
                        JLabel rating = new JLabel("Rating: "); rating.setBounds(50,200,100,20);
                        JTextField name1 = new JTextField(allSongs.get(indices[0]).getName()); name1.setBounds(150,50,200,20);
                        JTextField artist1 = new JTextField(allSongs.get(indices[0]).getArtist()); artist1.setBounds(150,100,200,20);
                        JTextField album1 = new JTextField(allSongs.get(indices[0]).getAlbum());; album1.setBounds(150,150,200,20);
                        JTextField rating1 = new JTextField(Integer.toString(allSongs.get(indices[0]).getStars())); rating1.setBounds(150,200,200,20);
                        newSong.add(name); newSong.add(name1); newSong.add(artist); newSong.add(artist1);
                        newSong.add(album); newSong.add(album1); newSong.add(rating); newSong.add(rating1);

                        JButton done = new JButton("Done"); done.setBounds(150,275,85,30); newSong.add(done);
                        JButton cancel = new JButton("Cancel"); cancel.setBounds(250,275,85,30); newSong.add(cancel); 
                        done.addActionListener(new ActionListener(){
                                public void actionPerformed(ActionEvent e)
                                {
                                    String aaaa = rating1.getText();
                                    if(aaaa.equals(""))
                                        aaaa = "0";
                                    Song s = new Song(name1.getText(),artist1.getText(),album1.getText(),Integer.parseInt(aaaa));
                                    newSong.dispose(); allSongs.remove(indices[0]); allSongs.add(indices[0],s);
                                    try
                                    {
                                        FileWriter fw = new FileWriter("all.txt");
                                        BufferedWriter bw = new BufferedWriter(fw);
                                        for(int j = 0;j<allSongs.size();j++)
                                            bw.write(allSongs.get(j).writeFile());
                                        bw.close(); fw.close();
                                    }
                                    catch(IOException ee)
                                    {
                                        JOptionPane.showMessageDialog(null,"IOException");
                                    }
                                    f.dispose(); listOfSongs();
                                }
                            });
                        cancel.addActionListener(new ActionListener(){
                                public void actionPerformed(ActionEvent e)
                                {
                                    newSong.dispose();
                                }
                            });
                        newSong.setVisible(true); newSong.setSize(425,375);                        
                    }
                    catch(NoSongSelected ae)
                    {
                        ae.get();
                    }                    
                    catch(NumberFormatException ae)
                    {
                        JOptionPane.showMessageDialog(null,"Rating must be a number");
                    }
                }
            });
        b4.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {
                    f.dispose();
                }
            });
        f.setResizable(false); 
        f.setSize(500,500); f.setVisible(true);
    }

    public void createPlaylist(String name)
    {
        Playlist ap = new Playlist(name);
        JFrame f = new JFrame("Add songs to "+name); 
        f.setLocation(450,100); f.setLayout(new FlowLayout());

        String listall[][] = new String[allSongs.size()][4];
        for(int j = 0;j<allSongs.size();j++)
            listall[j] = allSongs.get(j).display();
        String headers[] = {"Name","Artist","Album","Stars"};
        JTable table = new JTable(new DefaultTableModel(listall, headers) {
                    public boolean isCellEditable(int row, int col) {
                        return false;
                    }
                });
        JScrollPane jsp = new JScrollPane(table); f.add(jsp);
        ListSelectionModel select =  table.getSelectionModel();
        select.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        select.addListSelectionListener(new ListSelectionListener(){
                public void valueChanged(ListSelectionEvent e)
                {
                    if(!(e.getValueIsAdjusting()))
                    {
                        int[] row = table.getSelectedRows();  
                        try
                        {
                            FileWriter fw = new FileWriter(name+".txt",true);
                            BufferedWriter bw = new BufferedWriter(fw);
                            for (int k = 0; k < row.length; k++) 
                            {                           
                                bw.write(allSongs.get(row[k]).writeFile());
                                ap.addSong(allSongs.get(row[k]).getName(),allSongs.get(row[k]).getArtist(),allSongs.get(row[k]).getAlbum(),allSongs.get(row[k]).getStars());
                            }
                            bw.close(); fw.close();                            
                        }
                        catch(IOException ee)
                        {
                            JOptionPane.showMessageDialog(null,"IOException");
                        }
                    }
                }
            });
        Playlist all = new Playlist("All");
        for(int j = 0;j<allSongs.size();j++)
            all.addSong(allSongs.get(j).getName(),allSongs.get(j).getArtist(),allSongs.get(j).getAlbum(),allSongs.get(j).getStars());
        String options[]={"Sort by:","Name","Artist","Album","Stars"};
        JComboBox sort = new JComboBox(options); f.add(sort);
        sort.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {
                    try
                    {
                        if(sort.getSelectedIndex()  ==  0)
                            all.sortByName();
                        else if(sort.getSelectedIndex()  ==  1)
                            all.sortByName();
                        else if(sort.getSelectedIndex()  ==  2)
                            all.sortByArtist();
                        else if(sort.getSelectedIndex()  ==  3)
                            all.sortByAlbum();
                        else if(sort.getSelectedIndex() ==  4)    
                            all.sortByStars();
                        for(int j = 0;j<allSongs.size();j++)
                            allSongs.set(j,all.list.get(j));
                        FileWriter fw = new FileWriter("all.txt");
                        BufferedWriter bw = new BufferedWriter(fw);
                        for(int j = 0;j< allSongs.size();j++)
                            bw.write(all.list.get(j).writeFile());
                        bw.close(); fw.close();
                    }
                    catch(IOException ee)
                    {
                        JOptionPane.showMessageDialog(null,"File not found");
                    }                  
                    f.dispose(); createPlaylist(name);
                }
            });            
        JButton btn = new JButton("Done"); f.add(btn); 
        btn.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {
                    try
                    {
                        FileWriter fw = new FileWriter("Playlists.txt",true);
                        BufferedWriter bw = new BufferedWriter(fw);
                        bw.write(name+"\n"); p.add(ap);
                        bw.close(); fw.close(); f.dispose(); 
                        thisIsIt.dispose(); new Choice();
                    }
                    catch(IOException ee)
                    {
                        JOptionPane.showMessageDialog(null,"File not found");
                    }
                }
            });
        f.setVisible(true); f.setResizable(false); f.setSize(500,500);        
        JOptionPane.showMessageDialog(null,"Click on the songs to add them to your playlist");
    }

    public int[] sort(int a[])
    {
        for(int k=0;k<a.length;k++)
            for(int j=0;j<a.length-1-i;j++)
            {
                if(a[j]>a[j+1])
                {
                    int t=a[j];
                    a[j]=a[j+1];
                    a[j+1]=t;
                }
            }
        return a;
    }

    public void editPlaylist()
    {        
        JFrame f = new JFrame("Edit "+p.get(i).name); 
        f.setLocation(450,100); f.setLayout(new FlowLayout());
        String headers[] = {"Name","Artist","Album","Stars"};
        String aplay[][] = p.get(i).display();
        JTable jtable = new JTable(new DefaultTableModel(aplay, headers) {
                    public boolean isCellEditable(int row, int col) {
                        return false;
                    }
                });
        JScrollPane jsp = new JScrollPane(jtable); 
        f.add(jsp); flag = false;
        ListSelectionModel lsm = jtable.getSelectionModel();
        lsm.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        lsm.addListSelectionListener(new ListSelectionListener(){
                public void valueChanged(ListSelectionEvent e)
                {
                    if(!(e.getValueIsAdjusting()))
                    {
                        flag = true;
                        indices = jtable.getSelectedRows();
                    }
                }
            });  
        jtable.addMouseListener(new MouseAdapter(){
                public void mouseClicked(MouseEvent e)
                {
                    if (e.getClickCount() == 2 && !e.isConsumed()) 
                    {
                        e.consume(); int k = jtable.getSelectedRow();
                        new PlaySong(p.get(i).list.get(k),k,p.get(i).list);
                    }
                }
            });
        JButton b1 = new JButton("Add");
        JButton b2 = new JButton("Remove");
        JButton b3 = new JButton("Shuffle");
        JButton b4 = new JButton("Rename");
        JButton b5 = new JButton("Clear");

        Playlist all = new Playlist("All");
        for(int j = 0;j<allSongs.size();j++)
            all.addSong(allSongs.get(j).getName(),allSongs.get(j).getArtist(),allSongs.get(j).getAlbum(),allSongs.get(j).getStars());
        String options[]={"Sort by:","Name","Artist","Album","Stars"};
        JComboBox sort = new JComboBox(options);
        sort.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {
                    try
                    {
                        if(sort.getSelectedIndex()  ==  0);
                        else if(sort.getSelectedIndex()  ==  1)
                            all.sortByName();
                        else if(sort.getSelectedIndex()  ==  2)
                            all.sortByArtist();
                        else if(sort.getSelectedIndex()  ==  3)
                            all.sortByAlbum();
                        else if(sort.getSelectedIndex() ==  4)    
                            all.sortByStars();
                        for(int j = 0;j<allSongs.size();j++)
                            allSongs.set(j,all.list.get(j));
                        FileWriter fw = new FileWriter("all.txt");
                        BufferedWriter bw = new BufferedWriter(fw);
                        for(int j = 0;j< allSongs.size();j++)
                            bw.write(all.list.get(j).writeFile());
                        bw.close(); fw.close();
                    }
                    catch(IOException ee)
                    {
                        JOptionPane.showMessageDialog(null,"File not found");
                    }                  
                    f.dispose(); editPlaylist();
                }
            });      
        b1.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {
                    String listall[][] = new String[allSongs.size()][4];
                    for(int j = 0;j<allSongs.size();j++)
                        listall[j] = allSongs.get(j).display();
                    String headers[] = {"Name","Artist","Album","Stars"};
                    JFrame fall = new JFrame("All Songs"); 
                    fall.setLocation(450,100); fall.setLayout(new FlowLayout());
                    JTable table3 = new JTable(new DefaultTableModel(listall, headers) {
                                public boolean isCellEditable(int row, int col) {
                                    return false;
                                }
                            });
                    JScrollPane jsp = new JScrollPane(table3); 
                    fall.add(jsp);
                    ListSelectionModel select = table3.getSelectionModel();
                    select.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                    select.addListSelectionListener(new ListSelectionListener(){
                            public void valueChanged(ListSelectionEvent e)
                            {
                                if(!(e.getValueIsAdjusting()))
                                {
                                    int[] row = table3.getSelectedRows();  
                                    try
                                    {
                                        FileWriter fw = new FileWriter(p.get(i).name+".txt",true);
                                        BufferedWriter bw = new BufferedWriter(fw);
                                        for (int k = 0; k < row.length; k++) 
                                        {  
                                            bw.write(allSongs.get(row[k]).writeFile());
                                            p.get(i).addSong(allSongs.get(row[k]).getName(),allSongs.get(row[k]).getArtist(),allSongs.get(row[k]).getAlbum(),allSongs.get(row[k]).getStars());
                                        }
                                        bw.close(); fw.close();                       
                                    }
                                    catch(IOException ee)
                                    {
                                        JOptionPane.showMessageDialog(null,"IOException");
                                    }
                                }
                            }
                        });
                    JButton btn = new JButton("Done");
                    btn.addActionListener(new ActionListener(){
                            public void actionPerformed(ActionEvent e)
                            {
                                f.dispose(); fall.dispose(); editPlaylist();
                            }
                        });                                 
                    fall.add(btn); fall.setResizable(false); 
                    fall.setSize(500,500); fall.setVisible(true);               
                    JOptionPane.showMessageDialog(null,"Click on the songs to add them to your playlist");
                }
            });
        b2.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {
                    try
                    {
                        if(!flag)
                            throw new NoSongSelected(); 
                        if(indices.length > 1)
                            indices = sort(indices);
                        for(int j = 0; j<indices.length; j++)
                        {                           
                            if(p.get(i).list.get(indices[j]-j).getName().equals(PlaySong.prev.s.getName()))
                            { 
                                PlaySong.prev.nowplaying.dispose();
                                if(PlaySong.media.getStatus() == MediaPlayer.Status.PLAYING)
                                {
                                    PlaySong.media.stop();
                                    PlaySong.alreadyPlaying = false;
                                }
                            }
                            p.get(i).list.remove(indices[j]-j);
                        }
                        FileWriter fw = new FileWriter(p.get(i).name+".txt");
                        BufferedWriter bw = new BufferedWriter(fw);
                        for(int j = 0;j< p.get(i).list.size();j++)
                            bw.write( p.get(i).list.get(j).writeFile());
                        bw.close(); fw.close(); 
                        f.dispose(); editPlaylist();
                    }

                    catch(NoSongSelected ae){ ae.get();}
                    catch(IOException ee)
                    {
                        JOptionPane.showMessageDialog(null,"IOException");
                    }
                }

            });
        b3.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {
                    p.get(i).shuffle();
                    try
                    {
                        FileWriter fw = new FileWriter(p.get(i).name+".txt");
                        BufferedWriter bw = new BufferedWriter(fw);
                        for(int j = 0;j< p.get(i).list.size();j++)
                            bw.write( p.get(i).list.get(j).writeFile());
                        bw.close(); fw.close(); f.dispose(); editPlaylist();
                    }
                    catch(IOException ee) 
                    {
                        JOptionPane.showMessageDialog(null,"IOException");
                    }
                }
            });
        b4.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {
                    try
                    {
                        String n = JOptionPane.showInputDialog("Enter new name: ");
                        while(n == null || search(n))
                        {
                            try
                            {
                                if(search(n))
                                    throw new SameName();
                            }
                            catch(SameName ae)
                            {
                                ae.get();
                            }
                            n = JOptionPane.showInputDialog("Enter new name:");
                        }
                        File oldFile = new File(p.get(i).name+".txt"); 
                        p.get(i).rename(n);
                        File newFile = new File(n+".txt"); 
                        oldFile.renameTo(newFile);      
                        FileWriter fw = new FileWriter("Playlists.txt");
                        BufferedWriter bw = new BufferedWriter(fw);
                        for(int j = 0;j<p.size();j++)
                            bw.write(p.get(j).name+"\n");
                        bw.close(); fw.close(); f.dispose(); 
                        thisIsIt.dispose(); 
                        new Choice(); editPlaylist();
                    }
                    catch(IOException ee)
                    {
                        JOptionPane.showMessageDialog(null,"IOException");
                    }
                }
            });
        b5.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {
                    p.get(i).clear(); f.dispose();
                    editPlaylist();
                }
            });
        f.add( b1); f.add( b2); f.add( b3); 
        f.add(sort); f.add( b4); f.add( b5);
        f.setResizable(false); 
        f.setSize(500,520); f.setVisible(true);
    }

    public boolean search(String n)
    {
        for(int j = 0;j<p.size();j++)
            if(n.equals(p.get(j).name))
                return true;
        return false;
    }

    public void actionPerformed(ActionEvent e)
    {
        if("create".equals(e.getActionCommand()))
        {
            String n = JOptionPane.showInputDialog("Enter name of the playlist:");
            while(n != null && (n.equals("") || search(n)))
            {
                try
                {
                    if(n != null && search(n))
                        throw new SameName();
                }
                catch(SameName ae)
                {
                    ae.get();
                }
                n = JOptionPane.showInputDialog("Enter name of the playlist:");
            }
            if(n != null)
                createPlaylist(n);
        }
        else if("edit".equals(e.getActionCommand()))
        {   
            try
            {
                if(i == -1)
                    throw new NoPlaylistSelected(); 
                editPlaylist();
            }
            catch(NoPlaylistSelected ae)
            {
                ae.get();
            }
        }
        else if("list".equals(e.getActionCommand()))
            listOfSongs();
        else if("delete".equals(e.getActionCommand()))
        {
            try
            {
                if(i == -1)
                    throw new NoPlaylistSelected(); 
                File file = new File(p.get(i).name+".txt"); 
                file.delete(); p.remove(i);
                FileWriter fw = new FileWriter("Playlists.txt");
                BufferedWriter bw = new BufferedWriter(fw);
                for(int j = 0;j<p.size();j++)
                    bw.write(p.get(j).name+"\n");
                bw.close(); fw.close(); 
                thisIsIt.dispose(); new Choice();
            }
            catch(IOException ee) {;}
            catch(NoPlaylistSelected ae)
            {
                ae.get();
            }
        }
    }

    public static void main(String args[])
    {
        new Choice();
    }
}

