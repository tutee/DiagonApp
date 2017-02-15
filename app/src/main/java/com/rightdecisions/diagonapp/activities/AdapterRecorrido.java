package com.rightdecisions.diagonapp.activities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rightdecisions.diagonapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdapterRecorrido extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<DataRecorrido> data= Collections.emptyList();
    DataRecorrido current;
    int currentPos=0;

    // create constructor to innitilize context and data sent from MainActivity
    public AdapterRecorrido(Context context, List<DataRecorrido> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_lista_recorridos, parent,false);

        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder= (MyHolder) holder;
        DataRecorrido current = data.get(position);
        myHolder.textFishName.setText(current.itiName);


        //myHolder.textSize.setText("Size: " + current.sizeName);
        //myHolder.textType.setText("Category: " + current.catName);
        //myHolder.textPrice.setText("Rs. " + current.price + "\\Kg");
        //myHolder.textPrice.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));

        //myHolder.ivFish.setImageBitmap(current.fishImage);

        // load image into imageview using glide

        /*Glide.with(context).load(current.sitioImage)
                //.placeholder(R.drawable.ic_img_error)
                //.error(R.drawable.ic_img_error)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(myHolder.ivFish);*/

        /*Glide.with(context).load("http://192.168.1.7/test/images/" + current.fishImage)
                .placeholder(R.drawable.ic_img_error)
                .error(R.drawable.ic_img_error)
                .into(myHolder.ivFish);*/

        /*Glide.with(context).load(current.fishImage)
                .asBitmap()
                .into(myHolder.ivFish);*/

        //Glide.with(context).load(current.fishImage).into(myHolder.ivFish);

        /*Glide.with(context).load(current.fishImage)
                .asBitmap()
                //.error(R.drawable.ic_img_error)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        // you can do something with loaded bitmap here

                        // .....

                        myHolder.ivFish.setImageBitmap(resource);
                    }
                });*/


    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setFilter(List<DataRecorrido> countryModels) {
        data = new ArrayList<>();
        data.addAll(countryModels);
        notifyDataSetChanged();
    }


    class MyHolder extends RecyclerView.ViewHolder{

        TextView textFishName;
        Button ivFish;

        TextView textSize;
        TextView textType;
        TextView textPrice;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            textFishName= (TextView) itemView.findViewById(R.id.nombre);
            ivFish= (Button) itemView.findViewById(R.id.btnrecodelete);
            //textSize = (TextView) itemView.findViewById(R.id.descripcion);
            //textType = (TextView) itemView.findViewById(R.id.textType);
            //textPrice = (TextView) itemView.findViewById(R.id.button);
        }



    }

}
