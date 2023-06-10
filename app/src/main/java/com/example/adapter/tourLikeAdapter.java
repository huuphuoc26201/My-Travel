package com.example.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.model.tourLikeData;
import com.example.mytravel.R;
import com.example.mytravel.listTour;

import java.util.List;

public class tourLikeAdapter extends RecyclerView.Adapter<tourLikeAdapter.tourLikeHolder>{
    private Context mContext;
    private List<tourLikeData> mListwhy;

    public tourLikeAdapter(Context mContext) {
        this.mContext = mContext;
    }
    public void setData(List<tourLikeData> list){
        this.mListwhy=list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public tourLikeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.dong_tourlike,parent,false);
        return new tourLikeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull tourLikeHolder holder, int position) {
        final tourLikeData whydata=mListwhy.get(position);
        if(whydata==null){
            return;
        }
        holder.image.setImageResource(whydata.getImageUrl());
        holder.name.setText(whydata.getName());
        holder.like.setText(whydata.getLike());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, listTour.class);
                intent.putExtra("name",whydata.getName());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mListwhy!=null){
            return mListwhy.size();
        }
        return 0;
    }

    public class tourLikeHolder extends RecyclerView.ViewHolder{

        private ImageView image;
        private TextView name,like;
        public tourLikeHolder(@NonNull View itemView) {
            super(itemView);

            image=itemView.findViewById(R.id.place_image);
            name=itemView.findViewById(R.id.place_name);
            like=itemView.findViewById(R.id.like);
        }
    }
}
