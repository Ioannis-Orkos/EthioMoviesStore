package com.ioannisnicos.ethiomoviesstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.ioannisnicos.ethiomoviesstore.fragments.FavouritePagerFragment;
import com.ioannisnicos.ethiomoviesstore.fragments.ManageMediaRequestPagerFragment;
import com.ioannisnicos.ethiomoviesstore.fragments.ManageRequestMoviesFragment;
import com.ioannisnicos.ethiomoviesstore.fragments.MoviePagerFragment;
import com.ioannisnicos.ethiomoviesstore.fragments.TvshowPagerFragment;
import com.ioannisnicos.ethiomoviesstore.models.Stores;
import com.ioannisnicos.ethiomoviesstore.retrofit.ApiClient;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    //Navigation View
    private DrawerLayout        mDrawer;
    private NavigationView      navigationView;
        private View            navHeader ;
        private static int      navItemIndex = 0;
        private TextView        txtNavHeaderDisp;
        private CircleImageView imgNavHeaderProfile;
        private ImageView       imgNavHeaderBg;

    //ToolBar
    private Toolbar             mToolbar;

    // Google login views
    private GoogleSignInClient  mGoogleSignInClient;
    private GoogleSignInAccount mMyGoogleAccountInfo;


    //load user information from db
    private Stores mStoreUserInfo;
    private Call<Stores> getUserInfoCall;

    //setting activity response code
    private final int REQUEST_CODE = 101;


    private boolean doubleBackToExitPressedOnce;
    Toast mToastDoubleBackToExit          =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle("Ethio ፊልም");

        String notificaton = "";
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            notificaton =  extra.getString("notification");
        }
        mToastDoubleBackToExit = Toast.makeText(this, R.string.press_again_to_exit, Toast.LENGTH_SHORT);

        intNavigation();
        intGoogle();

        if(notificaton.equals("rent")) {
            setTitle("Transaction");
            setFragment(new ManageMediaRequestPagerFragment());

        }

        if (savedInstanceState == null&&!notificaton.equals("rent")) {
             setFragment(new MoviePagerFragment());
             navigationView.setCheckedItem(R.id.nav_movies);
        }

    }


    public void intGoogle(){

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }


    public void intNavigation(){

        mDrawer =           (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView =    (NavigationView) findViewById(R.id.navigation_main_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar,
                                           R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        navHeader =             navigationView.getHeaderView(navItemIndex);
        txtNavHeaderDisp =      (TextView)  navHeader.findViewById(R.id.textview_disname_nav_header);
        imgNavHeaderBg =        (ImageView) navHeader.findViewById(R.id.img_bg_disname_nav_header);
        imgNavHeaderProfile =   (CircleImageView) navHeader.findViewById(R.id.img_profile_disname_nav_header);


        mMyGoogleAccountInfo = GoogleSignIn.getLastSignedInAccount(MainActivity.this);
        if (mMyGoogleAccountInfo != null) {
            String personName =       mMyGoogleAccountInfo.getDisplayName();
            String personGivenName =  mMyGoogleAccountInfo.getGivenName();
            String personFamilyName = mMyGoogleAccountInfo.getFamilyName();
            String personEmail =      mMyGoogleAccountInfo.getEmail();
            String personId =         mMyGoogleAccountInfo.getId();
            Uri personPhoto =         mMyGoogleAccountInfo.getPhotoUrl();

            txtNavHeaderDisp.setText(personName);
            // Loading profile image
            Glide.with(this).load(personPhoto).into(imgNavHeaderProfile);

        getUserInfo(mMyGoogleAccountInfo.getId());
        }

    }

    private void getUserInfo(String userGID){

        getUserInfoCall = ApiClient.getRetrofitApi().getUserInfo(userGID);
        getUserInfoCall.enqueue(new Callback<Stores>() {
            @Override
            public void onResponse(Call<Stores> call, Response<Stores> response) {

                if (response.code()==204) {
                    Toast.makeText(MainActivity.this,"Unable to get your data on our server please try again later", Toast.LENGTH_LONG).show();
                    signOut();
                    return;
                }

                if (!response.isSuccessful()) {
                    //Toast.makeText(MainActivity.this,"Code: " + response.code(), Toast.LENGTH_LONG).show();
                    getUserInfoCall = call.clone();
                    getUserInfoCall.enqueue(this);
                    return;
                }

                if(response.code()==200){
                    //Toast.makeText(MainActivity.this,"Code: " + response.body().getFirst_name(), Toast.LENGTH_SHORT).show();
                    mStoreUserInfo = response.body();
                    txtNavHeaderDisp.setText(response.body().getDisplay_name());
                    Glide.with(MainActivity.this).load(ApiClient.BASE_URL+response.body().getProf_img()).into(imgNavHeaderProfile);
                    Glide.with(MainActivity.this).load(ApiClient.BASE_URL+response.body().getBanner()).placeholder(R.drawable.image_navigation_view_header_background).into(imgNavHeaderBg);
                }
            }

            @Override
            public void onFailure(Call<Stores> call, Throwable t) {
                Log.d(TAG,t.toString());
                getUserInfoCall = call.clone();
                getUserInfoCall.enqueue(this);
                return;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tab_menu_search, menu);

        final MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setQueryHint("search here");
        //searchView.setQueryHint(Html.fromHtml("<font color = #ffffff>" + "search here"  + "</font>"));
        ImageView searchClose = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        searchClose.setImageResource(R.drawable.ic_arrow_back_white_24);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Toast.makeText(MainActivity.this,"search for: " + query, Toast.LENGTH_LONG).show();
                startActivity(SearchActivity.newInstance(MainActivity.this,query));
                searchMenuItem.collapseActionView();
                return true;

            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nav_movies:
                setTitle("Movies");
                setFragment(new MoviePagerFragment());
                break;

            case R.id.nav_tv_show:
                navigationView.setCheckedItem(R.id.nav_tv_show);
                setFragment(new TvshowPagerFragment());
                setTitle("Tv Shows");
//                getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_fragment_container,
//                new TvshowsFragment()).commit();
                break;

            case R.id.nav_favourite:
                setTitle("Favourites");
                setFragment(new FavouritePagerFragment());
                break;

            case R.id.nav_transaction:
                setTitle("Transaction");
                setFragment(new ManageMediaRequestPagerFragment());
                break;

            case R.id.nav_settings:
                //startActivity(new Intent(this, SettingActivity.class));
                if(mStoreUserInfo != null){
                    Intent i = new Intent(MainActivity.this,SettingActivity.class);
                    i.putExtra("UserInfo", mStoreUserInfo); //I get the error The method putParcelable(String, Parcelable) in the type Bundle is not applicable for the arguments (int, A.myClass)
                    startActivityForResult(i, REQUEST_CODE);
                }
                break;

            case R.id.Managesubscriptions:
               Intent intent = new Intent(MainActivity.this,ManageSubscriptions.class);
               startActivity(intent);
               break;

            case R.id.nav_sign_out:
                signOut();
                //Toast.makeText(this, "Signing Out", Toast.LENGTH_SHORT).show();
                break;
        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .replace(R.id.main_activity_fragment_container, fragment,"helplp");
        fragmentTransaction.commit();

    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete( Task<Void> task) {
                        Toast.makeText(MainActivity.this,"Successfully signed out",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
                });
    }

    @Override
    public void finishAfterTransition() {
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                mToastDoubleBackToExit.cancel();
                super.finishAfterTransition();
                return;
            }
            doubleBackToExitPressedOnce = true;
            mToastDoubleBackToExit.show();
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE ){
            if(resultCode==RESULT_OK){
                Boolean useredited = data.getBooleanExtra("user_edited",false);
                if(useredited) getUserInfo(mMyGoogleAccountInfo.getId());
            }
        }
    }

    @Override
    protected void onDestroy() {
        if(getUserInfoCall !=null)   getUserInfoCall.cancel();
        super.onDestroy();
    }
}