package com.example.quizapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.RecViewHolder> {
    ArrayList<String> data1, data2;
    ArrayList<Bitmap> images;
    Context context;

    public RecAdapter(Context ctx, ArrayList<String> s1, ArrayList<String> s2, ArrayList<Bitmap> img){
        context = ctx;
        data1 = s1;
        data2 = s2;
        images = img;
    }

    @NonNull
    @Override
    public RecViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_layout, parent, false);

        return new RecViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecViewHolder holder, int position) {
        holder.text1.setText(Integer.toString(position + 1) + ". " + data1.get(position));
        holder.text2.setText(data2.get(position));
        if(position < images.size()){
            holder.myImage.setImageBitmap(Bitmap.createScaledBitmap(images.get(position), 200, 200, false));
        } else {
            holder.myImage.setImageResource(R.drawable.common_google_signin_btn_icon_light);
        }
    }

    @Override
    public int getItemCount() {
        return data1.size();
    }

    public class RecViewHolder extends RecyclerView.ViewHolder{
        TextView text1, text2;
        ImageView myImage;

        public RecViewHolder(@NonNull View itemView){
            super(itemView);
            text1 = itemView.findViewById(R.id.playerName);
            text2 = itemView.findViewById(R.id.playerScore);
            myImage = itemView.findViewById(R.id.playerImage);
        }
    }
}
