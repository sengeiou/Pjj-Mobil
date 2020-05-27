package com.pjj.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by xinheng on 2019/1/10.<br/>
 * describe：
 */
public class HeadRecycleAdapter<T extends RecyclerView.ViewHolder, A extends RecyclerView.Adapter<T>> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private A adapter;
    private View head;

    public HeadRecycleAdapter(A adapter, View head) {
        this.adapter = adapter;
        this.head = head;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        int itemViewType = adapter.getItemViewType(position - 1);
        if (itemViewType == 0) {
            throw new RuntimeException("itemViewType is not 0");
        }
        return itemViewType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        if (position == 0) {
            return new RecyclerView.ViewHolder(head) {
            };
        } else {
            return adapter.onCreateViewHolder(viewGroup, position - 1);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (position == 0) {

        } else {
            adapter.onBindViewHolder((T) viewHolder, position - 1);
        }
    }

    @Override
    public int getItemCount() {
        return 1 + adapter.getItemCount();
    }

    @Override
    public void registerAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        adapter.registerAdapterDataObserver(observer);
    }

    @Override
    public void unregisterAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        adapter.unregisterAdapterDataObserver(observer);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        adapter.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        adapter.onDetachedFromRecyclerView(recyclerView);
    }

}
