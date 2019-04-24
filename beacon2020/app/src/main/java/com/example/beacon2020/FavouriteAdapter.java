package com.example.beacon2020;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder> {

    private List<FavouriteData> favouriteDataList;
    private OnItemCLickListener mListener;

    public interface OnItemCLickListener{
        void onItemClick(int position);
        void onItemDeleteCLick(int position);
    }
    @NonNull
    @Override
    public FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.favourite_item, viewGroup, false);

        FavouriteViewHolder evh = new FavouriteViewHolder(v, mListener);
        return  evh;
    }


    @Override
    public void onBindViewHolder(@NonNull FavouriteViewHolder favouriteViewHolder, int i) {
        FavouriteData currentItem = favouriteDataList.get(i);

        favouriteViewHolder.mTitleView.setText(currentItem.getTitle());
        favouriteViewHolder.mDescriptionView.setText(currentItem.getDescription());
        favouriteViewHolder.mTitleView.setText(currentItem.getTitle());

        Picasso.with(favouriteViewHolder.mImage.getContext()).load(currentItem.getUrl()).resize(720,500).into(FavouriteViewHolder.mImage);
//        Picasso.with(this.get).load(imageurl).resize(720,500).into(image);
    }

    @Override
    public int getItemCount() {
        return favouriteDataList.size();
    }



    public void setOnItemClickListener(OnItemCLickListener listener){
        mListener = listener;
    }

    public static class FavouriteViewHolder extends RecyclerView.ViewHolder{

        public String title, description, url,location;
        public TextView mTitleView;
        public TextView mDescriptionView;
        public static ImageView mImage;
        public ImageView deletebtn;

        public FavouriteViewHolder(@NonNull View itemView, final OnItemCLickListener listener) {
            super(itemView);

            mTitleView = itemView.findViewById(R.id.tvFavouriteTitle);
            mDescriptionView = itemView.findViewById(R.id.TVFavouriteDescription);
            mImage = itemView.findViewById(R.id.IVFavouriteImage);
            deletebtn = itemView.findViewById(R.id.FavDeletebtn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
            deletebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION){
                            listener.onItemDeleteCLick(position);
                        }
                    }
                }
            });
        }
    }

    public FavouriteAdapter(List<FavouriteData> favouriteList){
        favouriteDataList =favouriteList;
    }


}
