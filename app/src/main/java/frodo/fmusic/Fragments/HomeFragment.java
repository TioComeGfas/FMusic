package frodo.fmusic.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.expansionpanel.ExpansionLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import frodo.fmusic.Activities.MainActivity;
import frodo.fmusic.Code.Util;
import frodo.fmusic.R;

public class HomeFragment extends Fragment {

    private Unbinder unbinder;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.expansionLayout) ExpansionLayout expansionLayout;

    public HomeFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this,view);
        initTab();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initTab(){
        toolbar.setBackgroundColor(getResources().getColor(R.color.color_inicio));
        Util.setStatusBarColor(getResources().getColor(R.color.color_inicio),((MainActivity)getActivity()).getWindow());
        toolbar.setTitle("Inicio");
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
    }
}
