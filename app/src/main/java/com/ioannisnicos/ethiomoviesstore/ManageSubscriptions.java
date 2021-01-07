package com.ioannisnicos.ethiomoviesstore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ioannisnicos.ethiomoviesstore.adapter.SubscriptionsRecyclerAdapter;
import com.ioannisnicos.ethiomoviesstore.models.Users;
import com.ioannisnicos.ethiomoviesstore.models.UsersAdditionalStatus;
import com.ioannisnicos.ethiomoviesstore.retrofit.ApiClient;
import com.ioannisnicos.ethiomoviesstore.retrofit_movie_response.QuerryResponse;
import com.ioannisnicos.ethiomoviesstore.retrofit_movie_response.UsersSubscriptionResponse;

import java.util.ArrayList;
import java.util.List;

public class ManageSubscriptions extends AppCompatActivity {

    private final static String TAG = ManageSubscriptions.class.getSimpleName();

    private GoogleSignInAccount mMyGoogleAccountInfo = null;

    private String requestStatus = "request";


    private List<UsersAdditionalStatus>     mUserSubsStatus;
    private List<Users>                     mUsersStores;
    private Call<UsersSubscriptionResponse> mSubCall;
    private SubscriptionsRecyclerAdapter    mUserSubsAdapter;
    private RecyclerView                    mUserSubsRecyclerView;
    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managesubscriptions);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_mang_subscriptions);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24);

        setTitle("Manage requested sub.s");
        mMyGoogleAccountInfo = GoogleSignIn.getLastSignedInAccount(this);
        mProgressBar = findViewById(R.id.progressBar_subscriptions_activity);


        FloatingActionButton fab = findViewById(R.id.floating_action_button_add_mang_subscription);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(ManageSubscriptions.this, view);
                popup.getMenuInflater().inflate(R.menu.manage_subscription_fillter_menu, popup.getMenu());
                if (requestStatus.equals("request")) popup.getMenu().findItem(R.id.request).setVisible(false);
                if (requestStatus.equals("subscribed")) popup.getMenu().findItem(R.id.accept).setVisible(false);
                if (requestStatus.equals("rejected")) popup.getMenu().findItem(R.id.reject).setVisible(false);
                popup.show();
                popup.setOnMenuItemClickListener(item -> onOptionsItemSelected(item));
            }
        });

        intUserSubsAdapter();
        LoadNearUserStores();

    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){

            case R.id.request:
                requestStatus="request";
                setTitle("Manage requested sub.s");
                mUsersStores.clear();
                mUserSubsStatus.clear();
                mUserSubsAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.VISIBLE);
                LoadNearUserStores();
                break;
            case R.id.accept:
                requestStatus="subscribed";
                setTitle("Manage accepted sub.s");
                mUsersStores.clear();
                mUserSubsStatus.clear();
                mUserSubsAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.VISIBLE);
                LoadNearUserStores();
                break;
            case R.id.reject:
                requestStatus="rejected";
                setTitle("Manage rejected sub.s");
                mUsersStores.clear();
                mUserSubsStatus.clear();
                mUserSubsAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.VISIBLE);
                LoadNearUserStores();
                break;
            case android.R.id.home:
                    onBackPressed();
                    break;

            default:
        }
        return true;
    };



    private void intUserSubsAdapter() {

        //mUserSubs = new StoresSubscriptionResponse(new ArrayList<>(),new ArrayList<>(),null);
        mUsersStores = new ArrayList<>();
        mUserSubsStatus = new ArrayList<>();

        mUserSubsRecyclerView = findViewById(R.id.recycler_view_user_mang_subscriptions);

        mUserSubsAdapter = new SubscriptionsRecyclerAdapter(this, mUsersStores,mUserSubsStatus);
        mUserSubsRecyclerView.setAdapter(mUserSubsAdapter);
        mUserSubsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUserSubsRecyclerView.setHasFixedSize(true);

        mUserSubsAdapter.setOnSubscribeButtonClickListener(new SubscriptionsRecyclerAdapter.OnSubscribeButtonClickListener() {
            @Override
            public void onSubscribeAcceptButtonClick(int position) {
                Toast.makeText(ManageSubscriptions.this, "accept Out", Toast.LENGTH_SHORT).show();
                manageReqStore(mUserSubsStatus.get(position).getId(),mMyGoogleAccountInfo.getId(),"subscribed",position);
           }

            @Override
            public void onSubscribeRejectButtonClick(int position) {
                Toast.makeText(ManageSubscriptions.this, "reject Out", Toast.LENGTH_SHORT).show();
                manageReqStore(mUserSubsStatus.get(position).getId(),mMyGoogleAccountInfo.getId(),"rejected",position);

            }
        });
    }

    private void LoadNearUserStores() {

        mSubCall = ApiClient.getRetrofitApi().getUserSubss(mMyGoogleAccountInfo.getId(),requestStatus);

        mSubCall.enqueue(new Callback<UsersSubscriptionResponse>() {
            @Override
            public void onResponse(Call<UsersSubscriptionResponse> call, Response<UsersSubscriptionResponse> response) {

                if(response.code() == 404) return;
                if (!response.isSuccessful()) {
                                 return;
                }
                mProgressBar.setVisibility(View.GONE);
                if (!(response.body() == null)) {
                    mUsersStores.addAll(response.body().getStores_list());
                    mUserSubsStatus.addAll(response.body().getStores_list_status());
                    mUserSubsAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<UsersSubscriptionResponse> call, Throwable t) {

                Log.d(TAG, t.toString());
            }
        });
    }

    private void manageReqStore(int userId, String storeGId, String req, int position) {

        Call<QuerryResponse> SubscribeStoreCall = ApiClient.getRetrofitApi().store_manage_request(userId, storeGId,req);

        SubscribeStoreCall.enqueue(new Callback<QuerryResponse>() {
            @Override
            public void onResponse(Call<QuerryResponse> call, Response<QuerryResponse> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                mUserSubsStatus.get(position).setSubscription_status(req);
                if(!req.equals(requestStatus)) {
                    mUsersStores.remove(position);
                    mUserSubsStatus.remove(position);
                    mUserSubsAdapter.notifyItemRemoved(position);
                }else    mUserSubsAdapter.notifyItemChanged(position);



            }

            @Override
            public void onFailure(Call<QuerryResponse> call, Throwable t) {
                Log.d("yo", t.toString());
            }
        });

        return;
    }






}
