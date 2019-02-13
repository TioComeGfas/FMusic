package frodo.fmusic.Activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ramotion.fluidslider.FluidSlider;
import com.truizlop.fabreveallayout.FABRevealLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import frodo.fmusic.Code.Manager;
import frodo.fmusic.Code.MusicService;
import frodo.fmusic.R;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;

import static com.bumptech.glide.load.DecodeFormat.PREFER_ARGB_8888;

public class DetallesActivity extends Activity {

    private Manager manager;
    private MusicService musicService;

    @BindView(R.id.imageViewCaratula)ImageView iv_caratula;
    @BindView(R.id.fab_reveal_layout) FABRevealLayout fabRevealLayout;
    @BindView(R.id.textTitle) TextView tv_titulo;
    @BindView(R.id.textArtist) TextView tv_artista;
    @BindView(R.id.textTitle2) TextView tv_titulo2;
    @BindView(R.id.cronometro) Chronometer cronometro;
    @BindView(R.id.seekBar) FluidSlider seekBar;
    @BindView(R.id.button_play_pause) ImageView iv_playPause;
    @BindView(R.id.button_aleatorio) ImageView iv_shuffle;
    @BindView(R.id.button_repeat) ImageView iv_repeat;

    private int currentTime;
    private int durationMilis;
    private Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);
        ButterKnife.bind(this);

        initComponent();
    }

    public void initComponent(){
        manager = MainActivity.getManager();
        musicService = MainActivity.getMusicSrv();


        //listener para cada vez que se actualize el cronometro
        cronometro.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                currentTime = manager.getTimeCurrent(musicService);
                Date date = new Date(currentTime);
                SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
                String myTime = formatter.format(date);
                seekBar.setPosition(currentTime);
                seekBar.setStartText(String.valueOf(myTime));
            }
        });


        //listener seekbar
        seekBar.setBeginTrackingListener(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                return Unit.INSTANCE;
            }
        });

        seekBar.setEndTrackingListener(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                musicService.setSeek((int)seekBar.getPosition());
                return Unit.INSTANCE;
            }
        });
        setInfo();
    }
    // ============================== Mostrar info ==============================

    private void imageCircular(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_image_caratula_null);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(),bitmap);
        roundedBitmapDrawable.setCircular(true);
        iv_caratula.setImageDrawable(roundedBitmapDrawable);
    }

    private void setCaratula(){
        Glide.with(this)
                .asBitmap()
                .load( manager.getAlbumArt())
                .apply(new RequestOptions()
                        .error(R.drawable.ic_image_caratula_null)
                        .centerCrop()
                        .format(PREFER_ARGB_8888)
                        .encodeQuality(100))
                .into(iv_caratula);
    }

    private void setTitulo(){
        tv_titulo.setText(manager.getTitulo());
        tv_titulo2.setText(manager.getTitulo());
    }

    private void setArtista(){
        tv_artista.setText(manager.getArtista());
    }

    private void setDuracion(){
        durationMilis = manager.getDuration();
        seekBar.setDuration(durationMilis);

        Date date = new Date(durationMilis);
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        String myTime = formatter.format(date);
        seekBar.setEndText(String.valueOf(myTime));
    }

    public void setInfo(){
        switch (MusicService.playing){
            case MusicService.PLAYING_ON:
                startTimer();
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_media_pause);
                iv_playPause.setImageBitmap(bmp);
                iv_playPause.setColorFilter(R.color.colorPrimary);
                break;
            case MusicService.PLAYING_OFF:
                stopTimer();
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_media_play);
                iv_playPause.setImageBitmap(bmp);
                iv_playPause.setColorFilter(R.color.colorPrimary);
                break;
        }
        setCaratula();
        setTitulo();
        setArtista();
        setDuracion();
    }
    // ==============================  Listeners ==============================

    @OnClick(R.id.button_previous)
    public void listenerBack(){
        musicService.previous();
        setInfo();
        stopTimer();
        startTimer();
    }

    @OnClick(R.id.button_button_next)
    public void listenerNext(){
        musicService.next();
        setInfo();
        stopTimer();
        startTimer();
    }

    @OnClick(R.id.button_play_pause)
    public void listenerPlayPause(){
        switch (MusicService.playing){
            case MusicService.PLAYING_ON:
                stopTimer();
                musicService.pause();
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_media_play);
                iv_playPause.setImageBitmap(bmp);
                iv_playPause.setColorFilter(R.color.color_blanco);
                break;
            case MusicService.PLAYING_OFF:
                startTimer();
                musicService.start();
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_media_pause);
                iv_playPause.setImageBitmap(bmp);
                iv_playPause.setColorFilter(R.color.color_blanco);
                break;
        }
    }

    @OnClick(R.id.button_repeat)
    public void listenerRepeat(){
        Bitmap bmp;
        switch(MusicService.repeat){
            case MusicService.REPEAT_OFF:
                MusicService.repeat = MusicService.REPEAT_ALL;
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_media_repeat_all);
                iv_repeat.setImageBitmap(bmp);
                iv_repeat.setColorFilter(getResources().getColor(R.color.color_blanco));
                break;
            case MusicService.REPEAT_ALL:
                MusicService.repeat = MusicService.REPEAT_ONE;
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_media_repeat_one);
                iv_repeat.setImageBitmap(bmp);
                iv_repeat.setColorFilter(getResources().getColor(R.color.color_blanco));
                break;
            case MusicService.REPEAT_ONE:
                MusicService.repeat = MusicService.REPEAT_OFF;
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_media_repeat_all);
                iv_repeat.setImageBitmap(bmp);
                iv_repeat.setColorFilter(getResources().getColor(R.color.color_negro));
                break;
        }
    }

    @OnClick(R.id.button_aleatorio)
    public void listenerShuffle(){
        switch(MusicService.shuffle){
            case MusicService.SHUFFLE_OFF:
                MusicService.shuffle = MusicService.SHUFFLE_ON;
                iv_shuffle.setColorFilter(getResources().getColor(R.color.color_blanco));
                break;
            case MusicService.SHUFFLE_ON:
                MusicService.shuffle = MusicService.SHUFFLE_OFF;
                iv_shuffle.setColorFilter(getResources().getColor(R.color.color_negro));
                break;
        }
    }

    @OnClick(R.id.buttonClose)
    public void listenerClose(){
        fabRevealLayout.revealMainView();
    }
    // ============================== Timer ==============================

    public void startTimer(){
        cronometro.setBase(SystemClock.elapsedRealtime());
        cronometro.start();
    }

    public void stopTimer(){
        cronometro.stop();
    }

    // ============================== Preferences ==============================
    public void setPreferences(){
        //((MainActivity)getActivity()).getOnPreferencesDetails();
        currentTime = manager.getTimeCurrent(musicService);

        //config de shuffle
        switch(MusicService.shuffle){
            case MusicService.SHUFFLE_OFF:
                iv_shuffle.setColorFilter(getResources().getColor(R.color.color_negro));
                break;
            case MusicService.SHUFFLE_ON:
                iv_shuffle.setColorFilter(getResources().getColor(R.color.color_blanco));
                break;
        }

        //config de repeat
        Bitmap bmp;
        switch(MusicService.repeat){
            case MusicService.REPEAT_OFF:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_media_repeat_all);
                iv_repeat.setImageBitmap(bmp);
                iv_repeat.setColorFilter(getResources().getColor(R.color.color_negro));
                break;
            case MusicService.REPEAT_ALL:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_media_repeat_all);
                iv_repeat.setImageBitmap(bmp);
                iv_repeat.setColorFilter(getResources().getColor(R.color.color_blanco));
                break;
            case MusicService.REPEAT_ONE:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_media_repeat_one);
                iv_repeat.setImageBitmap(bmp);
                iv_repeat.setColorFilter(getResources().getColor(R.color.color_blanco));
                break;
        }

    }
}
