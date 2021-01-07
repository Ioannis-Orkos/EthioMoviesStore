package com.ioannisnicos.ethiomoviesstore;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.textfield.TextInputEditText;
import com.ioannisnicos.ethiomoviesstore.models.Stores;
import com.ioannisnicos.ethiomoviesstore.models.Users;
import com.ioannisnicos.ethiomoviesstore.retrofit.ApiClient;
import com.ioannisnicos.ethiomoviesstore.retrofit_movie_response.QuerryResponse;
import com.ioannisnicos.ethiomoviesstore.utils.FileHanndler;
import com.ioannisnicos.ethiomoviesstore.utils.NetworkConnection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private final static String TAG = SettingActivity.class.getSimpleName();

    private static final int REQUEST_CAPTURE_IMAGE = 111;
    private static final int REQUEST_STORED_IMAGE  = 222;
    private String cameraImageFilePath;
    private File   cammeraImageFile;

    private final static int MY_PERMISSION_FINE_LOCATION =  10;
    private final static int MY_PERMISSION_CAMERA =         20;
    private final static int MY_PERMISSION_UPLOAD =         30;

    private CircleImageView mProfCircleImage;
    private ImageButton Upload_Btn;
    private ImageButton Camera_Btn;


    private ImageView   mBannerImageView;
    private ImageButton Upload_banner_Btn;
    private ImageButton Camera_banner_Btn;
    private Boolean     IsUploadBanner = false;

    private Stores mUser;
    private Boolean mUserEdited=false;

    private FusedLocationProviderClient mLocationProviderClient;
    private LocationRequest  mLocationRequest;
    private LocationCallback mLocationCallback;
    private boolean          mLocationNotSet  = false;


    private String mAddress;
    private String mPostcode;

    private TextInputEditText textDisplayName;
    private TextInputEditText textBusinessName;
    private TextInputEditText textDescription;
    private TextInputEditText textLocation;
    private TextInputEditText textPostcode;
    private Spinner spinnerLanguage;

    private Button setCurrentLocationButton;

    private Menu mSaveMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24);


        setTitle("Setting");

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            mUser = (Stores) extra.get("UserInfo");
        }

        intLocation();

        mBannerImageView = findViewById(R.id.imageView_banner_setting);
        if(mUser.getBanner()!=null) Glide.with(this).load(ApiClient.BASE_URL+mUser.getBanner()).into(mBannerImageView);

        Upload_banner_Btn = findViewById(R.id.imageButton_gallery_banner_setting);
        Upload_banner_Btn.setOnClickListener(v -> {     IsUploadBanner = true;
                                                        uploadPermission();    });

        Camera_banner_Btn = findViewById(R.id.imageButton_camera_banner_setting);
        Camera_banner_Btn.setOnClickListener(v ->{     IsUploadBanner = true;
                                                        cameraPermission();    });

        mProfCircleImage = findViewById(R.id.img_profile_setting);
        if(mUser.getProf_img()!=null) Glide.with(this).load(ApiClient.BASE_URL+mUser.getProf_img()).into(mProfCircleImage);

        Upload_Btn = findViewById(R.id.imageButton_gallery_setting);
        Upload_Btn.setOnClickListener(v -> {     IsUploadBanner = false;
                                                 uploadPermission();    });;

        Camera_Btn = findViewById(R.id.imageButton_camera_setting);
        Camera_Btn.setOnClickListener(v ->{     IsUploadBanner = false;
                                                cameraPermission();    });

        TextWatcher watcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                 mSaveMenu.findItem(R.id.save).setVisible(true);
            }
        };


        textDisplayName = findViewById(R.id.textedit_display_name_setting);
        textDisplayName.setText(mUser.getDisplay_name());
        textDisplayName.addTextChangedListener(watcher);

        textDescription = findViewById(R.id.textedit_description_setting);
        textDescription.setText(mUser.getDescription());
        textDescription.addTextChangedListener(watcher);

        textBusinessName = findViewById(R.id.textedit_business_name_setting);
        textBusinessName.setText(mUser.getBusiness_name());
        textBusinessName.addTextChangedListener(watcher);

        textLocation = findViewById(R.id.text_location);
        textLocation.setText(mUser.getAddress());

        textPostcode = findViewById(R.id.text_postcode);
        textPostcode.setText(mUser.getPostcode());

        spinnerLanguage = findViewById(R.id.spinner_language_setting);
        List<String> lang = Arrays.asList(getResources().getStringArray(R.array.language));
        spinnerLanguage.setSelection(lang.lastIndexOf(mUser.getLanguage()));
        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           String pValue = (String) spinnerLanguage.getSelectedItem();
           @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String value = adapterView.getItemAtPosition(i).toString();
                if (!pValue.equals(value)) mSaveMenu.findItem(R.id.save).setVisible(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        setCurrentLocationButton = findViewById(R.id.button_curLoc_setting);
        setCurrentLocationButton.setOnClickListener(view -> setLocationRequestPermission());


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_savechange_menu, menu);

        mSaveMenu = menu;
        MenuItem item = menu.findItem(R.id.save);
        if (item != null) {
            item.setVisible(false);
                    }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.save:
                closeKeyboard();
                UpdateUserDetail("update_detail",textDisplayName.getText().toString(),textBusinessName.getText().toString(),textDescription.getText().toString(),spinnerLanguage.getSelectedItem().toString());
                break;
            default:
        }

        return super.onOptionsItemSelected(item);
    }

    private void UpdateUserDetail(String w, String dn,String bn, String d, String l) {

        Call<QuerryResponse> setLocationCall = ApiClient.getRetrofitApi().UpdateByWhatSetting(mUser.getGoogle_id(), w, dn,bn,d, l);

        setLocationCall.enqueue(new Callback<QuerryResponse>() {
            @Override
            public void onResponse(Call<QuerryResponse> call, Response<QuerryResponse> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(SettingActivity.this, "Code: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                mSaveMenu.findItem(R.id.save).setVisible(false);
                mUserEdited = true;
            }


            @Override
            public void onFailure(Call<QuerryResponse> call, Throwable t) {
                Toast.makeText(SettingActivity.this, "Unable to connect with server", Toast.LENGTH_LONG).show();
                Log.d(TAG, t.toString());
            }
        });
    }

    private void saveLocAndAddressToDB(Location location){

        Call<QuerryResponse> setLocationCall = ApiClient.getRetrofitApi().UpdateLocation(mUser.getGoogle_id(), "location",
                mAddress,mPostcode,String.valueOf(location.getLatitude()),
                String.valueOf(location.getLongitude() ));


        setLocationCall.enqueue(new Callback<QuerryResponse>() {
            @Override
            public void onResponse(Call<QuerryResponse> call, Response<QuerryResponse> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(SettingActivity.this,"Code: " + response.code(), Toast.LENGTH_LONG).show();
                    setCurrentLocationButton.setEnabled(true);
                    setCurrentLocationButton.setText("SET CURRENT LOCATION");
                    return;
                }

                textLocation.setText(mAddress);
                textPostcode.setText(mPostcode);
                setCurrentLocationButton.setEnabled(true);
                setCurrentLocationButton.setText("SET CURRENT LOCATION");
            }

            @Override
            public void onFailure(Call<QuerryResponse> call, Throwable t) {
                Toast.makeText(SettingActivity.this,"Unable to connect with server", Toast.LENGTH_LONG).show();
                setCurrentLocationButton.setEnabled(true);
                setCurrentLocationButton.setText("SET CURRENT LOCATION");
                mLocationNotSet=false;
                Log.d("yo",t.toString());
            }
        });
    }

    private void intLocation() {

        mLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1);
        mLocationRequest.setFastestInterval(1);
        //mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //  locationRequest.setInterval(UPDATE_INTERVAL);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
                if (locationAvailability.isLocationAvailable()) {
                    Log.i(TAG, "Location is available");
                } else {
                    Log.i(TAG, "Location is unavailable");
                }
            }

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Log.i(TAG, "Location result is available" + locationResult.getLocations().toString());

                if(mLocationNotSet) {
                    List<Location> locationList = locationResult.getLocations();
                    if (!locationList.isEmpty()) {
                        //The last location in the list is the newest
                        Location location = locationList.get(locationList.size() - 1);
                        Toast.makeText(SettingActivity.this, location.getLatitude() + "  " + location.getLongitude(), Toast.LENGTH_SHORT).show();
                        Geocoder geocoder = new Geocoder(SettingActivity.this, Locale.getDefault());

                        try {
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            Address obj = addresses.get(0);
                            mAddress = obj.getThoroughfare() + " , ";
                            mAddress = mAddress + obj.getSubAdminArea() + " , ";
                            mAddress = mAddress + obj.getAdminArea() + " , ";
                            mAddress = mAddress + obj.getCountryCode();
                            mPostcode = obj.getPostalCode();

                            mLocationProviderClient.removeLocationUpdates(mLocationCallback);
                            mLocationNotSet=false;
                            saveLocAndAddressToDB(location);
                            Log.v(TAG, "Address" + obj.toString());

                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
    }


    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mLocationProviderClient != null) {
            mLocationProviderClient.removeLocationUpdates(mLocationCallback);
        }
    }

    private void setLocation() {

        if (isGPSEnabled(SettingActivity.this) == false) {
            Toast.makeText(SettingActivity.this,"Please enable the device Location", Toast.LENGTH_SHORT).show();
            return;}

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;}

        mLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, SettingActivity.this.getMainLooper());
        mLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
            Location location = task.getResult();
            setCurrentLocationButton.setEnabled(false);
            setCurrentLocationButton.setText("Loading....");

            if(location==null) {
                //Toast.makeText(SettingActivity.this,"You just enable the location wait for a bit and try again letter", Toast.LENGTH_LONG).show();
                mLocationNotSet = true;
                return;}

            Toast.makeText(SettingActivity.this, location.getLatitude() +"  "+ location.getLongitude(), Toast.LENGTH_SHORT).show();
            Geocoder geocoder = new Geocoder(SettingActivity.this, Locale.getDefault());
            if(!mLocationNotSet) {
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    Address obj = addresses.get(0);
                    mAddress = obj.getThoroughfare() + " , ";
                    mAddress = mAddress + obj.getSubAdminArea() + " , ";
                    mAddress = mAddress + obj.getAdminArea() + " , ";
                    mAddress = mAddress + obj.getCountryCode();
                    mPostcode = obj.getPostalCode();

                    //Toast.makeText(SettingActivity.this,mAddress, Toast.LENGTH_LONG).show();
                    mLocationProviderClient.removeLocationUpdates(mLocationCallback);
                    saveLocAndAddressToDB(location);
                    Log.v(TAG, "Address" + obj.toString());


                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        });

        mLocationProviderClient.getLastLocation().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "Exception while getting the location: "+e.getMessage());
            }
        });
    }

    private void setLocationRequestPermission() {
        String[] perms ={ Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
        if(EasyPermissions.hasPermissions(this, perms)){
                   setLocation();
        }
        else{
            EasyPermissions.requestPermissions(this, "We need permissions to access near by rental stores",
                    MY_PERMISSION_FINE_LOCATION, perms);
        }
    }

    private void saveImageToDB() {

        if ((mUser== null) || cammeraImageFile == null) return;

        RequestBody id = RequestBody.create(mUser.getGoogle_id(),MediaType.parse("multipart/form-file") );
        RequestBody w = RequestBody.create("prof_img",MediaType.parse("multipart/form-file") );
        if(IsUploadBanner) w = RequestBody.create("banner_img", MediaType.parse("multipart/form-file"));
        RequestBody reqBody = RequestBody.create(cammeraImageFile,MediaType.parse("multipart/form-file") );
        MultipartBody.Part partImage = MultipartBody.Part.createFormData("store_profile_form", cammeraImageFile.getName(), reqBody);
        if(IsUploadBanner) partImage = MultipartBody.Part.createFormData("store_banner_form", cammeraImageFile.getName(), reqBody);


        Call<QuerryResponse> updateTokenCall = ApiClient.getRetrofitApi().sendImage(id, w, partImage);
        if(IsUploadBanner)   updateTokenCall = ApiClient.getRetrofitApi().sendImageBanner(id, w, partImage);


        updateTokenCall.enqueue(new Callback<QuerryResponse>() {
            @Override
            public void onResponse(Call<QuerryResponse> call, Response<QuerryResponse> response) {

                if (!response.isSuccessful()) {

                    // Toast.makeText(SettingActivity.this,"Code: " + response.code(), Toast.LENGTH_LONG).show();

                    //FirstLoginCall = call.clone();
                    // FirstLoginCall.enqueue(this);

                    return;
                }

                  mUserEdited = true;
                //Toast.makeText(MainActivity.this,"Code: " + response.code(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<QuerryResponse> call, Throwable t) {

                Log.d("yo", t.toString());


            }
        });
    }

    private void uploadPermission() {
        String[] perms ={Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,};
        if (EasyPermissions.hasPermissions(this, perms)) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_STORED_IMAGE);

        } else {
            EasyPermissions.requestPermissions(this, "We need permissions to change your profile picture",
                                                MY_PERMISSION_UPLOAD, perms);
        }
    }

    private void cameraPermission() {

        String[] perms ={Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,};

        if (EasyPermissions.hasPermissions(this, perms)) {

            cammeraImageFile    =   FileHanndler.getOutputMediaFile();
            cameraImageFilePath =   "file:" + cammeraImageFile.getAbsolutePath();

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri photoURI = FileProvider.getUriForFile(SettingActivity.this, getBaseContext().getPackageName()+ ".provider", cammeraImageFile);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(intent, REQUEST_CAPTURE_IMAGE);

        } else {
            EasyPermissions.requestPermissions(this, "We need permissions to change your profile picturet",
                    MY_PERMISSION_CAMERA, perms);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_STORED_IMAGE) {

            if (resultCode == Activity.RESULT_OK) {
                cammeraImageFile    =   FileHanndler.getOutputMediaFile();
                cameraImageFilePath =   "file:" + cammeraImageFile.getAbsolutePath();

                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();

                String[] okFileExtensions = new String[] {
                        "jpg","png","gif","jpeg"
                };


                for (String extension: okFileExtensions) {
                    if (picturePath.endsWith(extension)) {

                        Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                        thumbnail = FileHanndler.getResizedBitmap(thumbnail, 140);
                        cammeraImageFile.delete();//
                        OutputStream outFile = null;
                        try {
                            outFile = new FileOutputStream(cammeraImageFile);
                            thumbnail.compress(Bitmap.CompressFormat.JPEG, 98, outFile);

                            outFile.flush();
                            outFile.close();

                            if(IsUploadBanner)  Glide.with(this).load(thumbnail).into(mBannerImageView);
                            if(!IsUploadBanner) Glide.with(this).load(thumbnail).into(mProfCircleImage);
                            if (NetworkConnection.isConnected(getApplicationContext())) {
                                saveImageToDB();
                            } else{
                                Toast.makeText(SettingActivity.this,"Could not save the image no internet connection", Toast.LENGTH_LONG).show();
                            }
                            //BitMapToString(thumbnail);

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }}
            }
        }




        if (requestCode == REQUEST_CAPTURE_IMAGE) {

            if (resultCode == Activity.RESULT_OK && cammeraImageFile!=null) {
                //Glide.with(this).load(cameraImageFilePath).into(mProfCircleImage);

                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(cammeraImageFile.getAbsolutePath(), bitmapOptions);
                    bitmap = FileHanndler.getResizedBitmap(bitmap, 140);
                    cammeraImageFile.delete();
                    OutputStream outFile = null;
                    try {
                        outFile = new FileOutputStream(cammeraImageFile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 98, outFile);
                        outFile.flush();
                        outFile.close();

                        if(IsUploadBanner)  Glide.with(this).load(bitmap).into(mBannerImageView);
                        if(!IsUploadBanner) Glide.with(this).load(bitmap).into(mProfCircleImage);
                        if (NetworkConnection.isConnected(getApplicationContext())) {
                            saveImageToDB();
                        } else{
                            Toast.makeText(SettingActivity.this,"Could not save the image no internet connection", Toast.LENGTH_LONG).show();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                }


            } else if (resultCode == Activity.RESULT_CANCELED) {
                if(cammeraImageFile!=null) cammeraImageFile.delete();
            }
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

        if( requestCode == MY_PERMISSION_FINE_LOCATION) setLocation();

        if( requestCode == MY_PERMISSION_UPLOAD) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_STORED_IMAGE);
        }

        if( requestCode == MY_PERMISSION_CAMERA) {

            cammeraImageFile    =   FileHanndler.getOutputMediaFile();
            cameraImageFilePath =   "file:" + cammeraImageFile.getAbsolutePath();

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri photoURI = FileProvider.getUriForFile(SettingActivity.this, getBaseContext().getPackageName()+ ".provider", cammeraImageFile);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(intent, REQUEST_CAPTURE_IMAGE);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder b =  new AppSettingsDialog.Builder(this);
            b.build().show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void finishAfterTransition() {
        Intent intent = getIntent();
        intent.putExtra("user_edited", mUserEdited);
        setResult(RESULT_OK, intent);
        if (mLocationProviderClient != null) {
            mLocationProviderClient.removeLocationUpdates(mLocationCallback);
        }
        super.finishAfterTransition();
    }


    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @SuppressLint("MissingPermission")
    public boolean isGPSEnabled(Context mContext)
    {
        LocationManager lm = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);

        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

}

