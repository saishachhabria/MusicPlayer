package Parts;
import java.util.*;
public class Playlist
{
    public String name;
    public ArrayList<Song> list = new ArrayList<>();
    public Playlist(String name)
    {
        this.name = name;
    }

    public void addSong(String song,String artist,String album,int stars)
    {
        list.add(new Song(song,artist,album,stars));
    }

    public void rename(String to)
    {
        name = to;
    }

    public void clear()
    {
        list.clear();
    }

    public String[][] display()
    {
        int x = 0; 
        String l[][] = new String[list.size()][];
        for(int j=0;j<list.size();j++)
            l[j] = list.get(j).display();   
        return l;
    }

    public void swap(Song song1,Song song2)
    {
        int i1 = list.indexOf(song1); int i2 = list.indexOf(song2);
        Song tsong=song2; list.set(i2,song1); list.set(i1,tsong);
    }

    public void sortByName()
    {
        for(int i = 0; i < list.size(); i++)
            for(int j = 0; j < list.size()-1-i; j++)
                if(list.get(j).getName().compareTo(list.get(j+1).getName()) > 0)
                    swap(list.get(j),list.get(j+1));      
    }

    public void sortByArtist()
    {
        for(int i=0;i<list.size();i++)
            for(int j=0;j<list.size()-1-i;j++)
                if(list.get(j).getArtist().compareTo(list.get(j+1).getArtist()) > 0)
                    swap(list.get(j),list.get(j+1));        
    }

    public void sortByAlbum()
    {
        for(int i = 0; i < list.size(); i++)
            for(int j = 0; j < list.size()-1-i ; j++)
                if(list.get(j).getAlbum().compareTo(list.get(j+1).getAlbum()) > 0)
                    swap(list.get(j),list.get(j+1));               
    }

    public void sortByStars()
    {
        for(int i = 0;i < list.size(); i++)
            for(int j = 0; j < list.size()-1-i; j++)
                if(list.get(j).getStars() < list.get(j+1).getStars())                
                    swap(list.get(j),list.get(j+1));
               
    }

    public void shuffle()
    {
        Random r = new Random(); 
        Set<Integer> rand = new HashSet<>();
        int i = 0; 
        int ind[] = new int[list.size()];
        while(rand.size() < list.size())
            rand.add(r.nextInt(list.size()));
        for(Integer j: rand)
            ind[i++]=j;
        for(int j = 0;j < list.size()-1; j++)
            swap(list.get(ind[j]),list.get(ind[j+1]));
    }
}
