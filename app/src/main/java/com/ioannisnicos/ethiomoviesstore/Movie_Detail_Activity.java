package com.ioannisnicos.ethiomoviesstore;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.ioannisnicos.ethiomoviesstore.database.FavouriteDBHandler;
import com.ioannisnicos.ethiomoviesstore.models.Movies;
import com.ioannisnicos.ethiomoviesstore.retrofit.ApiClient;
import com.ioannisnicos.ethiomoviesstore.utils.Constants;
import com.wang.avi.AVLoadingIndicatorView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.palette.graphics.Palette;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Movie_Detail_Activity extends AppCompatActivity {

    public static final String MOVIE_ID =    "movie_id";
    public static final String MOVIE_TITLE = "movie_title";

    public static final String ADAPTER_POSITION = "position";
    public static final int POSITION_ACTIVITY_RETURN = -1;

    private String          mMovieId;
    private String          mMovieTitle;
    private int             mPostion;
    private Call<Movies>    mGetMovieDetailCall;
    private Movies          mMovieDetail;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    private ImageButton mBackImageButton;
    private ImageButton mFavImageButton;
    private ImageButton mShareImageButton;

    private ImageView   mPosterImageView;
    private ImageView   mPostertTopGradientImageView;
    private ImageView   mPostertBottomGradientImageView;
    private int         mPosterHeight;
    private int         mPosterWidth;
    private AVLoadingIndicatorView mProgressBar;

    private ConstraintLayout mConstraintLayout;
    private TextView         mDetailTextview;
    private TextView         mDetailReadMoreTextview;
    private TextView         mReleaseDateTextview;
    private TextView         mRuntimeTextview;

    private ImageView   mConstraintLayoutImageView;
    private TextView    mGenreTextView;
    private TextView    mTypeTextView;

    private Button mButtonTrailer;

    private FloatingActionButton mRentFloatingActionButton;
    private FloatingActionButton mPlayFloatingActionButton;

    private Palette.Swatch vibrantSwatch;
    private Palette.Swatch lightMutedSwatch;
    private Palette.Swatch lightVibrantSwatch;
    private Palette.Swatch darkVibrantSwatch;
    private Palette.Swatch mutedSwatch;
    private Palette.Swatch darkMutedSwatch;

    private Boolean  mIsLoad = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie__detail);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail_movie);
        setSupportActionBar(toolbar);
        setTitle("Loading...");

        mCollapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_movie_detail);


        Intent receivedIntent = getIntent();
        mMovieId = receivedIntent.getStringExtra(Movie_Detail_Activity.MOVIE_ID);
        mMovieTitle = receivedIntent.getStringExtra(Movie_Detail_Activity.MOVIE_TITLE);
        mPostion = receivedIntent.getIntExtra(Movie_Detail_Activity.ADAPTER_POSITION,1);
        if (mMovieId == null) finish();

        mProgressBar = (AVLoadingIndicatorView)  findViewById(R.id.imageview_detail_progressBar);

        mPosterWidth =         getResources().getDisplayMetrics().widthPixels;
        mPosterHeight = (int) (getResources().getDisplayMetrics().heightPixels * 0.8);

        mPosterImageView = (ImageView) findViewById(R.id.imageview_detail_collapsing_poster);
        mPosterImageView.getLayoutParams().height = mPosterHeight;
        mPosterImageView.getLayoutParams().width = mPosterWidth;

        mPostertBottomGradientImageView = (ImageView) findViewById(R.id.imageview_detail_poster_bottomgradient);
        mPostertBottomGradientImageView.getLayoutParams().width = mPosterWidth;
        mPostertTopGradientImageView = (ImageView) findViewById(R.id.imageview_detail_poster_topgradient);
        mPostertTopGradientImageView.getLayoutParams().width = mPosterWidth;

        mConstraintLayoutImageView = findViewById(R.id.imageView_constraintlayout_movie_detail);
        mConstraintLayoutImageView.getLayoutParams().height = getResources().getDisplayMetrics().heightPixels;

        mGenreTextView = (TextView) findViewById(R.id.text_view_genre_movie_detail);
        mTypeTextView = (TextView) findViewById(R.id.text_view_type_movie_detail);
        mDetailTextview  = (TextView)findViewById(R.id.textview_detail_movie_description);
        mDetailReadMoreTextview= (TextView) findViewById(R.id.text_view_read_more_movie_detail);
        mReleaseDateTextview = (TextView) findViewById(R.id.text_view_Release_movie_detail);
        mRuntimeTextview = (TextView) findViewById(R.id.text_view_Runtime_movie_detail);

        mRentFloatingActionButton = findViewById(R.id.floatingactionbutton_movie_detail_rentmovie);
        mPlayFloatingActionButton = findViewById(R.id.floatingactionbutton_movie_detail_playmovie);


        mButtonTrailer = findViewById(R.id.button_detail_movie_trailer);

        mBackImageButton = (ImageButton) findViewById(R.id.image_button_back_movie_detail);
        mBackImageButton.setOnClickListener(view -> onBackPressed());

        mFavImageButton = (ImageButton) findViewById(R.id.image_button_fav_movie_detail);
        mFavImageButton.setOnClickListener(view -> {
            if(mIsLoad){
                if (FavouriteDBHandler.isMovieFav(this,mMovieId)) {
                    FavouriteDBHandler.removeMovieFromFav(this, mMovieId);
                    mFavImageButton.setImageResource(R.drawable.ic_favorite_border_black_18dp);
                }else {
                    view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    FavouriteDBHandler.addMovieToFav(this,
                            mMovieDetail.getImdb_id(),
                            mMovieDetail.getType(),
                            mMovieDetail.getTitle(),
                            mMovieDetail.getPoster(),
                            mMovieDetail.getReleased());
                    mFavImageButton.setImageResource(R.drawable.ic_favorite_black_18dp);
                }}
        });

        LoadMovieDetail();

        GoogleSignInAccount mMyGoogleAccountInfo = GoogleSignIn.getLastSignedInAccount(this);
        mRentFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewzz) {
                if(mIsLoad){
//                Bundle args = new Bundle();
////                args.putString(RentMovieDialog.USER_ID,mMyGoogleAccountInfo.getId());
////                args.putString(RentMovieDialog.MOVIE_ID,mMovieId);
////                args.putString(RentMovieDialog.MOVIE_TIILE,mMovieTitle);
////                args.putInt(RentMovieDialog.STORE_ID,-1);
////
////                RentMovieDialog dialog=new RentMovieDialog();
////                dialog.setArguments(args);
////                dialog.show(getSupportFragmentManager(),"dialog");
                }
            }
        });

    }


    private void LoadMovieDetail(){

        mGetMovieDetailCall = ApiClient.getRetrofitApi().getMovieDetail(mMovieId);

        mGetMovieDetailCall.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {

                if (!response.isSuccessful())    {
                    mGetMovieDetailCall = call.clone();
                    mGetMovieDetailCall.enqueue(this);
                    return;}


                setImageButtons(mMovieId,response.body().getType(),response.body().getPoster(), response.body().getTitle(), response.body().getYear());
                mMovieDetail=response.body();
                Glide.with(getApplication()).load(response.body().getPoster())
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                mProgressBar.setVisibility(View.GONE);
                                Animation animationZoomInOut = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom);
                                mPosterImageView.startAnimation(animationZoomInOut);

                                //     mBackdropImageView.startAnimation(animation2);
                                SetBackGroundImage(resource);
                                SetPaletteColor(resource);


                                return false;
                            }
                        })
                        .into(mPosterImageView);

                if(response.body().getTrailer()!=null){
                    mButtonTrailer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(response.body().getTrailer()));
                            // startActivity(youtubeIntent);
                            String match = " ";
                            Pattern p = Pattern.compile("(?<=\\?v=)[\\w-]+$",Pattern.MULTILINE|Pattern.CASE_INSENSITIVE );
                            Matcher m = p.matcher(response.body().getTrailer());
                            m.find();
                            if (m.group().length() != 0)   match =  m.group();

                            Intent  intent = YouTubeStandalonePlayer.createVideoIntent(Movie_Detail_Activity.this,"AIzaSyBdvxkyhvvWw57aFf5t00ziJUqd6Q", match, 0, true, false);
                            startActivity(intent);
                        }
                    });}else mButtonTrailer.setVisibility(View.INVISIBLE);

                StringBuilder title = new StringBuilder(response.body().getTitle());
                if (title.length() > 25) {
                    title.setLength(22);
                    title.append("...");
                }

                mCollapsingToolbarLayout.setTitle(title);

                if(response.body().getReleased()!=null) {

                    Timestamp stamp = new Timestamp(Integer.parseInt(response.body().getReleased()));
                    Date date = new Date(Long.parseLong(response.body().getReleased()) * 1000L);
                    SimpleDateFormat originalFormat = new SimpleDateFormat("MMM dd,yyyy");
                    //    Date date2 = originalFormat.parse( date);
                    mReleaseDateTextview.append(originalFormat.format(date));
                }
                if(response.body().getRuntime()!=null) {
                    int run = Integer.parseInt(response.body().getRuntime());
                    mRuntimeTextview.append(String.valueOf((int) run / 60) + "hr " + String.valueOf((int) run % 60) + " min");

                }
                mDetailTextview.setText(response.body().getSynopsis());

                if(mDetailTextview.getLineCount()>3)
                {mDetailReadMoreTextview.setVisibility(View.VISIBLE);
                    mDetailTextview.setLines(3);
                    mDetailReadMoreTextview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mDetailTextview.setMaxLines(100);
                            mDetailReadMoreTextview.setVisibility(View.GONE);


                        }
                    });
                }


                if(response.body().getGenres()!=null&&!response.body().getGenres().get(0).isEmpty()){
                    List<String> geners = response.body().getGenres();
                    StringBuilder sb = new StringBuilder();
                    for (String s : geners)
                    {
                        sb.append(s.substring(0, 1).toUpperCase() + s.substring(1));
                        sb.append(",");
                    }
                    if( sb.length() > 0 )
                        sb.setLength( sb.length() - 1 );
                    mGenreTextView.setText(sb);
                }

                if(response.body().getType()!=null)
                    mTypeTextView.setText(response.body().getType());

                mIsLoad=true;

            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {

                mGetMovieDetailCall = call.clone();
                mGetMovieDetailCall.enqueue(this);
                Log.d("yo",t.toString());
                return;


            }
        });

    }




    private void setImageButtons(final String movieId,String type, final String posterPath, final String movieTitle, final String year) {
        if (movieId == null) return;
        if (FavouriteDBHandler.isMovieFav(Movie_Detail_Activity.this, movieId)) {
            mFavImageButton.setTag(Constants.TAG_FAV);
            mFavImageButton.setImageResource(R.drawable.ic_favorite_white_24dp);
        } else {
            mFavImageButton.setTag(Constants.TAG_NOT_FAV);
            mFavImageButton.setImageResource(R.drawable.ic_favorite_border_white_24dp);
        }
        mFavImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                if ((int) mFavImageButton.getTag() == Constants.TAG_FAV) {
                    FavouriteDBHandler.removeMovieFromFav(Movie_Detail_Activity.this, movieId);
                    mFavImageButton.setTag(Constants.TAG_NOT_FAV);
                    mFavImageButton.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                } else {
                    FavouriteDBHandler.addMovieToFav(Movie_Detail_Activity.this, movieId,type, movieTitle, posterPath, year);

                    mFavImageButton.setTag(Constants.TAG_FAV);
                    mFavImageButton.setImageResource(R.drawable.ic_favorite_white_24dp);
                }
            }
        });}

    private void SetBackGroundImage(Drawable resource){

        //to make black and weight
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);



        Drawable clone = resource.getConstantState().newDrawable().mutate();
        clone.setColorFilter(filter);


        mConstraintLayoutImageView.setImageDrawable(clone);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.scale);
        mConstraintLayoutImageView.startAnimation(anim);

    }

    private void SetPaletteColor(Drawable resource){

        Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();

        Palette.from(bitmap).maximumColorCount(32).generate(new Palette.PaletteAsyncListener() {

            @Override
            public void onGenerated(Palette palette) {
                vibrantSwatch = palette.getVibrantSwatch();
                lightVibrantSwatch = palette.getLightVibrantSwatch();
                darkVibrantSwatch = palette.getDarkVibrantSwatch();
                mutedSwatch = palette.getMutedSwatch();
                lightMutedSwatch = palette.getLightMutedSwatch();
                darkMutedSwatch = palette.getDarkMutedSwatch();
                //       mRentFloatingActionButton.getBackground().mutate().setTint(ContextCompat.getColor(getBaseContext(),lightVibrantSwatch.getRgb()));
                if(vibrantSwatch != null)  mRentFloatingActionButton.setBackgroundTintList(ColorStateList.valueOf(vibrantSwatch.getRgb()));
                if(vibrantSwatch != null)  mPlayFloatingActionButton.setBackgroundTintList(ColorStateList.valueOf(vibrantSwatch.getRgb()));
                if(vibrantSwatch != null)  mButtonTrailer.setBackgroundTintList(ColorStateList.valueOf(vibrantSwatch.getRgb()));
                if(vibrantSwatch != null)  mReleaseDateTextview.setTextColor(vibrantSwatch.getRgb());
                if(vibrantSwatch != null)  mRuntimeTextview.setTextColor(vibrantSwatch.getRgb());
                if(lightMutedSwatch != null)   mDetailReadMoreTextview.setTextColor(lightMutedSwatch.getRgb());


            }
        });
    }




    @Override
    public void onBackPressed() {

        Intent returnIntent = new Intent();
        returnIntent.putExtra(Movie_Detail_Activity.ADAPTER_POSITION,mPostion);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mGetMovieDetailCall != null) mGetMovieDetailCall.cancel();
    }

}
