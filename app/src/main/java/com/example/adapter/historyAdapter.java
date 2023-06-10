package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.model.orderDetailsData;
import com.example.mytravel.R;

import java.util.ArrayList;

public class historyAdapter extends BaseAdapter {
    Context context;
    ArrayList<orderDetailsData> arraygiohang;

    public void setData(ArrayList<orderDetailsData> gioHanglist){
        this.arraygiohang=gioHanglist;
        notifyDataSetChanged();
    }

    public historyAdapter(Context context, ArrayList<orderDetailsData> arraygiohang) {
        this.context = context;
        this.arraygiohang = arraygiohang;
    }

    @Override
    public int getCount() {
        return arraygiohang.size();
    }

    @Override
    public Object getItem(int position) {
        return arraygiohang.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class ViewHoler{
        public TextView name,email,phone,total,thanhtoan,tour,date;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoler viewHoler=null;
        if(convertView==null){
            viewHoler=new ViewHoler();
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.dong_history,null);
            viewHoler.name=(TextView) convertView.findViewById(R.id.name);
            viewHoler.email=(TextView) convertView.findViewById(R.id.email);
            viewHoler.phone=(TextView) convertView.findViewById(R.id.phone);
            viewHoler.tour=(TextView) convertView.findViewById(R.id.textView46);
            viewHoler.total=(TextView) convertView.findViewById(R.id.total);
            viewHoler.date=(TextView) convertView.findViewById(R.id.textView48);
            viewHoler.thanhtoan=(TextView) convertView.findViewById(R.id.thanhtoan);
            convertView.setTag(viewHoler);
        }else {
            viewHoler=(ViewHoler) convertView.getTag();
        }

        orderDetailsData orderdetailsData=(orderDetailsData) getItem(position);
        viewHoler.name.setText(orderdetailsData.getName());
        viewHoler.email.setText(orderdetailsData.getEmail());
        viewHoler.phone.setText(orderdetailsData.getPhone());
        viewHoler.tour.setText(orderdetailsData.getTour());
        viewHoler.total.setText(orderdetailsData.getTotal());
        viewHoler.date.setText(orderdetailsData.getDate());
        viewHoler.thanhtoan.setText(orderdetailsData.getThanhtoan());


        return convertView;
    }
}
