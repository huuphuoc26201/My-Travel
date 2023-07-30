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

import com.example.model.bookHotelData;
import com.example.mytravel.History;
import com.example.mytravel.R;

import java.util.ArrayList;
import java.util.List;

public class historyHotelAdapter extends RecyclerView.Adapter<historyHotelAdapter.HotelViewHolder> {

    Context context;
    List<bookHotelData> hotelDataList;

    public historyHotelAdapter(Context context, ArrayList<bookHotelData> hotelDataList) {
        this.context = context;
        this.hotelDataList = hotelDataList;
    }


    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.dong_historyhotel, parent, false);

        // here we create a recyclerview row item layout file
        return new HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder,  int position) {
        final bookHotelData data_position= hotelDataList.get(position);
        if(data_position==null){
            return;
        }
        holder.placeName.setText(hotelDataList.get(position).getName());
        holder.phone.setText(hotelDataList.get(position).getPhone());
        holder.email.setText(hotelDataList.get(position).getEmail());
        holder.number.setText( "Người lớn: "+ hotelDataList.get(position).getPersons()+"\nTrẻ em: "+hotelDataList.get(position).getChild());
        holder.numberRooms.setText(hotelDataList.get(position).getNumberRooms());
        holder.date.setText(hotelDataList.get(position).getFromdate()+" - "+hotelDataList.get(position).getTodate());
        holder.bookDate.setText(hotelDataList.get(position).getBookDate());
        holder.total.setText(hotelDataList.get(position).getTotal());
        holder.hotel.setText(hotelDataList.get(position).getBookHotel());
        holder.payment.setText(hotelDataList.get(position).getPayment());

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, History.class);
                intent.putExtra("REMOVE",data_position.getId());
                intent.putExtra("payHotel",data_position.getPayment());
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

        TextView placeName, phone, email,number,numberRooms,date,bookDate,total,payment,hotel;
        ImageView remove;
        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            placeName = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
            email = itemView.findViewById(R.id.email);
            number = itemView.findViewById(R.id.number);
            numberRooms = itemView.findViewById(R.id.numberRooms);
            date = itemView.findViewById(R.id.date);
            bookDate = itemView.findViewById(R.id.bookdate);
            total = itemView.findViewById(R.id.total);
            payment=itemView.findViewById(R.id.thanhtoan);
            hotel=itemView.findViewById(R.id.hotel);
            remove=itemView.findViewById(R.id.remove);

        }
    }

}
