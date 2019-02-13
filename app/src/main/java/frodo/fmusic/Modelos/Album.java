package frodo.fmusic.Modelos;

import android.net.Uri;

public class Album extends Base{

    private Uri uriAlbumArt;

    public Album(Uri uriAlbumArt, long id, String name) {
        super(id,name,0,0);
        this.uriAlbumArt= uriAlbumArt;
    }

    public Uri getUriAlbumArt() {
        return uriAlbumArt;
    }

    public void setUriAlbumArt(Uri uriAlbumArt) {
        this.uriAlbumArt = uriAlbumArt;
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
}
