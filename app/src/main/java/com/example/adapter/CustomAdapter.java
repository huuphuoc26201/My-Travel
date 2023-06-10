package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.model.Docbaodata;
import com.example.mytravel.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Docbaodata> {
    public CustomAdapter(Context context, int resource, List<Docbaodata> items) {
        super(context, resource,items);
    }

    @Override
    public View getView(int position,View convertView, ViewGroup parent) {
        View view=convertView;
        if(view==null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.dong_layout_listview, null);
        }
        Docbaodata p= getItem(position);

        if(p!=null){
            TextView txt=(TextView) view.findViewById(R.id.textView);
            txt.setText(p.title);
            TextView txt1=(TextView) view.findViewById(R.id.textView1);
            txt1.setText(p.pubDate);
            ImageView imageView=(ImageView)  view.findViewById(R.id.imageView6);
            Picasso.with(getContext()).load(p.image).into(imageView);
        }
        return view;
    }
}
