package com.ioannisnicos.ethiomoviesstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ioannisnicos.ethiomoviesstore.R;
import com.ioannisnicos.ethiomoviesstore.models.Users;
import com.ioannisnicos.ethiomoviesstore.models.UsersAdditionalStatus;
import com.ioannisnicos.ethiomoviesstore.retrofit.ApiClient;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class SubscriptionsRecyclerAdapter extends RecyclerView.Adapter<SubscriptionsRecyclerAdapter.MovieViewHolder> {

    private WeakReference<Context> mContext;
    private List<UsersAdditionalStatus> mUsersSubsStatus;
    private List<Users> mUsers;
    private OnSubscribeButtonClickListener mSubscribeButtonListener;
    private OnSubscriptionBKClickListener mSubscriptionBKClickListener;

    public interface OnSubscribeButtonClickListener {
        void onSubscribeAcceptButtonClick(int position);
        void onSubscribeRejectButtonClick(int position);
    }

    public interface OnSubscriptionBKClickListener {
        void onSubscriptionBKClick(int position);
    }



    public SubscriptionsRecyclerAdapter(Context context, List<Users> stores, List<UsersAdditionalStatus> storesSubs) {
        mContext = new WeakReference<Context>(context);
        mUsers = stores;
        mUsersSubsStatus = storesSubs;
    }

    public void setOnSubscribeButtonClickListener(OnSubscribeButtonClickListener listener) {
        mSubscribeButtonListener = listener;
    }

    public void setOnSubscriptionBKClickListener(OnSubscriptionBKClickListener listener) {
        mSubscriptionBKClickListener = listener;
    }

    @Override
    public SubscriptionsRecyclerAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SubscriptionsRecyclerAdapter.MovieViewHolder(LayoutInflater.from(mContext.get()).inflate(R.layout.card_subscriptions_large, parent, false));
    }

    @Override
    public void onBindViewHolder(SubscriptionsRecyclerAdapter.MovieViewHolder holder, int position) {
        holder.mProgressBar.setVisibility(View.GONE);
//          Glide.with(mContext.get()).load(ApiClient.BASE_URL+ mUsers.get(position).getBanner())
//                .centerCrop()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(holder.storeBusinessImageview);
          Glide.with(mContext.get()).load(ApiClient.BASE_URL+ mUsers.get(position).getProf_img())
                 .centerCrop()
                 .diskCacheStrategy(DiskCacheStrategy.ALL)
                  .into(holder.profilePictureImageview);
//                        /*    try {
//                                Date date1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(mStores.get(position).getReg_date());
//                                holder.displaynameText.setText(date1.toString());
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }*/
        holder.storeUserNameText.setText(mUsers.get(position).getDisplay_name());
        holder.storeDescriptionText.setText(mUsers.get(position).getDescription());
//
//        if( mUsersSubsStatus.get(position).getSubscription_status()==null)    {
//            holder.subscribeButton.setText("Subscribe");
//            holder.subscribeButton.setBackgroundResource(R.drawable.style_button_radius);          }
//         else if(mUsersSubsStatus.get(position).getSubscription_status().equals("subscribed"))    {
//            holder.subscribeButton.setText("Unsubscribe");
//           }else  if(mUsersSubsStatus.get(position).getSubscription_status().equals("request"))    {
//            holder.subscribeButton.setText("Cancel request");
//       }

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }


        public class MovieViewHolder extends RecyclerView.ViewHolder {

        public TextView storeUserNameText;
        public TextView storeDescriptionText;
        public CircleImageView profilePictureImageview;
        public ImageView subscribeButton;
        public ConstraintLayout cardBackGround;
            public ProgressBar mProgressBar;


        public MovieViewHolder(View itemView) {
            super(itemView);

            profilePictureImageview = itemView.findViewById(R.id.circle_img_request_userprof);
            storeUserNameText = itemView.findViewById(R.id.textView_request_user_name);
            storeDescriptionText = itemView.findViewById(R.id.textView_request_movie_title);
            subscribeButton = itemView.findViewById(R.id.imageView_request_menu);
            cardBackGround = itemView.findViewById(R.id.card_subscriptions_large);
            mProgressBar = itemView.findViewById(R.id.progressBar_subscription_card);

            subscribeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(mContext.get(), v);
//                    popup.setGravity(Gravity.RIGHT);
                    popup.getMenuInflater().inflate(R.menu.manage_subscription_menu, popup.getMenu());

                    if (mUsersSubsStatus.get(getAdapterPosition()).getSubscription_status().equals("rejected")) popup.getMenu().findItem(R.id.reject).setVisible(false);
                    if (mUsersSubsStatus.get(getAdapterPosition()).getSubscription_status().equals("subscribed")) popup.getMenu().findItem(R.id.accept).setVisible(false);
                    popup.show();
                    popup.setOnMenuItemClickListener(item -> onOptionsItemSelected(item));
                }
            });


            cardBackGround.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View view) {
                                                      if (mSubscriptionBKClickListener != null) {
                                                          int position = getAdapterPosition();
                                                          if (position != RecyclerView.NO_POSITION) {
                                                              mSubscriptionBKClickListener.onSubscriptionBKClick(position);
                                                          }
                                                      }
                                                  }
                                              }
            );



        }

            private boolean onOptionsItemSelected(MenuItem item){
                switch (item.getItemId()) {
                    case R.id.accept:
                        if (mSubscribeButtonListener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                mSubscribeButtonListener.onSubscribeAcceptButtonClick(position);
                                mProgressBar.setVisibility(View.VISIBLE);
                            }}
                            break;
                            case R.id.reject:
                                if (mSubscribeButtonListener != null) {
                                    int position = getAdapterPosition();
                                    if (position != RecyclerView.NO_POSITION) {
                                        mSubscribeButtonListener.onSubscribeRejectButtonClick(position);
                                        mProgressBar.setVisibility(View.VISIBLE);
                                    }}
                                break;
                            default:
                        }

                return true;
                }


            }
        }