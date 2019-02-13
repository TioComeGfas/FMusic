package frodo.fmusic.Modelos;

import java.util.ArrayList;

public class PlayList {

   private long id;
   private String name;
   private ArrayList<Long> listSong;

    public PlayList(long id, String name) {
        this.id = id;
        this.name = name;
        this.listSong = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void add(long idSong){
        listSong.add(idSong);
    }

    public void remove(Song song){
        listSong.remove(song);
    }

    public void remove(int index){
        listSong.remove(index);
    }

    public boolean isEmpty(){
        return listSong.size() == 0;
    }

    public boolean isExist(long idSong){
        for (int i = 0; i < listSong.size(); i++){
            if(listSong.contains(idSong)){
                return true;
            }
        }
        return false;
    }
}
