package com.ioannisnicos.ethiomoviesstore.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import com.ioannisnicos.ethiomoviesstore.Movie_Detail_Activity;
import com.ioannisnicos.ethiomoviesstore.R;
import com.ioannisnicos.ethiomoviesstore.Tvshow_Detail_Activity;
import com.ioannisnicos.ethiomoviesstore.adapter.RowSmallMoviesRecyclerAdapter;
import com.ioannisnicos.ethiomoviesstore.adapter.RowSmallTvshowsRecyclerAdapter;

import com.ioannisnicos.ethiomoviesstore.models.Tvshow;
import com.ioannisnicos.ethiomoviesstore.retrofit.ApiClient;
import com.ioannisnicos.ethiomoviesstore.retrofit_movie_response.MoviesResponsePaging;
import com.ioannisnicos.ethiomoviesstore.retrofit_movie_response.QuerryResponse;
import com.ioannisnicos.ethiomoviesstore.retrofit_tvshow_response.TvshowsResponsePaging;
import com.ioannisnicos.ethiomoviesstore.utils.ConnectivityBroadcastReceiver;
import com.ioannisnicos.ethiomoviesstore.utils.NetworkConnection;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TvshowsFragment extends Fragment {

    public static final String TVSHOW_STORE_PARAM = "storeParam";
    public static final String TVSHOW_TYPE_PARAM =  "tvshowTypeParam";
    public static final String MOVIE_SEARCH =      "movieSearchParam";

    private View view;
    private ProgressBar mProgressBar;
    private TextView mTextviewNoContent;

    private int      mStoreIDParam   = -1;
    private String mTvshowTypeParam =  "";
    private String   mMovieSearchParam =  "";

    private boolean  pagesOver = false;
    private int      presentPage = 1;
    private boolean  loading = true;
    private int      previousTotal = 0;
    private int      visibleThreshold = 3;

    private Snackbar mConnectivitySnackbar;
    private ConnectivityBroadcastReceiver mConnectivityBroadcastReceiver;
    private boolean isBroadcastReceiverRegistered;
    private boolean isFragmentLoaded;

    private Call<TvshowsResponsePaging> mTvshowsListCall;
    private List<Tvshow> mTvshowsList;

    private RecyclerView mTvshowsRecyclerView;
    private RowSmallTvshowsRecyclerAdapter mTvshowsAdapter;

    private   boolean[] GenrescheckedList = {false, false, false,false, false, false,false, false, false,false, false,
            false,false,false, false,false, false, false,false, false, false,false,
            false, false,false};

    private String[] GenresList = {"comedy","short","drama","history","war","romance","action","western","fantasy",
            "horror","science-fiction","documentary","adventure","family","crime","music",
            "thriller","animation","mystery","musical","holiday","anime","superhero","suspense",
            "tv-movie" };

    private String mGener="all";
    private String mYear="all";
    private String mMyMovies="no";
    int YearFiltercheckedItem = 2;

    private String moviesToAdd="";
    private List<Integer> moviesToAddPosition;
    private String moviesToRemove="";
    private List<Integer> moviesToRemovePosition;

    private TextView movieaddNo;
    private Button addMovie;
    private Button   clearMovie;


    public static TvshowsFragment newInstance(int paramStore, String paramType,String paramSearch){
        TvshowsFragment fragment = new TvshowsFragment();
        Bundle args = new Bundle();
        args.putInt(TVSHOW_STORE_PARAM, paramStore);
        args.putString(TVSHOW_TYPE_PARAM, paramType);
        args.putString(MOVIE_SEARCH, paramSearch);
        fragment.setArguments(args);

        return fragment;
    }

    public TvshowsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStoreIDParam   = getArguments().getInt(TVSHOW_STORE_PARAM,-1);
            mTvshowTypeParam = getArguments().getString(TVSHOW_TYPE_PARAM,"");
            mMovieSearchParam = getArguments().getString(MOVIE_SEARCH,"");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        view = inflater.inflate(R.layout.fragment_movies, container, false);
        mProgressBar = view.findViewById(R.id.progressBar_movies_main_fragment);
        mTextviewNoContent = view.findViewById(R.id.textView_movies_main_fragment);


        GoogleSignInAccount mMyGoogleAccountInfo = GoogleSignIn.getLastSignedInAccount(getContext());
        if(presentPage<=1)  mTvshowsList = new ArrayList<Tvshow>();

        mTvshowsRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_movies_main_fragment);
        mTvshowsAdapter = new RowSmallTvshowsRecyclerAdapter(getContext(), mTvshowsList);
        mTvshowsRecyclerView.setAdapter(mTvshowsAdapter);

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        mTvshowsRecyclerView.setLayoutManager(gridLayoutManager);

        mTvshowsAdapter.setOnBKItemClickListener(new RowSmallTvshowsRecyclerAdapter.OnBKItemClickListener() {
            @Override
            public void onBKItemClick(int position) {
                Intent intent = new Intent(getContext(), Tvshow_Detail_Activity.class);
                intent.putExtra(Tvshow_Detail_Activity.MOVIE_ID, mTvshowsList.get(position).getImdb_id());
                intent.putExtra(Tvshow_Detail_Activity.MOVIE_TITLE, mTvshowsList.get(position).getTitle());
                intent.putExtra(Tvshow_Detail_Activity.ADAPTER_POSITION, position);
                startActivityForResult(intent,  Tvshow_Detail_Activity.POSITION_ACTIVITY_RETURN);
            }
        });
//
        mTvshowsAdapter.setRequestStoreClickListener(new RowSmallTvshowsRecyclerAdapter.RequestTvshowClickListener() {
            @Override
            public void onStoreItemClick(int position) {
//                Bundle args = new Bundle();
//                args.putString(RentTvshowDialog.USER_ID,mMyGoogleAccountInfo.getId());
//                args.putString(RentTvshowDialog.TVSHOW_ID, mTvshowsList.get(position).getImdb_id());
//                args.putString(RentTvshowDialog.TVSHOW_TIILE, mTvshowsList.get(position).getTitle());
//                args.putInt(RentTvshowDialog.STORE_ID, mStoreIDParam);
//                RentTvshowDialog dialog=new RentTvshowDialog();
//

//                dialog.setl(new ChooseMovieRelatedStoreAlert.PopClickedListener() {
//                    @Override
//                    public void itemClick(String username) {
//                        Toast.makeText(getContext(),username, Toast.LENGTH_LONG).show();
//                    }
//                });

//                dialog.setArguments(args);
//                dialog.show(getFragmentManager(),"dialog");
            }
        });

        moviesToAddPosition    = new ArrayList<>();
        moviesToRemovePosition = new ArrayList<>();


        movieaddNo = view.findViewById(R.id.textView_movies_main_fragment_number);
        addMovie = view.findViewById(R.id.button_movies_main_fragment_addmovie);
        addMovie.setOnClickListener(view -> moviesToaddQuery(moviesToAddPosition,moviesToRemovePosition));

        clearMovie = view.findViewById(R.id.button_movies_main_fragment_clear);
        clearMovie.setOnClickListener(view12 -> clearMoviePush());


        mTvshowsAdapter.setRequestStoreClickListener(position -> moviePush(position));

        mTvshowsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){

                int visibleItemCount = gridLayoutManager.getChildCount();
                int totalItemCount = gridLayoutManager.getItemCount();
                int firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    FrameLayout.LayoutParams  lllp=(FrameLayout.LayoutParams)mProgressBar.getLayoutParams();
                    lllp.gravity=Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL;
                    mProgressBar.setLayoutParams(lllp);

                    mProgressBar.setVisibility(View.VISIBLE);



                    loadMovies();
                    loading = true;
                }

            }
        });


        FloatingActionButton filterResult = view.findViewById(R.id.floatingactionbutton_movies_main_fragment_filter);
        filterResult.setOnClickListener(view1 -> {
            //showAlertDialog();
                PopupMenu popup = new PopupMenu(getContext(), view1);
                popup.setGravity(Gravity.RIGHT);
                popup.getMenuInflater().inflate(R.menu.movie_fragmetnt_fillter_menu, popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(item -> onOptionsItemSelected(item));
            });

        if (NetworkConnection.isConnected(getContext())) {
            isFragmentLoaded = true;
            loadMovies();
        }
            return view;
        }




    private void clearMoviePush(){
        if(moviesToAddPosition !=null){
            for (int p: moviesToAddPosition) {
                mTvshowsList.get(p).setAvailable("no");
                mTvshowsAdapter.notifyItemChanged(p);
            }
            moviesToAdd="";
            moviesToAddPosition.clear();

            addMovie.setVisibility(View.GONE);
            clearMovie.setVisibility(View.GONE);
            movieaddNo.setVisibility(View.GONE);
        }

        if(moviesToRemovePosition !=null){
            for (int p: moviesToRemovePosition) {
                mTvshowsList.get(p).setAvailable("yes");
                mTvshowsAdapter.notifyItemChanged(p);
            }
            moviesToRemove="";
            moviesToRemovePosition.clear();

            addMovie.setVisibility(View.GONE);
            clearMovie.setVisibility(View.GONE);
            movieaddNo.setVisibility(View.GONE);
        }
    }

    private void moviePush(int position){

        if(mTvshowsList.get(position).getAvailable().equals("no")) {

            mTvshowsList.get(position).setAvailable("yes");
            mTvshowsAdapter.notifyItemChanged(position);

            if(moviesToRemovePosition !=null){
                for(int i=0; i<moviesToRemovePosition.size() ;i++){
                    if(moviesToRemovePosition.get(i)==position)  { moviesToRemovePosition.remove(i); return;}
                } }

            addMovie.setVisibility(View.VISIBLE);
            clearMovie.setVisibility(View.VISIBLE);
            movieaddNo.setVisibility(View.VISIBLE);
            moviesToAddPosition.add(position);
            moviesToaddString(moviesToAddPosition);
            movieaddNo.setText(String.valueOf(moviesToAddPosition.size()));
        }
        else {

            mTvshowsList.get(position).setAvailable("no");
            mTvshowsAdapter.notifyItemChanged(position);

            if(moviesToAddPosition !=null){
                for(int i=0; i<moviesToAddPosition.size() ;i++){
                    if(moviesToAddPosition.get(i)==position)  { moviesToAddPosition.remove(i); return;}
                } }

            addMovie.setVisibility(View.VISIBLE);
            clearMovie.setVisibility(View.VISIBLE);
            movieaddNo.setVisibility(View.VISIBLE);
            moviesToRemovePosition.add(position);
            moviesToaddString(moviesToRemovePosition);


            movieaddNo.setText(String.valueOf(moviesToRemovePosition.size()));
        }
    }

    private void moviesToaddString(@NotNull List<Integer> s){
        StringBuilder sb = new StringBuilder();
        for (int p: s) {
            sb.append(mTvshowsList.get(p).getImdb_id()+ ",");
        }

        Toast.makeText(getContext(), sb, Toast.LENGTH_SHORT).show();

    }

    private void moviesToaddQuery(@NotNull List<Integer> s,@NotNull List<Integer> v){
        StringBuilder sb_add = new StringBuilder();
        for (int p: s) {
            sb_add.append("'"+mTvshowsList.get(p).getImdb_id()+ "',");
        }

        StringBuilder sb_del = new StringBuilder();
        for (int p: v) {
            sb_del.append("'"+mTvshowsList.get(p).getImdb_id()+ "',");
        }
        mProgressBar.setVisibility(View.VISIBLE);
        addTvshows(sb_add,sb_del);
    }






    @Override
    public void onStart() {
        super.onStart();
        mTvshowsAdapter.notifyDataSetChanged();
      }

    @Override
    public void onResume() {
        super.onResume();

        if (!isFragmentLoaded && !NetworkConnection.isConnected(getContext())) {
            mConnectivitySnackbar = Snackbar.make(getActivity().findViewById(R.id.main_activity_fragment_container),"No Network", Snackbar.LENGTH_INDEFINITE);
            mConnectivitySnackbar.show();
            mConnectivityBroadcastReceiver = new ConnectivityBroadcastReceiver(new ConnectivityBroadcastReceiver.ConnectivityReceiverListener() {
                @Override
                public void onNetworkConnectionConnected() {
                    mConnectivitySnackbar.dismiss();
                    isFragmentLoaded = true;
                    loadMovies();
                    isBroadcastReceiverRegistered = false;
                    getActivity().unregisterReceiver(mConnectivityBroadcastReceiver);
                }
            });
            IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
            isBroadcastReceiverRegistered = true;
            getActivity().registerReceiver(mConnectivityBroadcastReceiver, intentFilter);
        } else if (!isFragmentLoaded && NetworkConnection.isConnected(getContext())) {
            isFragmentLoaded = true;
            loadMovies();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.gener_fillter:
                clearMoviePush();
                generFilterAlertDialog();
                break;
            case R.id.year_filler:
                clearMoviePush();
                yearFilterAlertDialog();
                break;
            case R.id.myMovie_filler:
                clearMoviePush();
                mMyMovies="yes";
                pagesOver = false;
                presentPage = 1;
                previousTotal = 0;
                mTvshowsList.clear();
                mTvshowsAdapter.notifyDataSetChanged();
                loadMovies();
                break;
            case R.id.allMovie_filler:
                clearMoviePush();
                mMyMovies="no";
                pagesOver = false;
                presentPage = 1;
                previousTotal = 0;
                mTvshowsList.clear();
                mTvshowsAdapter.notifyDataSetChanged();
                loadMovies();
                break;
            default:
        }
        return true;
    };



    private void loadMovies() {
        if (pagesOver){
            mProgressBar.setVisibility(View.GONE);
            return;}
        GoogleSignInAccount mMyGoogleAccountInfo = GoogleSignIn.getLastSignedInAccount(getContext());

//        if(!mMovieSearchParam.isEmpty()) mTvshowsListCall = ApiClient.getRetrofitApi().getSearchTvshows(presentPage,mMovieSearchParam,mTvshowTypeParam,mGener,mYear,null);
//        else if(mStoreIDParam!=-1)       mTvshowsListCall = ApiClient.getRetrofitApi().getStoreTvshows(presentPage,mStoreIDParam,mTvshowTypeParam,mGener,mYear,null);
//        else

       if(!mMovieSearchParam.isEmpty()) mTvshowsListCall = ApiClient.getRetrofitApi().getSearchTvshows(mMyGoogleAccountInfo.getId(),presentPage,mMovieSearchParam,mTvshowTypeParam,mGener,mYear,null);
            else
                mTvshowsListCall = ApiClient.getRetrofitApi().getTvshowsList(mMyGoogleAccountInfo.getId(),presentPage,mTvshowTypeParam,mGener,mYear,null,mMyMovies);

        mTvshowsListCall.enqueue(new Callback<TvshowsResponsePaging>() {
            @Override
            public void onResponse(Call<TvshowsResponsePaging> call, Response<TvshowsResponsePaging> response) {
                if (!response.isSuccessful()) {
                    mTvshowsListCall = call.clone();
                    mTvshowsListCall.enqueue(this);
                    return;
                }

                mProgressBar.setVisibility(View.GONE);
                if(response.code()==204 && presentPage==1) {
                    mTextviewNoContent.setVisibility(View.VISIBLE);
                    return;
                }

                if (pagesOver){
                    mProgressBar.setVisibility(View.GONE);
                    return;}

                if (response.body() == null) return;
                if (response.body().getMessage() != null) return;
                if (response.body().getResults() == null) return;

                for (Tvshow movieBrief : response.body().getResults()) {
                    if (movieBrief != null && movieBrief.getPoster() != null)
                         mTvshowsList.add(movieBrief);
                }

                //mMoviesList.addAll(response.body().getResults());
                mTvshowsAdapter.notifyDataSetChanged();

                if (response.body().getPaging().getPage() == response.body().getPaging().getTotalPages())
                    pagesOver = true;
                else
                    presentPage++;
            }

            @Override
            public void onFailure(Call<TvshowsResponsePaging> call, Throwable t) {
                //Toast.makeText(g, "Unable to connect with server", Toast.LENGTH_LONG).show();
                //Log.d(TAG, t.toString());

            }
        });


    }

    private void addTvshows(StringBuilder addQueryData,StringBuilder delQueryData){
        GoogleSignInAccount mMyGoogleAccountInfo = GoogleSignIn.getLastSignedInAccount(getContext());
        Call<QuerryResponse> addMoviesCall = ApiClient.getRetrofitApi().saveChangeMyTvshowsPost(mMyGoogleAccountInfo.getId(),addQueryData,delQueryData);
        addMoviesCall.enqueue(new Callback<QuerryResponse>() {
            @Override
            public void onResponse(Call<QuerryResponse> call, Response<QuerryResponse> response) {

                if (!response.isSuccessful()) {
                    return;
                }

                if(response.code()==200){
                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(),"Code: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    moviesToAddPosition.clear();
                    moviesToRemovePosition.clear();
                    addMovie.setVisibility(View.GONE);
                    clearMovie.setVisibility(View.GONE);
                    movieaddNo.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<QuerryResponse> call, Throwable t) {
                Log.d("TAG",t.toString());
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



//        if (requestCode == Movie_Detail_Activity.POSITION_ACTIVITY_RETURN) {
//            if (resultCode == Activity.RESULT_OK) {
//                int result = data.getIntExtra(Movie_Detail_Activity.ADAPTER_POSITION, 1);
//                mTvshowsAdapter.notifyItemChanged(result);
//
//            }
//            if (resultCode == Activity.RESULT_CANCELED) {
//                //Write your code if there's no result
//            }
//        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mTvshowsListCall != null)    mTvshowsListCall.cancel();
    }


    private void generFilterAlertDialog() {

        MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(getContext());
        alertDialog.setTitle("Gener");

        alertDialog.setMultiChoiceItems(GenresList, GenrescheckedList, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {

            }

        });

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!NetworkConnection.isConnected(getContext())) {
                    Toast.makeText(getContext(), "NO Network", Toast.LENGTH_SHORT).show();
                    return;
                }
                String gener ="";

                for (int i =0 ;GenrescheckedList.length>i;i++){
                    if(GenrescheckedList[i]) gener +="'" +(GenresList[i])+"',";
                }
                if (gener.endsWith(",")) gener = gener.substring(0, gener.length()-1);

                Toast.makeText(getContext(), gener , Toast.LENGTH_LONG).show();

                mGener=gener;
                pagesOver = false;
                presentPage = 1;
                previousTotal = 0;
                mTvshowsList.clear();
                mTvshowsAdapter.notifyDataSetChanged();
                loadMovies();
            }
        });
        alertDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();
    }

    private void yearFilterAlertDialog() {
        MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(getContext());
        alertDialog.setTitle("Year");

        String[] items = {"2022","all","2021","2020","2019","2018","2017","2018","2017","2018","2016","2015","2014","2013","2012","2000","2000"
                ,"2000","2000","2000","2000","2000","2000","2000","2000","2000","2000","2000","2000","2000","2000","2000","2000"
                ,"2000","2000","2000","2000","2000","2000","2000","2000","2000","2000","2000","2000","2000","2000"};

        alertDialog.setSingleChoiceItems(items, YearFiltercheckedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!NetworkConnection.isConnected(getContext())) {
                    Toast.makeText(getContext(), "NO Network", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getContext(), items[i] , Toast.LENGTH_SHORT).show();
                mYear=items[i];
                YearFiltercheckedItem = i;
                dialogInterface.dismiss();
                pagesOver = false;
                presentPage = 1;
                previousTotal = 0;
                mTvshowsList.clear();
                mTvshowsAdapter.notifyDataSetChanged();
                loadMovies();

            }
        });


        alertDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alertDialog.show();
    }




}