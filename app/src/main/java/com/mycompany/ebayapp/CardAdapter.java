package com.mycompany.ebayapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    ArrayList<ItemData> mValues;
    Context mContext;
    private ItemClickListener mListener;

    public CardAdapter(Context context, ArrayList<ItemData> values) {
        mValues = values;
        mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView,zipView,shipView,condView,priceView;
        public ImageView imgView,wishView;

        public ViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.itemtitle);
            zipView = v.findViewById(R.id.itemzip);
            shipView = v.findViewById(R.id.itemship);
            condView = v.findViewById(R.id.itemcondition);
            imgView = v.findViewById(R.id.itemimage);
            priceView = v.findViewById(R.id.itemprice);
            wishView = v.findViewById(R.id.itemwish);

            wishView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "WishList clicked", Toast.LENGTH_SHORT).show();
                }
            });

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id_disp = mValues.get(getAdapterPosition()).id;
//                    Toast.makeText(view.getContext(),"ID: "+id_disp, Toast.LENGTH_SHORT).show();
                    mListener.onItemClick(view,getAdapterPosition());
                }
            });
        }
    }

    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_item_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder Vholder, int position) {
        if(!mValues.get(position).imageURL.equals("N/A"))
            Picasso.with(mContext).load(mValues.get(position).imageURL).into(Vholder.imgView);
        String title = mValues.get(position).title.substring(0,36).toUpperCase();
        title = title.substring(0,title.lastIndexOf(" ")) + "...";
        Vholder.textView.setText(title);
        Vholder.zipView.setText("Zip: "+mValues.get(position).zipcode);
        if(mValues.get(position).shippingType.contains("Free"))
            Vholder.shipView.setText(mValues.get(position).shippingType);
        else
            Vholder.shipView.setText("$"+mValues.get(position).shippingType);
        Vholder.condView.setText(mValues.get(position).conditionType);
        Vholder.priceView.setText("$"+mValues.get(position).price);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void setClickListener(ItemClickListener itemClickListener){
        this.mListener = itemClickListener;
    }
}
