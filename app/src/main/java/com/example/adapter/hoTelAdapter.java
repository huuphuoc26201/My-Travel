package com.example.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.model.hoTelData;
import com.example.mytravel.R;
import com.example.mytravel.bookHotel;

import java.util.ArrayList;
import java.util.List;

public class hoTelAdapter extends RecyclerView.Adapter<hoTelAdapter.HotelViewHolder> {

    Context context;
    List<hoTelData> hotelDataList;

    public hoTelAdapter(Context context, ArrayList<hoTelData> hotelDataList) {
        this.context = context;
        this.hotelDataList = hotelDataList;
    }


    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.dong_hotel, parent, false);

        // here we create a recyclerview row item layout file
        return new HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder,  int position) {
        final hoTelData data_position= hotelDataList.get(position);
        if(data_position==null){
            return;
        }
        holder.placeName.setText(hotelDataList.get(position).getName());
        holder.money.setText(hotelDataList.get(position).getGia());
        holder.danhgia.setText(hotelDataList.get(position).getDanhgia());
        Glide.with(context).
                load(hotelDataList.get(position).getImageUrl())
                .into(holder.placeImage);
        holder.vitri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address =data_position.getVitri();
                Uri intentUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, intentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            1);
                    return;
                }
                mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(mapIntent);
            }
        });

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
        TextView placeName, money, vitri,danhgia;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            placeImage = itemView.findViewById(R.id.place_image);
            placeName = itemView.findViewById(R.id.place_name);
            money = itemView.findViewById(R.id.money);
            vitri = itemView.findViewById(R.id.textView53);
            danhgia = itemView.findViewById(R.id.textView7);
        }
    }

}
