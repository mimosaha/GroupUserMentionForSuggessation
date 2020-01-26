package com.go.hungrynaki.groupmentioneditbox;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class ItemArrayAdapter extends RecyclerView.Adapter<ItemArrayAdapter.ViewHolder> {

    //All methods in this adapter are required for a bare minimum recyclerview adapter
    private int listItemLayout;
    private ArrayList<Item> itemList;
    private View.OnClickListener clickListener;
    // Constructor of the class
    public ItemArrayAdapter(int layoutId, ArrayList<Item> itemList, View.OnClickListener clickListener) {
        listItemLayout = layoutId;
        this.itemList = itemList;
        this.clickListener = clickListener;
    }

    // get the size of the list
    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }


    // specify the row layout file and click for each row
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(listItemLayout, parent, false);
        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    // load data in each row element
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition) {
        TextView item = holder.item;
        item.setText(itemList.get(listPosition).getName());
        item.setTag(itemList.get(listPosition).getName());
        item.setOnClickListener(clickListener);
    }

    // Static inner class to initialize the views of rows
    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView item;
        public ViewHolder(View itemView) {
            super(itemView);
            item = (TextView) itemView.findViewById(R.id.row_item);
        }
    }
}