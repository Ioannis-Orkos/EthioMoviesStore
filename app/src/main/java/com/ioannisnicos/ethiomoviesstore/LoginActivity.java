package com.ioannisnicos.ethiomoviesstore;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.ioannisnicos.ethiomoviesstore.retrofit.ApiClient;
import com.ioannisnicos.ethiomoviesstore.retrofit_movie_response.QuerryResponse;
import com.ioannisnicos.ethiomoviesstore.utils.ConnectivityBroadcastReceiver;
import com.ioannisnicos.ethiomoviesstore.utils.NetworkConnection;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    //Google SignIn
    int     RC_SIGN_IN = 0;
    private GoogleSignInClient  mGoogleSignInClient;
    private GoogleSignInAccount mAccount;

    public String mNotificationToken;

    //Views
    private SignInButton    signInButton;
    private ProgressBar     progressBar;
    private Snackbar        mConnectivitySnackbar;

    private ConnectivityBroadcastReceiver   mConnectivityBroadcastReceiver;
    //private boolean                         isActivityLoaded;
    private boolean                         isBroadcastReceiverRegistered;
    private boolean                         doubleBackToExitPressedOnce;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.view_login_title);


        //Initializing Views
        signInButton = findViewById(R.id.sign_in_button_login);
        progressBar =  findViewById(R.id.progressBar_login);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (NetworkConnection.isConnected(getApplicationContext())) {
                    //isActivityLoaded = true;
                    signIn();
                }
                else{
                    mConnectivitySnackbar = Snackbar.make(view, R.string.no_network, Snackbar.LENGTH_INDEFINITE);
                    mConnectivitySnackbar.show();
                    //isActivityLoaded = true;
                    mConnectivityBroadcastReceiver = new ConnectivityBroadcastReceiver(new ConnectivityBroadcastReceiver.ConnectivityReceiverListener() {
                       @Override
                        public void onNetworkConnectionConnected() {
                            mConnectivitySnackbar.dismiss();
                            //isActivityLoaded = false;
                            signIn();
                            isBroadcastReceiverRegistered = false;
                            unregisterReceiver(mConnectivityBroadcastReceiver);
                        }
                    });
                    IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
                    isBroadcastReceiverRegistered = true;
                    registerReceiver(mConnectivityBroadcastReceiver, intentFilter);
                }
            }
        });
    }


    //If logged and notification token saved pass to next activity
    @Override
    protected void onStart() {
        // Check for existing Google Sign In mAccount, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if(account != null) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
            String value =  preferences.getString("NotificationToken", null);
            if (value==null && NetworkConnection.isConnected(getApplicationContext())) { prepNotifToken();}
            else{
                //Toast.makeText(this,"Wellcome Again", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        }
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetworkConnection.isConnected(getApplicationContext())&&mConnectivitySnackbar!=null) {
            mConnectivitySnackbar.dismiss();
            //signIn();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (isBroadcastReceiverRegistered) {
            isBroadcastReceiverRegistered = false;
            unregisterReceiver(mConnectivityBroadcastReceiver);
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

        startProgressLoading(true);
    }

    private void startProgressLoading(Boolean start){
        if(start){
            signInButton.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE); }
        else{
            signInButton.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            //isActivityLoaded = true;
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            mAccount = completedTask.getResult(ApiException.class);

            if(mAccount != null){

                // Signed in successfully, show authenticated UI.
                Toast.makeText(this,"Wellcome " + mAccount.getGivenName(), Toast.LENGTH_LONG).show();
                createUserAccount();
            }

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Google Sign In Error", "signInResult:failed code=" + e.getStatusCode());
            startProgressLoading(false);
            //Toast.makeText(this, "signInResult:failed code=" + e.getStatusCode(), Toast.LENGTH_SHORT).show();
        }
    }


    private void createUserAccount() {

        //For Form Post query
        Call<QuerryResponse> mFirstLoginCall;
        mFirstLoginCall = ApiClient.getRetrofitApi().RegisterUserOnLoginPost(mAccount.getId(),
                                                                             mAccount.getGivenName(),
                                                                             mAccount.getFamilyName(),
                                                                             mAccount.getDisplayName(),
                                                                             mAccount.getEmail());

        mFirstLoginCall.enqueue(new Callback<QuerryResponse>() {
            @Override
            public void onResponse(Call<QuerryResponse> call, Response<QuerryResponse> response) {

                if (!response.isSuccessful()) {

                    //mFirstLoginCall = call.clone();
                    //mFirstLoginCall.enqueue(this);

                    Toast.makeText(LoginActivity.this,"Code: " + response.code(), Toast.LENGTH_LONG).show();
                    onFailureActon();
                    return;
                }

                if(response.code()==201)  prepNotifToken();
                else{onFailureActon();
                    return;}

                //Toast.makeText(LoginActivity.this,"Code: " + response.code()+"\n"+response.body().getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<QuerryResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this,"Unable to connect with server",Toast.LENGTH_SHORT).show();
                Log.d(TAG,t.toString());
                onFailureActon();
            }
        });
    }

    //get notification token form fire base
    //check if its saved in the data preferences, if store the data
    //
    private void prepNotifToken(){
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if(!task.isSuccessful()){
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                String value =  preferences.getString("NotificationToken", null);
                if (value!=null){
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
                onFailureActon();
                return;
            }else {
                mNotificationToken = task.getResult().getToken();

                if(mNotificationToken!=null)   {

                    Log.i(TAG, " The completed result: " + task.getResult().getToken());

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                    String value =  preferences.getString("NotificationToken", null);

                    if(value==null) value="";
                    if (!value.equals(mNotificationToken)) {
                        //setNotifToken();
                        //Toast.makeText(LoginActivity.this,"Token " + task.getResult().getToken() ,Toast.LENGTH_LONG).show();
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                        sharedPreferencesEditor.putString("NotificationToken",mNotificationToken);
                        sharedPreferencesEditor.apply();

                        //Making an API call - Thread, Volley, okHttp, Retrofit
                        SaveNotifTokenOnCloud();
                    }
                    SaveNotifTokenOnCloud();
                    //startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    //finish();
                    return;
                }
                onFailureActon();
                return;
            }
        });

    }

    private void SaveNotifTokenOnCloud(){

        Call<QuerryResponse>  updateTokenCall = ApiClient.getRetrofitApi().UpdateToken(mAccount.getId(), "notification",mNotificationToken);

        updateTokenCall.enqueue(new Callback<QuerryResponse>() {
            @Override
            public void onResponse(Call<QuerryResponse> call, Response<QuerryResponse> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(LoginActivity.this,"Unable to connect with server",Toast.LENGTH_SHORT).show();

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                    String value =  preferences.getString("NotificationToken", null);
                    if (value!=null){
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                        sharedPreferencesEditor.putString("NotificationToken",null);
                        sharedPreferencesEditor.apply();
                        }

                    onFailureActon();
                    return;
                }

                if(response.code()==200){
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }else{
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                    SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                    sharedPreferencesEditor.putString("NotificationToken",null);
                    sharedPreferencesEditor.apply();

                    onFailureActon();
                    return;}
            }

            @Override
            public void onFailure(Call<QuerryResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this,"Unable to connect with server",Toast.LENGTH_SHORT).show();

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                String value =  preferences.getString("NotificationToken", null);
                if (value!=null){
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                    SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                    sharedPreferencesEditor.putString("NotificationToken",null);
                    sharedPreferencesEditor.apply();
                }

                onFailureActon();
            }
        });
        return;
    }

    private void onFailureActon(){
        startProgressLoading(false);
        mGoogleSignInClient.signOut();
        //isActivityLoaded = false;
    }

    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            finish();
            return;
        }

        doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.press_again_to_exit, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        //if(mFirstLoginCall !=null)    mFirstLoginCall.cancel();
        super.onDestroy();
    }
}