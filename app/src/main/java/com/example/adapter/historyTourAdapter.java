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

import com.example.model.historyTourData;
import com.example.mytravel.History;
import com.example.mytravel.R;

import java.util.ArrayList;
import java.util.List;

public class historyTourAdapter extends RecyclerView.Adapter<historyTourAdapter.HotelViewHolder> {

    Context context;
    List<historyTourData> hotelDataList;

    public historyTourAdapter(Context context, ArrayList<historyTourData> hotelDataList) {
        this.context = context;
        this.hotelDataList = hotelDataList;
    }


    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.dong_historytour, parent, false);

        // here we create a recyclerview row item layout file
        return new HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder,  int position) {
        final historyTourData data_position= hotelDataList.get(position);
        if(data_position==null){
            return;
        }
        holder.placeName.setText(hotelDataList.get(position).getName());
        holder.phone.setText(hotelDataList.get(position).getPhone());
        holder.email.setText(hotelDataList.get(position).getEmail());
        holder.number.setText( "Người lớn: "+ hotelDataList.get(position).getNglon()+"\nTrẻ em: "+hotelDataList.get(position).getTreem());
        holder.fromDate.setText(hotelDataList.get(position).getDate());
        holder.maTour.setText(hotelDataList.get(position).getMaTour());
        holder.bookDate.setText(hotelDataList.get(position).getDateTime());
        holder.total.setText(hotelDataList.get(position).getGiatour());
        holder.tour.setText(hotelDataList.get(position).getTentour());
        holder.payment.setText(hotelDataList.get(position).getPayment());

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, History.class);
                intent.putExtra("REMOVETOUR",data_position.getId());
                intent.putExtra("paymenttour",data_position.getPayment());
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

        TextView placeName, phone, email,number,fromDate,maTour,bookDate,total,payment,tour;
        ImageView remove;
        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            placeName = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
            email = itemView.findViewById(R.id.email);
            number = itemView.findViewById(R.id.number);
            fromDate = itemView.findViewById(R.id.fromDate);
            maTour = itemView.findViewById(R.id.maTour);
            bookDate = itemView.findViewById(R.id.bookdate);
            total = itemView.findViewById(R.id.total);
            payment=itemView.findViewById(R.id.thanhtoan);
            tour=itemView.findViewById(R.id.tour);
            remove=itemView.findViewById(R.id.remove);

        }
    }

}
