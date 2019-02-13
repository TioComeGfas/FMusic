package frodo.fmusic.Code;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import frodo.fmusic.Modelos.Album;
import frodo.fmusic.Modelos.Artist;
import frodo.fmusic.Modelos.Song;

public class Manager {

    private ArrayList<Song> listSong;
    private ArrayList<Artist> listArtist;
    private ArrayList<Album> listAlbum;

    public Manager() {
        listSong = new ArrayList<>();
        listArtist = new ArrayList<>();
        listAlbum  = new ArrayList<>();
    }

    public ArrayList<Song> getListSong() {
        return listSong;
    }

    public void ordenarAlfabeticamente(){
        Collections.sort(listSong, new Comparator<Song>(){
            public int compare(Song a, Song b){
                return a.getName().compareTo(b.getName());
            }
        });
    }

    /**
     * Crear metodoo para cola de reproduccionstea
     */
    public void ordenarAletorio(){

    }

    public Song getSong(int index){
        return listSong.get(index);
    }

    /**
     * Busca y almacena todas las canciones del dispositivo
     * @param context
     */
    public void loadingData(Context context){
        final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        final String[] cursor_cols = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DURATION ,
                MediaStore.Audio.Media.ARTIST_ID};

        final String where = MediaStore.Audio.Media.IS_MUSIC + "=1";
        final Cursor cursor = context.getContentResolver().query(uri, cursor_cols, where, null, null);

        while (cursor.moveToNext()) {
            long thisId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
            String thisArtist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
            String thisAlbum = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
            String thisTitle = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
            Long thisIdAlbum = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
            int thisDuration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
            long thisIdArtist = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

            Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
            Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, thisIdAlbum);

            Album album = new Album(albumArtUri,thisIdAlbum,thisAlbum);
            Artist artist = new Artist(thisArtist,thisIdArtist);
            Song song = new Song(thisId,thisTitle,artist,album,thisDuration);

            listSong.add(song);
            listAlbum.add(album);
            listArtist.add(artist);
        }
    }

    public int getSizeList(){
        return listSong.size();
    }

    public void removeSong(int index){
        listSong.remove(index);
    }

    public void removeSong(Song song){
        listSong.remove(song);
    }

    // ==============================  Envio de informacion a fragments ==============================

    public int getDuration(){
        return listSong.get(MusicService.songPosn).getDuracion();
    }

    public Uri getAlbumArt(){
        return listSong.get(MusicService.songPosn).getAlbum().getUriAlbumArt();
    }

    public String getTitulo(){
        return listSong.get(MusicService.songPosn).getName();
    }

    public String getArtista(){
        return listSong.get(MusicService.songPosn).getArtist().getName();
    }

    public int getTimeCurrent(MusicService musicSrv){
        return musicSrv.getPosition();
    }
}
