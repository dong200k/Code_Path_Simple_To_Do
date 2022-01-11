package com.example.codepathsimpletodo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    ArrayList<String> items;
    onLongClickListener onLongClickListener;
    onClickListener onClickListener;

    public interface onLongClickListener{
        void onItemLongClick(int position);
    }
    public interface onClickListener{
        void onItemClick(int position);
    }

    //Constructor
    public ItemsAdapter(ArrayList<String>items, onLongClickListener onLongClickListener, onClickListener onClickListener){
        this.items = items;
        this.onLongClickListener = onLongClickListener;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate view
        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        //warp inside viewholder and return
        return new ViewHolder(todoView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //get item
        String item = items.get(position);
        //bind item to viewholder
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // member variable
        TextView tvItem;

        // constructor
        public ViewHolder(View itemView) {
            //super constructor
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }

        public void bind(String item){
            tvItem.setText(item);
            // On long click
            tvItem.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View view) {
                    // Remove item
                    onLongClickListener.onItemLongClick(getBindingAdapterPosition());
                    return true;
                }
            });
            // On click
            tvItem.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    // Edit item
                    onClickListener.onItemClick(getBindingAdapterPosition());
                }
            });
        }
    }
}
