package frodo.fmusic.Activities;

import android.Manifest;
import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import frodo.fmusic.Code.MusicService;
import frodo.fmusic.Code.MusicService.MusicBinder;
import frodo.fmusic.Code.NotificationHandler;
import frodo.fmusic.Code.Util;
import frodo.fmusic.Fragments.HomeFragment;
import frodo.fmusic.Fragments.ParentFragment;
import frodo.fmusic.Modelos.Album;
import frodo.fmusic.Modelos.Artist;
import frodo.fmusic.Modelos.PlayList;
import frodo.fmusic.Modelos.Song;
import frodo.fmusic.Code.Manager;
import frodo.fmusic.R;

public class MainActivity extends AppCompatActivity{

    private SharedPreferences prefs;
    public static Manager manager;
    public static MusicService musicSrv;

    private DetallesActivity detallesActivity;

    private PlayList masReproducida = new PlayList(1,"mas Reproducidas");

    private NotificationHandler notificationHandler;
    @BindView(R.id.space) SpaceNavigationView spaceNavigationView;

    private ParentFragment parentFragment;
    private HomeFragment homeFragment;
    private boolean flagExistNotification = false;
    private Intent playIntent;

    private final static int RESPUESTA_PERMISO_READER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.contentFragment,homeFragment).commit();

        verificar(); //verifica si los permisos estan aceptados
        initSpaceBar(); //inicia la barra inferior de navegacion
    }

    // ==============================  Manejo del servicio  ==============================

    private ServiceConnection musicConnection = new ServiceConnection(){
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicBinder binder = (MusicBinder)service;
            musicSrv = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    // ==============================  Metodos previos ==============================

    public void initComponent(){
        manager = new Manager();
        prefs = getSharedPreferences("preferences",Context.MODE_PRIVATE);
        notificationHandler = new NotificationHandler(this);
        manager.loadingData(this);
        manager.ordenarAlfabeticamente();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public ArrayList getList(){
        return manager.getListSong();
    }

    public static MusicService getMusicSrv() {
        return musicSrv;
    }

    public static Manager getManager(){
        return manager;
    }

    // ============================== Control del audio ==============================

    public void songPicked(int index,MusicService musicSrv){
        manager.getSong(index).setCount(1);
        musicSrv.setSong(index);
        musicSrv.play();
    }

    // ==============================  Manejo de los permisos ==============================

    public void verificar(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            initComponent();
        }else {
            explicar();
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},RESPUESTA_PERMISO_READER);
        }
    }

    public void explicar(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "necesito el permiso de lectura para buscar las canciones", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == RESPUESTA_PERMISO_READER){
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                initComponent();
                Toast.makeText(this, "Permiso Concedido", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Permiso Denegado", Toast.LENGTH_SHORT).show();
                System.exit(0);
            }
        }
    }

    // ==============================  Manejo notificacion ==============================

    public void upNotification(String title,String message){
        if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(message)){
            Notification.Builder nb = notificationHandler.createNotification(title,message);
            notificationHandler.getManager().notify(1,nb.build());
        }
    }

    // ==============================  Ciclo vida actividad  ==============================
    @Override
    protected void onPause(){
        super.onPause();
        if(!flagExistNotification){
            String title = manager.getSong(MusicService.songPosn).getName();
            String artista = manager.getSong(MusicService.songPosn).getArtist().getName();
            upNotification(title,artista);
            flagExistNotification = true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent == null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }

        for(int i = 0; i < manager.getSizeList(); i ++){
            Song song = manager.getSong(i);
            try{
                song.setCount(Integer.parseInt(getOnPreferences("count_song"+song.getId())));
            }catch (NumberFormatException e){ }
        }
    }

    @Override
    protected void onDestroy() {
        for(int i = 0; i < manager.getSizeList(); i ++){
            if(manager.getSong(i).getCount() != 0){
                Song song = manager.getSong(i);
                saveOnPreferences( String.valueOf(song.getCount()),song.getId());
            }
        }
        saveStateDetails();
        stopService(playIntent);
        musicSrv = null;
        super.onDestroy();
    }

    // ==============================  Shared Preferences  ==============================
    //id cancion , text , count
    private void saveOnPreferences(String data,long id){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("count_song"+id,data);
        editor.commit();
        editor.apply();
    }

    private String getOnPreferences(String tag){
        return prefs.getString(tag,"0");
    }

    private void removeSharedPreferences(){
        prefs.edit().clear().apply();
    }

    public void saveStateDetails(){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("stateShuffle",MusicService.shuffle);
        editor.putInt("stateRepeat",MusicService.repeat);
        editor.putInt("stateLastSong",MusicService.songPosn);
        editor.commit();
        editor.apply();
    }

    public void getOnPreferencesDetails(){
        MusicService.shuffle = prefs.getInt("stateShuffle",0);
        MusicService.repeat = prefs.getInt("stateRepeat",0);
        if(MusicService.playing == MusicService.PLAYING_OFF) MusicService.songPosn = prefs.getInt("stateLastSong",0);
    }
    // ==============================  Manejo de Space bar  ==============================

    private void initSpaceBar(){
        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_nav_inicio));
        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_nav_canciones));
        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_nav_album));

        spaceNavigationView.showIconOnly();
        spaceNavigationView.setCentreButtonColor(getResources().getColor(R.color.md_pink_A200));
        spaceNavigationView.setActiveSpaceItemColor(getResources().getColor(R.color.color_blanco));

        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                updateFragment(-1);
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                switch (itemIndex){
                    case 0: //inicio
                        updateFragment(0);
                        break;
                    case 1: //biblioteca
                        updateFragment(1);
                        break;
                    case 2: //opciones
                        actionListenerBar(MainActivity.this,R.color.md_red_A700);
                        break;
                }
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                switch (itemIndex){
                    case 0:
                        updateFragment(0);
                        break;
                    case 1:
                        updateFragment(1);
                        break;
                    case 2:
                        actionListenerBar(MainActivity.this,R.color.md_red_A700);
                        break;
                }
            }
        });
    }

    private void actionListenerBar(Context context, int color){
        spaceNavigationView.changeSpaceBackgroundColor(ContextCompat.getColor(context,color));
        Util.setStatusBarColor(ContextCompat.getColor(context,color),getWindow());
        Util.setNavBarColor(ContextCompat.getColor(context,color),getWindow());
    }

    private void updateFragment(int index){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(homeFragment != null)transaction.hide(homeFragment);
        if(parentFragment != null)transaction.hide(parentFragment);

        switch (index){
            case -1:
                spaceNavigationView.changeSpaceBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.color_center_button));
                if(detallesActivity == null)detallesActivity = new DetallesActivity();
                Intent intent = new Intent(this,detallesActivity.getClass());
                startActivity(intent);
                break;
            case 0:
                actionListenerBar(MainActivity.this,R.color.color_inicio);
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.contentFragment,homeFragment);
                } else {
                    transaction.show(homeFragment);
                }
                transaction.commit();
                break;
            case 1:
                actionListenerBar(MainActivity.this,R.color.color_biblioteca);
                if (parentFragment == null) {
                    parentFragment = new ParentFragment();
                    transaction.add(R.id.contentFragment,parentFragment);
                } else {
                    transaction.show(parentFragment);
                }
                transaction.commit();
                break;
        }
    }

    // ==============================  Manejo de PlayList  ==============================
}
