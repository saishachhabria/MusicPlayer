package Parts;
public class Song
{
    private String name,artist,album;
    private int stars;
    public Song(String name,String artist,String album,int stars)
    {
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.stars = stars;
    }

    public int getStars()
    {
        return stars;
    }

    public String getName()
    {
        return name;
    }

    public String getArtist()
    {
        return artist;
    }

    public String getAlbum()
    {
        return album;
    }

    public String[] display()
    {
        String st  =  "";
        for(int i = 0; i < stars; i++)
            st+= "\u2605";
        String n[] = {name,artist,album,st};
        return n;
    }

    public String writeFile()
    {
        return (name+"\t\t\t"+artist+"\t\t\t"+album+"\t\t\t"+stars+"\t\t\t\n");
    }
}
