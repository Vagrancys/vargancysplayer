package com.vargancys.vargancysplayer.base;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

/**
 * author: Vagrancy
 * e-mail: 18050829067@163.com
 * time  : 2020/02/04
 * version:1.0
 */
public abstract class BaseRecyclerAdapter extends RecyclerView.Adapter<BaseRecyclerAdapter.ViewHolder> {

    public interface OnItemClickListener{
        void onItemClick(int position, RecyclerView.ViewHolder holder);
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(int position,RecyclerView.ViewHolder holder);
    }
    private OnItemClickListener itemClickListener;
    private OnItemLongClickListener itemLongClickListener;

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickListener !=null){
                    itemClickListener.onItemClick(position,holder);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(itemLongClickListener !=null){
                    itemLongClickListener.onItemLongClick(position,holder);
                }
                return false;
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView){
            super(itemView);
        }
    }
}
