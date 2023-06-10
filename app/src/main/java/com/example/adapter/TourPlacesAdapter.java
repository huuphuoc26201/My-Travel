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

import com.bumptech.glide.Glide;
import com.example.model.TourData;
import com.example.mytravel.DetailsActivity;
import com.example.mytravel.R;

import java.text.DecimalFormat;
import java.util.List;

public class TourPlacesAdapter extends RecyclerView.Adapter<TourPlacesAdapter.TourViewHolder> {

    Context context;
    List<TourData> tourDataList;

    public TourPlacesAdapter(Context context, List<TourData> tourDataList) {
        this.context = context;
        this.tourDataList = tourDataList;
    }


    @NonNull
    @Override
    public TourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.tour_top_places, parent, false);

        // here we create a recyclerview row item layout file
        return new TourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TourViewHolder holder,  int position) {
        final TourData data_position= tourDataList.get(position);
        if(data_position==null){
            return;
        }
        holder.placeName.setText(tourDataList.get(position).getPlaceName());
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        holder.money.setText(decimalFormat.format(Double.parseDouble((tourDataList.get(position).getMoney())))+" ₫");
        Glide.with(context).
                load(tourDataList.get(position).getImageUrl())
                .into(holder.placeImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, DetailsActivity.class);
                intent.putExtra("imageUrl",data_position.getImageUrl());
                intent.putExtra("placeName",data_position.getPlaceName());
                intent.putExtra("money", data_position.getMoney());
                intent.putExtra("time",data_position.getTime());
                intent.putExtra("start",data_position.getStart());
                intent.putExtra("about",data_position.getAbout());
                intent.putExtra("imageUrl1",data_position.getImageUrl1());
                intent.putExtra("imageUrl2",data_position.getImageUrl2());
                intent.putExtra("trip",data_position.getTrip());
                intent.putExtra("maTour",data_position.getMaTour());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return tourDataList.size();
    }



    public static final class TourViewHolder extends RecyclerView.ViewHolder{

        ImageView placeImage;
        TextView placeName, money;

        public TourViewHolder(@NonNull View itemView) {
            super(itemView);

            placeImage = itemView.findViewById(R.id.place_image);
            placeName = itemView.findViewById(R.id.place_name);
            money = itemView.findViewById(R.id.money);
        }
    }
}