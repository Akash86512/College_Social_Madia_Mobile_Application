package com.example.alumniapp.Home;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alumniapp.ItemClickListner;
import com.example.alumniapp.R;

import java.util.ArrayList;


class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
public ImageView imgname;
public TextView name;
public TextView time;
public TextView posttext;
public ImageView postimg;
public ImageView comment;
    private ItemClickListner itemClickListner;

    public HomeViewHolder(@NonNull View itemView) {
        super(itemView);

imgname=itemView.findViewById(R.id.image);
name=itemView.findViewById(R.id.name);
        time=itemView.findViewById(R.id.time);
        posttext=itemView.findViewById(R.id.posttext);
        postimg=itemView.findViewById(R.id.postImage);
        comment=itemView.findViewById(R.id.comment);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void setItemClickListner(ItemClickListner itemClickListner){
        this.itemClickListner=itemClickListner;
    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v,getAdapterPosition(),false);

    }

    @Override
    public boolean onLongClick(View v) {
        itemClickListner.onClick(v,getAdapterPosition(),false);
        return true;
    }
}


public class HomeAdapter extends RecyclerView.Adapter< HomeViewHolder> {

    private ArrayList<Home> technicals;
    private Context c;

    public HomeAdapter(Context c, ArrayList<Home> technicals) {
        this.technicals=technicals;
        this.c=c;

    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup Parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(Parent.getContext());
        View view = inflater.inflate(R.layout.home_item_layout, Parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
      final  Home h=technicals.get(position);
       holder.name.setText(h.getName());
       holder.imgname.setImageResource(h.getImgname());
       holder.time.setText(h.getTime());
       holder.postimg.setImageResource(h.getPostimg());
       holder.posttext.setText(h.getPosttext());

        holder.setItemClickListner(new ItemClickListner() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {






            }
        });

    }

    @Override
    public int getItemCount() {
        return technicals.size();
    }
}