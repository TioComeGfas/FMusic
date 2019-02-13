package frodo.fmusic.Fragments.Children;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import frodo.fmusic.Activities.MainActivity;
import frodo.fmusic.Adapters.SongAdapter;
import frodo.fmusic.Code.Manager;
import frodo.fmusic.Code.MusicService;
import frodo.fmusic.Interfaces.OnItemClicked;
import frodo.fmusic.R;

public class ListSongsFragment extends Fragment implements OnItemClicked {

    private Unbinder unbinder;
    private SongAdapter songAdt;
    @BindView(R.id.recicler_view) FastScrollRecyclerView songView;

    private MusicService musicService;
    private Manager manager;

    public ListSongsFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_songs, container, false);
        unbinder = ButterKnife.bind(this,view);

        initComponent(view);
        return view;
    }

    private void initComponent(View view){

        musicService = ((MainActivity)getActivity()).getMusicSrv();
        manager = ((MainActivity)getActivity()).getManager();

        songView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        songAdt = new SongAdapter();
        songView.setAdapter(songAdt);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.list_divider));
        songView.addItemDecoration(itemDecoration);

        songAdt.setOnClick(ListSongsFragment.this);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //------- LISTENERS --------
    @Override
    public void onItemClick(int position) {
        musicService.setSong(position);
        musicService.play();
    }
}
