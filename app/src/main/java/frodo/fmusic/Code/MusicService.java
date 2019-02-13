package frodo.fmusic.Code;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import frodo.fmusic.Activities.MainActivity;
import frodo.fmusic.Interfaces.ListenersMusicService;
import frodo.fmusic.Modelos.Song;

public class MusicService extends Service implements
        ListenersMusicService,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener{

    //INSTANCIAS
    private MediaPlayer player;
    private Manager manager;
    private final IBinder musicBind = new MusicBinder();

    public static int repeat = 0;
    public static int shuffle = 0;
    public static int playing = 0;

    public static int songPosn;

    public static final int REPEAT_OFF = 0;
    public static final int REPEAT_ONE = 1;
    public static final int REPEAT_ALL = 2;

    public static final int SHUFFLE_OFF = 0;
    public static final int SHUFFLE_ON = 1;

    public static final int PLAYING_ON = 1;
    public static final int PLAYING_OFF = 0;

    public void onCreate(){
        super.onCreate();
        manager = MainActivity.getManager();
        songPosn = 0;
        player = new MediaPlayer();

        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
        initMusicPlayer();
    }

    public void initMusicPlayer(){
        player.setWakeMode(getApplicationContext(),PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent){
        player.stop();
        player.release();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(shuffle == SHUFFLE_ON){
            songPosn = (int) (Math.random()*manager.getSizeList()-1);
        }
        switch(repeat){
            case REPEAT_ONE:
                play();
                break;
            case REPEAT_ALL:
                if(songPosn == manager.getSizeList()-1){
                    songPosn = 0;
                }
                play();
                break;
            case REPEAT_OFF:
                if(songPosn == manager.getSizeList()-1){
                    player.stop();
                }else{
                    songPosn++;
                    play();
                }
                break;
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    public void setSong(int songIndex){
        songPosn=songIndex;
    }

    // ============================== Control del audio ==============================

    @Override
    public void setPlaying(final int id) {
        playing = id;
    }

    @Override
    public void setShuffle(final int id) {
        shuffle = id;
    }

    @Override
    public void setRepeat(final int id) {
        repeat = id;
    }

    @Override
    public void play() {
        player.reset();
        Song song = manager.getSong(songPosn);
        long currSong = song.getId();
        Uri trackUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, currSong);
        try{
            player.setDataSource(getApplicationContext(), trackUri);
            player.prepareAsync();
        }
        catch(Exception e){
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }

        setPlaying(PLAYING_ON);
    }

    @Override
    public void start() {
        try{
            player.start();
        }catch(IllegalStateException ise){

        }
        playing = PLAYING_ON;
    }

    @Override
    public void pause() {
        try{
            player.pause();
        }catch (IllegalStateException ise){
        }
        setPlaying(PLAYING_OFF);
    }

    @Override
    public void next() {
        switch (shuffle){
            case SHUFFLE_ON:
                songPosn = getRandom();
                play();
                break;
            case SHUFFLE_OFF:
                if(songPosn == manager.getSizeList()-1)songPosn = 0;
                else songPosn++;
                play();
                break;
        }
    }

    @Override
    public void previous() {
        switch (shuffle){
            case SHUFFLE_ON:
                songPosn = getRandom();
                play();
                break;
            case SHUFFLE_OFF:
                if(songPosn == 0)songPosn = manager.getSizeList()-1;
                else songPosn--;
                play();
                break;
        }
    }

    @Override
    public int getDuration() {
        int retorno = 0;
        try{
            retorno = player.getDuration();
        }catch(IllegalStateException ise){

        }
        return retorno;
    }

    @Override
    public int getRandom() {
        return (int) (Math.random()*manager.getSizeList()-1);
    }

    @Override
    public int getPosition() {
        try{
            return player.getCurrentPosition();
        }catch (IllegalStateException ise){

        }
        return 0;
    }

    @Override
    public int setSeek(final int seg) {
        try{
             player.seekTo(seg);
        }catch (IllegalStateException ise) {}
        return seg;
    }

    @Override
    public void setVolume(final float vol) {
        try{
            player.setVolume(vol,vol);
        }catch (IllegalStateException ise){ }
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
}
