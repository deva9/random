package com.mycompany.ebayapp;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    ArrayList<ItemData> mValues;
    Context mContext;
    protected ItemListener mListener;

    public CardAdapter(Context context, ArrayList<ItemData> values) {
        mValues = values;
        mContext = context;
//        mListener=itemListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public TextView idView;
        public RelativeLayout relativeLayout;
        ItemData item;

        public ViewHolder(View v) {
            super(v);
//            v.setOnClickListener(this);
            textView = v.findViewById(R.id.itemtitle);
            idView = v.findViewById(R.id.itemcondition);

        }

//        public void setData(ItemData item) {
//            this.item = item;
//            textView.setText(item.title);
//        }

//        @Override
//        public void onClick(View view) {
//            if (mListener != null) {
//                mListener.onItemClick(item);
//            }
//        }
    }

    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_item_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder Vholder, int position) {
        Vholder.textView.setText(mValues.get(position).title);
        Vholder.idView.setText(mValues.get(position).id);


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public interface ItemListener {
        void onItemClick(ItemData item);
    }
}
