package com.ioannisnicos.ethiomoviesstore.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ioannisnicos.ethiomoviesstore.R;
import com.ioannisnicos.ethiomoviesstore.adapter.RowSmallMoviesRecyclerAdapter;
import com.ioannisnicos.ethiomoviesstore.database.FavouriteDBHandler;
import com.ioannisnicos.ethiomoviesstore.models.Movies;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class FavouriteMoviesFragment extends Fragment {

    private RecyclerView mMoviesRecyclerView;
    private List<Movies> mMoviesList;
    private RowSmallMoviesRecyclerAdapter mMoviesAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_favourite, container, false);


        mMoviesRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_favmovies);
        mMoviesList = new ArrayList<>();

        mMoviesAdapter = new RowSmallMoviesRecyclerAdapter(getContext(), mMoviesList);
        mMoviesRecyclerView.setAdapter(mMoviesAdapter);


        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        mMoviesRecyclerView.setLayoutManager(gridLayoutManager);

        mMoviesAdapter.setOnFavItemClickListener(new RowSmallMoviesRecyclerAdapter.OnFavouriteMovieClickListener() {
            @Override
            public void onFavouriteItemClick(int position) {
                loadEngMovies();
               // mMoviesAdapter.notifyItemChanged(position);
                mMoviesAdapter.notifyItemRemoved(position);
            }
        });



//        mMoviesAdapter.setOnBKItemClickListener(new RowSmallMoviesRecyclerAdapter.OnBKItemClickListener() {
//            @Override
//            public void onBKItemClick(int position) {
//                Intent intent = new Intent(getContext(), Movie_Detail_Activity.class);
//                intent.putExtra(Movie_Detail_Activity.MOVIE_ID, mMoviesList.get(position).getImdb_id());
//                intent.putExtra(Movie_Detail_Activity.ADAPTER_POSITION, position);
//                startActivityForResult(intent,  Movie_Detail_Activity.POSITION_ACTIVITY_RETURN);
//            }
//        });
//
//        mMoviesAdapter.setRequestStoreClickListener(new RowSmallMoviesRecyclerAdapter.RequestMovieClickListener() {
//            @Override
//            public void onStoreItemClick(int position) {
//                Bundle args = new Bundle();
//                args.putString(ChooseMovieRelatedStoreAlert.USER_ID,"105576209607967447123");
//                args.putString(ChooseMovieRelatedStoreAlert.MOVIE_ID, mMoviesList.get(position).getImdb_id());
//                ChooseMovieRelatedStoreAlert dialog=new ChooseMovieRelatedStoreAlert();
//
//                dialog.setl(new ChooseMovieRelatedStoreAlert.PopClickedListener() {
//                    @Override
//                    public void itemClick(String username) {
//                        Toast.makeText(getContext(),username, Toast.LENGTH_LONG).show();
//                    }
//                });
//
//                dialog.setArguments(args);
//                dialog.show(getFragmentManager(),"dialog");
//            }
//        });
        loadEngMovies();

        return view;
    }

    private void loadEngMovies() {
        mMoviesList.clear();
        List<Movies> moviesList = FavouriteDBHandler.getFavMovieBriefs(getContext());
        for (Movies movieBrief : moviesList) {
            mMoviesList.add(movieBrief);
        }
       // mMoviesAdapter.notifyDataSetChanged();
    }



//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//
//
//        if (requestCode == Movie_Detail_Activity.POSITION_ACTIVITY_RETURN) {
//            if (resultCode == Activity.RESULT_OK) {
//                int result = data.getIntExtra(Movie_Detail_Activity.ADAPTER_POSITION, 1);
//                loadEngMovies();
//
//            }
//            if (resultCode == Activity.RESULT_CANCELED) {
//                //Write your code if there's no result
//            }
//        }
//    }




}