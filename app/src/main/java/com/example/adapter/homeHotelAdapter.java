package com.example.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.model.hoTelData;
import com.example.mytravel.R;
import com.example.mytravel.bookHotel;

import java.util.List;

public class homeHotelAdapter extends RecyclerView.Adapter<homeHotelAdapter.HotelViewHolder> {

    Context context;
    List<hoTelData> hotelDataList;

    public homeHotelAdapter(Context context, List<hoTelData> hotelDataList) {
        this.context = context;
        this.hotelDataList = hotelDataList;
    }


    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.all_hotel, parent, false);

        // here we create a recyclerview row item layout file
        return new HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder,  int position) {
        final hoTelData data_position= hotelDataList.get(position);
        if(data_position==null){
            return;
        }

        LayerDrawable stars = (LayerDrawable) holder.ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(context, R.color.yelow), PorterDuff.Mode.SRC_ATOP);
        holder.placeName.setText(hotelDataList.get(position).getName());
        holder.money.setText(hotelDataList.get(position).getGia());
        holder.danhgia.setText(hotelDataList.get(position).getDanhgia());
        Glide.with(context).
                load(hotelDataList.get(position).getImageUrl())
                .into(holder.placeImage);
        holder.ratingBar.setRating(Float.parseFloat(hotelDataList.get(position).getDanhgia()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, bookHotel.class);
                intent.putExtra("imageUrl",data_position.getImageUrl());
                intent.putExtra("imageUrl1",data_position.getImageUrl1());
                intent.putExtra("imageUrl2",data_position.getImageUrl2());
                intent.putExtra("imageUrl3",data_position.getImageUrl3());
                intent.putExtra("name",data_position.getName());
                intent.putExtra("diachi", data_position.getDiachi());
                intent.putExtra("phone",data_position.getPhone());
                intent.putExtra("vitri",data_position.getVitri());
                intent.putExtra("gia",data_position.getGia());
                intent.putExtra("danhgia",data_position.getDanhgia());
                intent.putExtra("about",data_position.getAbout());
                intent.putExtra("back","back");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
    }





    @Override
    public int getItemCount() {
        return hotelDataList.size();
    }


    public static final class HotelViewHolder extends RecyclerView.ViewHolder{

        ImageView placeImage;
        TextView placeName, money,danhgia;
        RatingBar ratingBar;


        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            placeImage = itemView.findViewById(R.id.place_image);
            placeName = itemView.findViewById(R.id.place_name);
            money = itemView.findViewById(R.id.money);
            danhgia = itemView.findViewById(R.id.textView7);
            ratingBar=itemView.findViewById(R.id.ratingbar);

        }
    }

}
