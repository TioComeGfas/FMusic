package frodo.fmusic.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import frodo.fmusic.R;


public class ParentPlaylistFragment extends Fragment {

    private Unbinder unbinder;
    @BindView(R.id.mostPlaying) Button b_mostPlaying;

    public ParentPlaylistFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent_playlist, container, false);
        unbinder = ButterKnife.bind(this,view);
        return view;
    }

}
