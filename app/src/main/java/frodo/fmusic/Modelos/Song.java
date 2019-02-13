package frodo.fmusic.Modelos;

public class Song extends Base{

    private Artist artist;
    private Album album;
    private int duracion;

    public Song(long id, String title, Artist artist, Album album, int duration) {
        super(id,title,0,duration);
        this.artist = artist;
        this.album = album;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public void setCount(int valor) {
        count+=valor;
    }

    @Override
    public int getDuration() {
        return this.duration;
    }

    @Override
    public void setDuration(int duration) {
        this.duracion = duration;
    }
}

