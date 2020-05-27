package com.pjj.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pjj.utils.ViewUtils;

import java.util.ArrayList;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.TimeViewHolder> {
    private ArrayList<Integer> mList;

    public void setList(ArrayList<Integer> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView textView = ViewUtils.createTextView(parent.getContext(), "");
        return new TimeViewHolder(textView);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeViewHolder holder, int position) {
        holder.textView.setText(mList.get(position)+":00");
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class TimeViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public TimeViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }
    }
}
