package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.model.whydata;
import com.example.mytravel.R;

import java.util.List;

public class whyAdapter extends  RecyclerView.Adapter<whyAdapter.whyViewHolder>{
    private Context mContext;
    private List<whydata> mListwhy;

    public whyAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<whydata> list){
        this.mListwhy=list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public whyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_why,parent,false);
        return new whyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull whyViewHolder holder, int position) {
        whydata whydata=mListwhy.get(position);
        if(whydata==null){
            return;
        }
        holder.image.setImageResource(whydata.getIcon());
        holder.tieude.setText(whydata.getTieude());
        holder.ndung.setText(whydata.getNdung());

    }


    @Override
    public int getItemCount() {
        if(mListwhy!=null){
            return mListwhy.size();
        }
        return 0;
    }

    public class whyViewHolder extends RecyclerView.ViewHolder{

        private ImageView image;
        private TextView tieude,ndung;
        public whyViewHolder(@NonNull View itemView) {
            super(itemView);

            image=itemView.findViewById(R.id.imageView4);
            tieude=itemView.findViewById(R.id.textView55);
            ndung=itemView.findViewById(R.id.ndung);
        }
    }
}
