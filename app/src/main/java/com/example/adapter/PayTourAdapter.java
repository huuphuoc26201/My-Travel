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
import com.example.model.payTourData;
import com.example.mytravel.R;
import com.example.mytravel.payMentTour;
import com.example.mytravel.payTour;

import java.text.DecimalFormat;
import java.util.List;

public class PayTourAdapter extends RecyclerView.Adapter<PayTourAdapter.TourViewHolder> {

    Context context;
    List<payTourData> tourDataList;

    public PayTourAdapter(Context context, List<payTourData> tourDataList) {
        this.context = context;
        this.tourDataList = tourDataList;
    }


    @NonNull
    @Override
    public TourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.dong_giohang, parent, false);

        // here we create a recyclerview row item layout file
        return new TourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TourViewHolder holder,  int position) {
        final payTourData data_position= tourDataList.get(position);
        if(data_position==null){
            return;
        }
        holder.placeName.setText(tourDataList.get(position).getTentour());
        holder.fromDate.setText("Ngày đi: "+tourDataList.get(position).getDate());
        holder.nguoilon.setText("Người lớn: "+tourDataList.get(position).getNglon()+" vé");
        holder.treem.setText("Trẻ em: "+tourDataList.get(position).getTreem()+" vé");
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        holder.money.setText("Giá: "+decimalFormat.format(Double.parseDouble((tourDataList.get(position).getGiatour())))+" ₫");

        Glide.with(context).
                load(tourDataList.get(position).getHinhtour())
                .into(holder.placeImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, payMentTour.class);
                intent.putExtra("imageUrl",data_position.getHinhtour());
                intent.putExtra("placeName",data_position.getTentour());
                intent.putExtra("money", data_position.getGiatour());
                intent.putExtra("nguoilon",data_position.getNglon());
                intent.putExtra("treem",data_position.getTreem());
                intent.putExtra("fromdate",data_position.getDate());
                intent.putExtra("id",data_position.getId());
                intent.putExtra("maTour",data_position.getMaTour());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });


        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, payTour.class);
                intent.putExtra("REMOVE",data_position.getId());
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

        ImageView placeImage,checkBox;
        TextView placeName, money,fromDate,nguoilon,treem;


        public TourViewHolder(@NonNull View itemView) {
            super(itemView);

            placeImage = itemView.findViewById(R.id.imageView6);
            placeName = itemView.findViewById(R.id.textView);
            fromDate = itemView.findViewById(R.id.textView28);
            money = itemView.findViewById(R.id.textView31);
            nguoilon = itemView.findViewById(R.id.textView32);
            treem = itemView.findViewById(R.id.textView33);
            checkBox = itemView.findViewById(R.id.remove);
        }
    }
}