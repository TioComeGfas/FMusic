package frodo.fmusic.Interfaces;

public interface ListenersMusicService {

    void setPlaying(final int id);

    void setShuffle(final int id);

    void setRepeat(final int id);

    void play();

    void start();

    void pause();

    void next();

    void previous();

    int getDuration();

    int getRandom();

    int getPosition();

    int setSeek(final int seg);

    void setVolume(final float vol);

}
