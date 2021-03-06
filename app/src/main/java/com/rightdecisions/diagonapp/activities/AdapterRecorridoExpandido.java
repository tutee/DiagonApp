package com.rightdecisions.diagonapp.activities;

import android.content.Context;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rightdecisions.diagonapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdapterRecorridoExpandido extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private OnItemClickListenerAdapterRecorridoExpandido onItemClickListenerAdapterRecorridoExpandido;
    List<DataRecorridoSitio> data= Collections.emptyList();
    DataRecorridoSitio current;
    int currentPos=0;

    // create constructor to innitilize context and data sent from MainActivity
    public AdapterRecorridoExpandido(Context context, List<DataRecorridoSitio> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_lista_sitios_recorridoexp, parent,false);

        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder= (MyHolder) holder;
        DataRecorridoSitio current = data.get(position);



        Log.e("REPETICION NUMERO", String.valueOf(data.indexOf(current)));
        Log.e("POSICION", String.valueOf(current.getSitioPos()));
        //current.sitioPos = data.indexOf(current);

        //Log.e("SITIO", String.valueOf(data.indexOf(current)));
       //Log.e("POSICION", String.valueOf(data.indexOf(current)));
        myHolder.textFishName.setText(current.getName());
        if (current.getTipo().equals("Origen")){
            myHolder.ivFish.setBackgroundResource(R.mipmap.ic_places_greenpro);
        } else if (current.getTipo().equals("Destino")){
            myHolder.ivFish.setBackgroundResource(R.mipmap.ic_places_redpro);
        }
        else if (current.getTipo().equals("Parada")){
            Log.e("ERROR", current.getName());
            myHolder.ivFish.setBackgroundResource(R.mipmap.ic_places_lbluepro);
        }
        else if (current.getTipo().equals("Unico")){
            Log.e("ERROR", current.getName());
            myHolder.ivFish.setBackgroundResource(R.mipmap.ic_places_redpro);
        }
        /*for (int j = 0; j < current.arraySitInt.size(); j++){
            String obj = String.valueOf(current.arraySitInt);
            myHolder.textFishName.setText(obj);
        }*/




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

    public void setOnItemClickListenerAdapterRecorridoExpandido (OnItemClickListenerAdapterRecorridoExpandido onItemClickListenerAdapterRecorridoExpandido){
        this.onItemClickListenerAdapterRecorridoExpandido = onItemClickListenerAdapterRecorridoExpandido;
    }

   /* public void setFilter(List<DataRecorrido> countryModels) {
        data = new ArrayList<>();
        data.addAll(countryModels);
        notifyDataSetChanged();
    }*/


    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView textFishName;
        ImageView ivFish;
        ImageButton ivFish1, ivFish2;

        TextView textSize;
        TextView textType;
        TextView textPrice;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            textFishName= (TextView) itemView.findViewById(R.id.nombre);
            ivFish= (ImageView) itemView.findViewById(R.id.img);
            ivFish1 = (ImageButton) itemView.findViewById(R.id.btncheck);
            ivFish2 = (ImageButton) itemView.findViewById(R.id.btnrecodelete);
            //textSize = (TextView) itemView.findViewById(R.id.descripcion);
            //textType = (TextView) itemView.findViewById(R.id.textType);
            //textPrice = (TextView) itemView.findViewById(R.id.button);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            ivFish1.setOnClickListener(this);
            ivFish2.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {

            if (view.getId() == ivFish1.getId()) {

                if (onItemClickListenerAdapterRecorridoExpandido!=null) {

                    onItemClickListenerAdapterRecorridoExpandido.checkItemClick(view, getAdapterPosition());

                }

            } else if (view.getId() == ivFish2.getId()) {

                if (onItemClickListenerAdapterRecorridoExpandido!=null) {

                    onItemClickListenerAdapterRecorridoExpandido.deleteItemClick(view, getAdapterPosition());

                }

            } else if (view.getId() == itemView.getId()) {

                if (onItemClickListenerAdapterRecorridoExpandido!=null) {

                    onItemClickListenerAdapterRecorridoExpandido.itemListClick(view, getAdapterPosition());

                }

            }



        }

        @Override
        public boolean onLongClick(View view) {
            if (view.getId() == itemView.getId()){

                if (onItemClickListenerAdapterRecorridoExpandido!=null) {

                    onItemClickListenerAdapterRecorridoExpandido.itemListLongClick(view, getAdapterPosition());

                }

            }
            return true;
        }
    }

    public interface OnItemClickListenerAdapterRecorridoExpandido {

        public void itemListLongClick (View view, int position);

        public void itemListClick (View view, int position);

        public void checkItemClick (View view, int position);

        public void deleteItemClick (View view, int position);

    }

}
