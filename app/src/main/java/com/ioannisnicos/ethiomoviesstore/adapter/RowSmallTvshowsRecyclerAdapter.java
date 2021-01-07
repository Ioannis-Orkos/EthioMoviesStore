package com.ioannisnicos.ethiomoviesstore.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.ioannisnicos.ethiomoviesstore.R;
import com.ioannisnicos.ethiomoviesstore.database.FavouriteDBHandler;
import com.ioannisnicos.ethiomoviesstore.models.Tvshow;


import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RowSmallTvshowsRecyclerAdapter extends RecyclerView.Adapter<RowSmallTvshowsRecyclerAdapter.TvshowViewHolder> {

    private WeakReference<Context> mContext;
    private List<Tvshow> mTvshows;
    private OnBKItemClickListener mBKListener;
    private RequestTvshowClickListener mStoreItemListener;
    private OnFavouriteMovieClickListener mFavClickListener;
    private int mWidth, mHeight;

    public interface OnBKItemClickListener {
        void onBKItemClick(int position);
    }

    public interface RequestTvshowClickListener {
        void onStoreItemClick(int position);
    }

    public interface OnFavouriteMovieClickListener {
        void onFavouriteItemClick(int position);
    }

    public void setOnBKItemClickListener(OnBKItemClickListener listener) {
        mBKListener = listener;
    }
    public void setOnFavItemClickListener(OnFavouriteMovieClickListener listener) {
        mFavClickListener = listener;
    }
    public void setRequestStoreClickListener(RequestTvshowClickListener listener) {
        mStoreItemListener = listener;
    }

    public RowSmallTvshowsRecyclerAdapter(Context context, List<Tvshow> movies) {
        mContext = new WeakReference<Context>(context);
        mTvshows = movies;
    }

    @Override
    public TvshowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TvshowViewHolder(LayoutInflater.from(mContext.get()).inflate(R.layout.card_movies_small, parent, false));
    }

    @Override
    public void onBindViewHolder(TvshowViewHolder holder, int position) {
        RequestOptions options=new RequestOptions();
        options.override(mWidth,mHeight);

        Glide.with(mContext.get())
                .asBitmap()
                .override(mWidth/2,mHeight/2)
                .load(mTvshows.get(position).getPoster())
                .centerCrop()
                //.transform(new DrawGradient(mContext))
                //  .apply(options)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(R.drawable.ic_movie_black_24)
                .into(holder.moviePosterImageView);
//
//       if (mMovies.get(position).getYear() != null)
//           holder.movieYearTextView.setText(mMovies.get(position).getYear());
//       else
          holder.movieYearTextView.setText("");


        if (FavouriteDBHandler.isTVShowFav(mContext.get(), mTvshows.get(position).getImdb_id())) {
            holder.fvbButton.setImageResource(R.drawable.ic_favorite_black_18dp);
            holder.fvbButton.setEnabled(true);
        } else {
            holder.fvbButton.setImageResource(R.drawable.ic_favorite_border_black_18dp);
            holder.fvbButton.setEnabled(true);
        }

        if(mTvshows.get(position).getAvailable()!=null) {
            if (mTvshows.get(position).getAvailable().equals("yes")) {
                holder.storeButton.setImageResource(R.drawable.ic_baseline_check_box_24);
            } else {
                holder.storeButton.setImageResource(R.drawable.ic_baseline_check_box_outline_blank_30);
            }
        }
        else {
            holder.storeButton.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mTvshows.size();
    }


    public class TvshowViewHolder extends RecyclerView.ViewHolder {

         public CardView movieCard;

         public ImageView moviePosterImageView;
         public ImageButton storeButton;
         public ImageButton fvbButton;
         public TextView movieYearTextView;


        public TvshowViewHolder(View itemView) {
            super(itemView);

            moviePosterImageView = itemView.findViewById(R.id.sml_movie_card_image_view);
            mWidth =  (int) (mContext.get().getResources().getDisplayMetrics().widthPixels * 0.33333);
            mHeight = (int) ((mContext.get().getResources().getDisplayMetrics().widthPixels * 0.31) / 0.66);

            moviePosterImageView.getLayoutParams().width =mWidth;
            moviePosterImageView.getLayoutParams().height = mHeight;

            movieYearTextView = itemView.findViewById(R.id.sml_movie_card_yearText);
            movieCard = itemView.findViewById(R.id.sml_movie_card_view);
            storeButton = itemView.findViewById(R.id.sml_movie_card_IBstore);
            fvbButton = itemView.findViewById(R.id.sml_movie_card_IBfav);

            storeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mStoreItemListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mStoreItemListener.onStoreItemClick(position);
                        }}
                }
            });

            movieCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mBKListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mBKListener.onBKItemClick(position);
                        }}
                }
            });

            fvbButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (FavouriteDBHandler.isTVShowFav(mContext.get(), mTvshows.get(getAdapterPosition()).getImdb_id())) {
                        FavouriteDBHandler.removeTVShowFromFav(mContext.get(), mTvshows.get(getAdapterPosition()).getImdb_id());
                        fvbButton.setImageResource(R.drawable.ic_favorite_border_black_18dp);
                        if (mFavClickListener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                mFavClickListener.onFavouriteItemClick(position);
                            }}
                    }else {
                        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                        FavouriteDBHandler.addTVShowToFav(mContext.get(),
                                                         mTvshows.get(getAdapterPosition()).getImdb_id(),
                                                         mTvshows.get(getAdapterPosition()).getType(),
                                                         mTvshows.get(getAdapterPosition()).getTitle(),
                                                         mTvshows.get(getAdapterPosition()).getPoster(),
                                                         mTvshows.get(getAdapterPosition()).getLast_updated());
                        fvbButton.setImageResource(R.drawable.ic_favorite_black_18dp);
                         }}}
            );

        }
    }







    private static class DrawGradient extends BitmapTransformation {
            Context context;

        public DrawGradient(Context context) {
            super();
            this.context=context;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform,
                                   int outWidth, int outHeight) {
            int w = outWidth;
            int h = outHeight;
            Bitmap overlay = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(overlay);

            canvas.drawBitmap(toTransform, 0, 0, null);
         //   toTransform.recycle();

            Paint paint = new Paint();
            float gradientHeight = h / 2f;
            LinearGradient shader = new LinearGradient(0, h - gradientHeight, 0, h, 0xFFFFFFFF, 0x00FFFFFF, Shader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            canvas.drawRect(0, h - gradientHeight, w, h, paint);
            return overlay;

        //    return createCustomMarker(context,toTransform,"hrllp");
        }


        @Override
        public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
                   messageDigest.update("gradient transformation".getBytes());
        }
    }


    public class BlurTransformation extends BitmapTransformation {

        private RenderScript rs;

        public BlurTransformation(Context context) {
            super();

            rs = RenderScript.create(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            Bitmap blurredBitmap = toTransform.copy(Bitmap.Config.ARGB_8888, true);

            // Allocate memory for Renderscript to work with
            Allocation input = Allocation.createFromBitmap(rs, blurredBitmap, Allocation.MipmapControl.MIPMAP_FULL, Allocation.USAGE_SHARED);
            Allocation output = Allocation.createTyped(rs, input.getType());

            // Load up an instance of the specific script that we want to use.
            ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setInput(input);

            // Set the blur radius
            script.setRadius(5);

            // Start the ScriptIntrinisicBlur
            script.forEach(output);

            // Copy the output to the blurred bitmap
           output.copyTo(blurredBitmap);

            return blurredBitmap;


        }

        @Override
        public void updateDiskCacheKey(MessageDigest messageDigest) {
            messageDigest.update("blur transformation".getBytes());
        }
    }


//    public  static Bitmap createCustomMarker(Context context, Bitmap OBitmap, String _name) {
//
//        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.places_pin_layout, null);
//        CircleImageView markerImage = (CircleImageView) marker.findViewById(R.id.profile_image);
//        TextView txt_name = (TextView)marker.findViewById(R.id.textview_disname_nav_header);
//        markerImage.setImageBitmap(OBitmap);
//
//
//
//        txt_name.setText(_name);
//
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        displayMetrics=context.getResources().getDisplayMetrics();
//        marker.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
//        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
//        marker.buildDrawingCache();
//        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        marker.draw(canvas);
//
//
//
//
//        return bitmap;
//    }


}
