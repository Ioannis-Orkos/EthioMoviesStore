package com.ioannisnicos.ethiomoviesstore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.ioannisnicos.ethiomoviesstore.fragments.MoviesFragment;
import com.ioannisnicos.ethiomoviesstore.fragments.TvshowsFragment;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class SearchActivity extends AppCompatActivity {

        //ToolBar
        private Toolbar mToolbar;

        public static final String SEARCH = "search";


    // TODO: Rename and change types and number of parameters
    public static Intent newInstance(Context c, String search) {
        Intent intent = new Intent((Activity) c, SearchActivity.class);
        intent.putExtra(SEARCH,search);

        return intent;
    }


    private String mSearch="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent receivedIntent = getIntent();
        mSearch = receivedIntent.getStringExtra(SearchActivity.SEARCH);

        if(mSearch.isEmpty() || mSearch==null) finish();

        mToolbar = findViewById(R.id.toolbar_search);
        setSupportActionBar(mToolbar);
        setTitle("Search result for: " +mSearch);


        SearchActivity.StoreMediaPagerAdapter sectionsPagerAdapter = new SearchActivity.StoreMediaPagerAdapter(this,mSearch);
        ViewPager2 viewPager = findViewById(R.id.view_pager_search);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tab_view_search);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    if (position == 0) {
                        tab.setText("Movies");
                    } else if (position == 1) {
                        tab.setText("Tvshows");
                    }else {
                        tab.setText("");
                    }
                }
        ).attach();
    }

    public class StoreMediaPagerAdapter extends FragmentStateAdapter {

        private String search;
        public StoreMediaPagerAdapter(SearchActivity fragment, String search) {
            super(fragment);
            this.search = search;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            // Return a NEW fragment instance in createFragment(int)
            switch (position) {
                case 0:
                    return MoviesFragment.newInstance(-1,null,mSearch);
                case 1:
                    return TvshowsFragment.newInstance(-1,null,mSearch);
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}