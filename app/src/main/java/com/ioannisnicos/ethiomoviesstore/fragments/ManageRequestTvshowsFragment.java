package com.ioannisnicos.ethiomoviesstore.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ioannisnicos.ethiomoviesstore.R;
import com.ioannisnicos.ethiomoviesstore.adapter.ManageRequestRecyclerAdapter;
import com.ioannisnicos.ethiomoviesstore.adapter.ManageTvshowRequestRecyclerAdapter;
import com.ioannisnicos.ethiomoviesstore.retrofit.ApiClient;
import com.ioannisnicos.ethiomoviesstore.retrofit_movie_response.ManageMovieRequestResponse;
import com.ioannisnicos.ethiomoviesstore.retrofit_movie_response.ManageMovieRequestResponsePaging;
import com.ioannisnicos.ethiomoviesstore.retrofit_movie_response.QuerryResponse;
import com.ioannisnicos.ethiomoviesstore.retrofit_tvshow_response.ManageTvshowRequestResponse;
import com.ioannisnicos.ethiomoviesstore.retrofit_tvshow_response.ManageTvshowRequestResponsePaging;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ManageRequestTvshowsFragment extends Fragment {

    private final static String TAG = "ManageRequestMovies_Fragment";

    private boolean pagesOver=          false;
    private int     presentPage=        1;
    private boolean loading=            true;
    private int     previousTotal=      0;
    private int     visibleThreshold=   5;
    private String  mStatus=            "pending";

    //Recycler view
    private List<ManageTvshowRequestResponse>            mManageRequestList;
    private RecyclerView                                mManageRequestRecyclerView;
    private ManageTvshowRequestRecyclerAdapter                mManageRequestAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean isSwipeOn = false;


    //Retrofit
    private Call<ManageTvshowRequestResponsePaging>      mManageRequestMoviesCall;

    private View view;
    private ProgressBar mProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_manage_request_movies, container, false);
        mProgressBar = view.findViewById(R.id.progressBar_manage_request_fragment);



        getActivity().setTitle("Action Pending Movies");
        intAdapter();

        loadRequestMovies();

        FloatingActionButton filterResult = view.findViewById(R.id.floatingactionbutton_manage_request_filter);
        filterResult.setOnClickListener(view1 -> {
            //showAlertDialog();
            PopupMenu popup = new PopupMenu(getContext(), view1);
            popup.setGravity(Gravity.RIGHT);
            popup.getMenuInflater().inflate(R.menu.manage_media_fillter_menu, popup.getMenu());
            if (mStatus.equals("pending")) popup.getMenu().findItem(R.id.pending).setVisible(false);
            if (mStatus.equals("ready")) popup.getMenu().findItem(R.id.accept).setVisible(false);
            if (mStatus.equals("rejected")) popup.getMenu().findItem(R.id.reject).setVisible(false);
            popup.show();
            popup.setOnMenuItemClickListener(item -> onOptionsItemSelected(item));
        });

        return view;
    }

    @Override
    public void onResume() {
        getActivity().setTitle("Manage Movies Request");
        super.onResume();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        FrameLayout.LayoutParams  lllp=(FrameLayout.LayoutParams)mProgressBar.getLayoutParams();

        switch (item.getItemId()){

            case R.id.accept:
                mStatus="ready";
                getActivity().setTitle("Accepted Movies");
                pagesOver = false;
                presentPage = 1;
                previousTotal = 0;
                mManageRequestList.clear();
                mManageRequestAdapter.notifyDataSetChanged();
                lllp.gravity=Gravity.CENTER|Gravity.CENTER_HORIZONTAL;
                mProgressBar.setVisibility(View.VISIBLE);
                loadRequestMovies();
                break;
            case R.id.reject:
                mStatus="rejected";
                getActivity().setTitle("Rejected Movies");
                pagesOver = false;
                presentPage = 1;
                previousTotal = 0;
                mManageRequestList.clear();
                mManageRequestAdapter.notifyDataSetChanged();
                lllp.gravity=Gravity.CENTER|Gravity.CENTER_HORIZONTAL;
                mProgressBar.setVisibility(View.VISIBLE);
                loadRequestMovies();
                break;
             case R.id.pending:
                 mStatus="pending";
                 getActivity().setTitle("Action Pending Movies");
                 pagesOver = false;
                 presentPage = 1;
                 previousTotal = 0;
                 mManageRequestList.clear();
                 mManageRequestAdapter.notifyDataSetChanged();
                 lllp.gravity=Gravity.CENTER|Gravity.CENTER_HORIZONTAL;
                 mProgressBar.setVisibility(View.VISIBLE);
                 loadRequestMovies();
                break;
            default:
        }
        return true;
    };


    private void intAdapter() {

        mManageRequestList = new ArrayList<ManageTvshowRequestResponse>();

        mManageRequestRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_manage_request_fragment);
        mManageRequestAdapter = new ManageTvshowRequestRecyclerAdapter(getContext(), mManageRequestList);
        mManageRequestRecyclerView.setAdapter(mManageRequestAdapter);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mManageRequestRecyclerView.setLayoutManager(linearLayoutManager);
        mManageRequestRecyclerView.setHasFixedSize(true);

        mManageRequestAdapter.setOnSubscribeButtonClickListener(new ManageTvshowRequestRecyclerAdapter.OnSubscribeButtonClickListener() {
            @Override
            public void onSubscribeAcceptButtonClick(int position,String msg) {
                mangRequestMovie(mManageRequestList.get(position).getMovies().getImdb_id(),
                        mManageRequestList.get(position).getUsers().getGoogle_id(),
                        mManageRequestList.get(position).getStores().getId(),"ready",position,msg);
            }

            @Override
            public void onSubscribeRejectButtonClick(int position,String msg) {
                mangRequestMovie(mManageRequestList.get(position).getMovies().getImdb_id(),
                        mManageRequestList.get(position).getUsers().getGoogle_id(),
                        mManageRequestList.get(position).getStores().getId(),"rejected",position,msg);

            }
            @Override
            public void onSubscribeDelButtonClick(int position) {
                RequestMovieDel(mManageRequestList.get(position).getMovies().getImdb_id(),
                        mManageRequestList.get(position).getUsers().getGoogle_id(),
                        mManageRequestList.get(position).getStores().getId(),position);
            }
        });

        // SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh_manage_request_fragment);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pagesOver = false;
                presentPage = 1;
                previousTotal = 0;
                isSwipeOn = true;

                loadRequestMovies();            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);


        mManageRequestRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                                           @Override
                                                           public void onScrolled(RecyclerView recyclerView, int dx, int dy){

                                                               int visibleItemCount = linearLayoutManager.getChildCount();
                                                               int totalItemCount = linearLayoutManager.getItemCount();
                                                               int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                                                               if (loading) {
                                                                   if (totalItemCount > previousTotal) {
                                                                       loading = false;
                                                                       previousTotal = totalItemCount;
                                                                   }
                                                               }
                                                               if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold) && firstVisibleItem>0) {
                                                                   FrameLayout.LayoutParams  lllp=(FrameLayout.LayoutParams)mProgressBar.getLayoutParams();
                                                                   lllp.gravity=Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL;
                                                                   mProgressBar.setLayoutParams(lllp);

                                                                   mProgressBar.setVisibility(View.VISIBLE);

                                                                   loadRequestMovies();
                                                                   loading = true;
                                                               }

                                                           }
                                                       }
        );
    }


    private void loadRequestMovies() {
        if (pagesOver){
            mProgressBar.setVisibility(View.GONE);
            return;}

        GoogleSignInAccount mMyGoogleAccountInfo = GoogleSignIn.getLastSignedInAccount(getContext());

        mManageRequestMoviesCall = ApiClient.getRetrofitApi().getRequestTvshows(1,mMyGoogleAccountInfo.getId(),mStatus);

        mManageRequestMoviesCall.enqueue(new Callback<ManageTvshowRequestResponsePaging>() {
            @Override
            public void onResponse(Call<ManageTvshowRequestResponsePaging> call, Response<ManageTvshowRequestResponsePaging> response) {
                if (!response.isSuccessful()) {
                    // mMoviesCall = call.clone();
                    //mMoviesCall.enqueue(this);
                    return;
                }

                if(presentPage == 1) mManageRequestList.clear();


                if(isSwipeOn){
                    mManageRequestList.clear();
                    mManageRequestAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
                    isSwipeOn=false;
                }

                mProgressBar.setVisibility(View.GONE);
                if (response.body() == null) return;
                if (response.body().getResult_data() == null) return;

                mManageRequestList.addAll(response.body().getResult_data());

                mManageRequestAdapter.notifyDataSetChanged();

                if (response.body().getPaging().getPage() == response.body().getPaging().getTotalPages())
                    pagesOver = true;
                else
                    presentPage++;
            }

            @Override
            public void onFailure(Call<ManageTvshowRequestResponsePaging> call, Throwable t) {

            }
        });


    }

    private void mangRequestMovie(String movieId,String userGId,int storeId,String status,int position,String msg){

        Call<QuerryResponse>  mRequestMovieMoviesCall = ApiClient.getRetrofitApi().mangRequestTvshowRent
                (movieId,userGId,storeId,status,msg);

        mRequestMovieMoviesCall.enqueue(new Callback<QuerryResponse>() {
            @Override
            public void onResponse(Call<QuerryResponse> call, Response<QuerryResponse> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                //if(mMovieRelatedStores.get(position).getMovie_status()== null )
                mManageRequestList.get(position).getStatus().setReq_status(status);
                if(!mStatus.equals(status)) {
                    mManageRequestList.remove(position);
                    mManageRequestAdapter.notifyItemRemoved(position);
                }else    mManageRequestAdapter.notifyItemChanged(position);
           }

            @Override
            public void onFailure(Call<QuerryResponse>call, Throwable t) {
                Log.d("yo",t.toString());
            }
        });
    }

    private void RequestMovieDel(String movieId,String userGId,int storeId,int position){

        Call<QuerryResponse>  mRequestMovieMoviesCall = ApiClient.getRetrofitApi().cancelTvshowRequestRent(movieId,userGId,storeId);

        mRequestMovieMoviesCall.enqueue(new Callback<QuerryResponse>() {
            @Override
            public void onResponse(Call<QuerryResponse> call, Response<QuerryResponse> response) {
                if (!response.isSuccessful()) {

                }

                mManageRequestList.remove(position);
                mManageRequestAdapter.notifyItemRemoved(position);

            }

            @Override
            public void onFailure(Call<QuerryResponse>call, Throwable t) {
                Log.d("yo",t.toString());
            }
        });

    }

}