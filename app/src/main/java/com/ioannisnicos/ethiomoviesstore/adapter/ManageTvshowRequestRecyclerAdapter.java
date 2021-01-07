package com.ioannisnicos.ethiomoviesstore.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ioannisnicos.ethiomoviesstore.R;
import com.ioannisnicos.ethiomoviesstore.retrofit.ApiClient;
import com.ioannisnicos.ethiomoviesstore.retrofit_movie_response.ManageMovieRequestResponse;
import com.ioannisnicos.ethiomoviesstore.retrofit_tvshow_response.ManageTvshowRequestResponse;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ManageTvshowRequestRecyclerAdapter extends RecyclerView.Adapter<ManageTvshowRequestRecyclerAdapter.ManageRequestRecyclerAdapterViewHolder> {

    private WeakReference<Context>                      mContext;
    private List<ManageTvshowRequestResponse>           mManageRequestList;
    private OnSubscribeButtonClickListener              mSubscribeButtonListener;

    public interface OnSubscribeButtonClickListener {
        void onSubscribeAcceptButtonClick(int position,String msg);
        void onSubscribeRejectButtonClick(int position,String msg);
        void onSubscribeDelButtonClick(int position);
    }

    public void setOnSubscribeButtonClickListener(OnSubscribeButtonClickListener listener) {
        mSubscribeButtonListener = listener;
    }


    public ManageTvshowRequestRecyclerAdapter(Context context, List<ManageTvshowRequestResponse> manageRequestList) {
        mContext = new WeakReference<Context>(context);
        mManageRequestList = manageRequestList;
    }

    @Override
    public ManageTvshowRequestRecyclerAdapter.ManageRequestRecyclerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ManageTvshowRequestRecyclerAdapter.ManageRequestRecyclerAdapterViewHolder(LayoutInflater.from(mContext.get()).inflate(R.layout.card_manage_request_large, parent, false));
    }

    @Override
    public void onBindViewHolder(ManageTvshowRequestRecyclerAdapter.ManageRequestRecyclerAdapterViewHolder holder, int position) {

        holder.mProgressBar.setVisibility(View.GONE);

        Glide.with(mContext.get()).load(ApiClient.BASE_URL+ mManageRequestList.get(position).getUsers().getProf_img())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.userProfilePictureImageview);
        Glide.with(mContext.get()).load(mManageRequestList.get(position).getMovies().getPoster())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.movieImageView);
        if (mManageRequestList.get(position).getStatus().getTvshow_status().equals("yes"))
            holder.movieAvalImageView.setBackgroundColor(Color.GREEN);

        holder.movieTilteText.setText(mManageRequestList.get(position).getMovies().getTitle());
        holder.userUserNameText.setText(mManageRequestList.get(position).getUsers().getDisplay_name());

        if(mManageRequestList.get(position).getStatus().getReq_user_msg()!=null&&!mManageRequestList.get(position).getStatus().getReq_user_msg().isEmpty()){
            holder.userMsgText.setVisibility(View.VISIBLE);
            holder.userMsgText.setText("User Msg : " + mManageRequestList.get(position).getStatus().getReq_user_msg());
        }
        int itemViewType = getItemViewType(position);
        if (itemViewType==1){
            holder.textReqMesglayout.setVisibility(View.VISIBLE);
            holder.textReqMesg.setText( mManageRequestList.get(position).getStatus().getReq_store_msg());
        }else if(mManageRequestList.get(position).getStatus().getReq_store_msg()!=null){
            holder.storeMsgText.setVisibility(View.VISIBLE);
            holder.storeMsgText.setText("Store Msg : " + mManageRequestList.get(position).getStatus().getReq_store_msg());
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (mManageRequestList.get(position).getStatus().getReq_status().equals("pending")) return 1;

        return 0;
    }

    @Override
    public int getItemCount() {
        return mManageRequestList.size();
    }



    public class ManageRequestRecyclerAdapterViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView      userProfilePictureImageview;
        public TextView             userUserNameText;
        public TextView             movieTilteText;
        public ImageView            movieImageView;
        public ImageView            movieAvalImageView;

        public ImageView            menuButtonImageView;

        public ConstraintLayout     cardBackGround;

        public TextView             userMsgText;
        public TextView             storeMsgText;
        public TextInputLayout textReqMesglayout;
        public TextInputEditText textReqMesg;
        public ProgressBar mProgressBar;



        public ManageRequestRecyclerAdapterViewHolder(View itemView) {
                    super(itemView);

            mProgressBar = itemView.findViewById(R.id.progressBar_request_movie);

            userProfilePictureImageview = itemView.findViewById(R.id.circle_img_request_userprof);
                userUserNameText = itemView.findViewById(R.id.textView_request_user_name);

                movieImageView = itemView.findViewById(R.id.img_request_movie);
                movieAvalImageView = itemView.findViewById(R.id.img_aval_movie);
                movieTilteText = itemView.findViewById(R.id.textView_request_movie_title);

                userMsgText= itemView.findViewById(R.id.textView_request_movie_usermsg);
                storeMsgText= itemView.findViewById(R.id.textView_request_movie_storemsg);

                textReqMesglayout= itemView.findViewById(R.id.textinputlayout_request_movie_store_response);
                textReqMesg= itemView.findViewById(R.id.textInput_manage_req_msg);


                // cardBackGround = itemView.findViewById(R.id.img_request_movie);

                menuButtonImageView = itemView.findViewById(R.id.imageView_request_menu);

                menuButtonImageView.setOnClickListener(v -> {
                    closeKeyboard(v);

                    PopupMenu popup = new PopupMenu(mContext.get(), v);
                    //popup.setGravity(Gravity.RIGHT);
                    popup.getMenuInflater().inflate(R.menu.manage_media_menu, popup.getMenu());

                    int position = getAdapterPosition();
                    if(mManageRequestList.get(position).getStatus().getReq_status().equals("ready"))
                        popup.getMenu().findItem(R.id.accept).setVisible(false);
                    if(mManageRequestList.get(position).getStatus().getReq_status().equals("rejected"))
                        popup.getMenu().findItem(R.id.reject).setVisible(false);
                    popup.setOnMenuItemClickListener(item -> onOptionsItemSelected(item));
                    popup.show();
                });



            }

        private void closeKeyboard( View view ) {
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) mContext.get().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }


        private boolean onOptionsItemSelected(MenuItem item){
            switch (item.getItemId()) {
                case R.id.accept:
                    if (mSubscribeButtonListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            //if(getItemViewType()==1)
                            mProgressBar.setVisibility(View.VISIBLE);
                            mSubscribeButtonListener.onSubscribeAcceptButtonClick(position,textReqMesg.getText().toString());
                        }}
                    break;
                case R.id.reject:
                    if (mSubscribeButtonListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mProgressBar.setVisibility(View.VISIBLE);
                            mSubscribeButtonListener.onSubscribeRejectButtonClick(position,textReqMesg.getText().toString());
                        }}
                    break;
                case R.id.delete:
                    if (mSubscribeButtonListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mProgressBar.setVisibility(View.VISIBLE);
                            mSubscribeButtonListener.onSubscribeDelButtonClick(position);
                        }}
                    break;
                default:
            }
            return true;
        }
    }
}