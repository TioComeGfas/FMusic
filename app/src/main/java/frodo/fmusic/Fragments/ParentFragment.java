package frodo.fmusic.Fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import frodo.fmusic.Activities.MainActivity;
import frodo.fmusic.Adapters.PagerAdapter;
import frodo.fmusic.R;

public class ParentFragment extends Fragment {

    private TabLayout tabLayout;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private PagerAdapter adapter;

    public ParentFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent, container, false);
        initTabs(view);
        return view;
    }

    public void initTabs(View view){
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.color_biblioteca));
        toolbar.setTitle("Biblioteca");
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Canciones"));
        tabLayout.addTab(tabLayout.newTab().setText("Artistas"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = view.findViewById(R.id.viewPager);
        adapter = new PagerAdapter(getChildFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                viewPager.setCurrentItem(position);
            }
        });
    }
}
