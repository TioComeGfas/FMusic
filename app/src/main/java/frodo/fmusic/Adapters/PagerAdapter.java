package frodo.fmusic.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import frodo.fmusic.Fragments.Children.ArtistFragment;
import frodo.fmusic.Fragments.Children.ListSongsFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private ListSongsFragment fragmentSongList = new ListSongsFragment();
    private ArtistFragment fragmentArtist  = new ArtistFragment();

    private int numberOfTab;

    public PagerAdapter(FragmentManager fm,int numberOfTab) {
        super(fm);
        this.numberOfTab = numberOfTab;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return fragmentSongList;
            case 1:
                return fragmentArtist;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTab;
    }
}
