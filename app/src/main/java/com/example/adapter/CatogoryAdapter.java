package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.model.Catogory;
import com.example.mytravel.R;

import java.util.List;

public class CatogoryAdapter extends ArrayAdapter<Catogory> {
    public CatogoryAdapter(@NonNull Context context, int resource, @NonNull List<Catogory> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select,parent,false);
        TextView tvselect=convertView.findViewById(R.id.tv_select);

        Catogory catogory=this.getItem(position);
        if(catogory!=null){
            tvselect.setText(catogory.getName());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_catogory,parent,false);
        TextView tvcatogory=convertView.findViewById(R.id.tv_catogory);

        Catogory catogory=this.getItem(position);
        if(catogory!=null){
            tvcatogory.setText(catogory.getName());
        }
        return convertView;
    }
}
