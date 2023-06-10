package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.model.gioHangData;
import com.example.mytravel.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class orderDetailsAdapter extends BaseAdapter {
    Context context;
    ArrayList<gioHangData> arraygiohang;

    public orderDetailsAdapter(Context context, ArrayList<gioHangData> arraygiohang) {
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
        public TextView order;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoler viewHoler=null;
        if(convertView==null){
            viewHoler=new ViewHoler();
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.dong_orderdetails,null);
            viewHoler.order=(TextView) convertView.findViewById(R.id.order);
            convertView.setTag(viewHoler);
        }else {
            viewHoler=(ViewHoler) convertView.getTag();
        }
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        gioHangData gioHangdata=(gioHangData) getItem(position);
        viewHoler.order.setText("- "+gioHangdata.getTentour()+" ( "+decimalFormat.format(gioHangdata.getGiatour())+" ₫"+" )\n"+"       Người lớn: "+gioHangdata.getNglon()+" vé  "+"     Trẻ em: "+gioHangdata.getTreem()+" vé\n"+"       Ngày đi: "+gioHangdata.getDate());
        return convertView;
    }

}
