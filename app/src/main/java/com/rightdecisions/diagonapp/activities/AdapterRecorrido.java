package com.rightdecisions.diagonapp.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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
    private OnItemClickListenerAdapter onItemClickListenerAdapter;
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

    public void setOnItemClickListenerAdapter (OnItemClickListenerAdapter onItemClickListenerAdapter) {
        this.onItemClickListenerAdapter = onItemClickListenerAdapter;
    }

    public void delete (){
    }



    public void setFilter(List<DataRecorrido> countryModels) {
        data = new ArrayList<>();
        data.addAll(countryModels);
        notifyDataSetChanged();
    }




    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textFishName;
        ImageButton ivFish;


        TextView textSize;
        TextView textType;
        TextView textPrice;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);

            textFishName= (TextView) itemView.findViewById(R.id.nombre);
            ivFish= (ImageButton) itemView.findViewById(R.id.btnrecodelete);

            ivFish.setOnClickListener(this);
            itemView.setOnClickListener(this);


            //textSize = (TextView) itemView.findViewById(R.id.descripcion);
            //textType = (TextView) itemView.findViewById(R.id.textType);
            //textPrice = (TextView) itemView.findViewById(R.id.button);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == ivFish.getId()){
                Log.e("ERROR", "ESTAS TOCANDO EL TARRITO!!!");

                if (onItemClickListenerAdapter!=null) {

                    onItemClickListenerAdapter.deleteItemClick(view, getAdapterPosition());

                }



            } else if (view.getId() == itemView.getId()){
                Log.e("ERROR", "ESTAS TOCANDO LA ROW!!!!!");

                if (onItemClickListenerAdapter!=null) {

                    onItemClickListenerAdapter.itemClicked(view, getAdapterPosition());

                }

            }
        }

    }

    public interface OnItemClickListenerAdapter {

        public void itemClicked(View view, int position);

        public void deleteItemClick (View view, int position);

    }

}
