package com.example.beacon2020;


import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> implements Filterable {

    private Context mCtx;
    private List<HistoryItem> historyList;
    private List<HistoryItem> prodlistFull;


    public HistoryAdapter(Context mCtx, List<HistoryItem> historyList) {
        this.mCtx = mCtx;
        this.historyList = historyList;
        prodlistFull =historyList;
    }
    @Override
    public Filter getFilter() {
        return historyFilter;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        LayoutInflater inflater = LayoutInflater.from(mCtx);
//        View view = inflater.inflate(R.layout.history_item, null, true);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_item, viewGroup,false);
//        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_list_item, viewGroup,false);
        HistoryViewHolder holder = new HistoryViewHolder(view);

        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder historyViewHolder, int i) {
        HistoryItem item = historyList.get(i);
        historyViewHolder.title.setText(item.getTitle());
        historyViewHolder.location.setText(item.getLocation());
//        historyViewHolder.imageView.setImageDrawable(item.getmImageResource());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }


    private Filter historyFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
//            List<HistoryItem> filteredList = new ArrayList<>();
//
//            if(constraint == null || constraint.length() ==0){
//                filteredList.addAll(prodlistFull);
//            }
//            else {
//                String filterPattern = constraint.toString().toLowerCase().trim();
//
//                for (HistoryItem item : prodlistFull){
//                    if(item.getTitle().toLowerCase().contains(filterPattern)){
//                        filteredList.add(item);
//                    }
//                }
//            }
//            FilterResults  result = new FilterResults();
//            result.values = filteredList;
            return  null;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            historyList.clear();
            historyList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };


    class HistoryViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView location;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.historyViewTItle);
            location = itemView.findViewById(R.id.historyViewLocation);


        }
    }
}
