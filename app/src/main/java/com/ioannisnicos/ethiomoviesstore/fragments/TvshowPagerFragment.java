package com.ioannisnicos.ethiomoviesstore.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.ioannisnicos.ethiomoviesstore.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;


public class TvshowPagerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_pagger, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ViewPager2 viewPager = view.findViewById(R.id.view_pager_movies);
        viewPager.setAdapter( new FragmentStateAdapter(this) {

            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position) {
                    case 0:
                        return TvshowsFragment.newInstance(-1,"western",null);
                    case 1:
                        return  TvshowsFragment.newInstance(-1,"local",null);
                    case 2:
                        return TvshowsFragment.newInstance(-1,"others",null);
                }
                return null;
            }


            @Override
            public int getItemCount() {
                return 3;
            }

        });


        TabLayout tabLayout = view.findViewById(R.id.tab_view_pager_movies);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    if (position == 0) {
                        tab.setText("Western");
                    } else if (position == 1) {
                        tab.setText("Local");
                    }else if (position == 2) {
                        tab.setText("Others");
                    }else {
                        tab.setText("");
                    }
                }
        ).attach();
    }

}
