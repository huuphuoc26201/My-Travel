package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.model.gioHangData;
import com.example.mytravel.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class gioHangAdapter extends BaseAdapter {
    Context context;
    ArrayList<gioHangData> arraygiohang;


    public void setData(ArrayList<gioHangData> gioHanglist){
        this.arraygiohang=gioHanglist;
        notifyDataSetChanged();
    }

    public gioHangAdapter(Context context, ArrayList<gioHangData> arraygiohang) {
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
        public TextView tentour,gia,ngay,nglon,treem;
        public ImageView anhtour;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoler viewHoler=null;
        if(convertView==null){
            viewHoler=new ViewHoler();
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.dong_giohang,null);
            viewHoler.tentour=(TextView) convertView.findViewById(R.id.textView);
            viewHoler.gia=(TextView) convertView.findViewById(R.id.textView31);
            viewHoler.ngay=(TextView) convertView.findViewById(R.id.textView28);
            viewHoler.anhtour=(ImageView) convertView.findViewById(R.id.imageView6);
            viewHoler.nglon=(TextView) convertView.findViewById(R.id.textView32);
            viewHoler.treem=(TextView) convertView.findViewById(R.id.textView33);
            convertView.setTag(viewHoler);
        }else {
            viewHoler=(ViewHoler) convertView.getTag();
        }
        gioHangData gioHangdata=(gioHangData) getItem(position);
        viewHoler.tentour.setText(gioHangdata.getTentour());
        viewHoler.nglon.setText("Người lớn: "+gioHangdata.getNglon()+" vé");
        viewHoler.treem.setText("Trẻ em: "+gioHangdata.getTreem()+" vé");
        viewHoler.ngay.setText("Ngày đi: "+gioHangdata.getDate());
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        viewHoler.gia.setText("Giá: " +decimalFormat.format(gioHangdata.getGiatour())+" ₫");
        Picasso.with(context).load(gioHangdata.getHinhtour())
                //.placeholder(R.drawable.codo_hue)
                .error((R.mipmap.ic_launcher))
                .into(viewHoler.anhtour);
        return convertView;
    }
}
