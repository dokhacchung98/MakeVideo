package com.khacchung.makevideo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.khacchung.makevideo.BR;
import com.khacchung.makevideo.R;
import com.khacchung.makevideo.databinding.RowTimerBinding;
import com.khacchung.makevideo.handler.MyClickHandler;
import com.khacchung.makevideo.handler.MySelectedItemListener;
import com.khacchung.makevideo.model.MyTimerModel;
import com.khacchung.makevideo.util.CodeSelectedItem;

import java.util.ArrayList;

public class ListTimerAdapter extends RecyclerView.Adapter<ListTimerAdapter.MyViewHolder>
        implements MyClickHandler {

    private ArrayList<MyTimerModel> listTimer;
    private Context context;
    private RowTimerBinding binding;
    private MySelectedItemListener listener;

    public ListTimerAdapter(Context context, ArrayList<MyTimerModel> listTimer, MySelectedItemListener listener) {
        this.listTimer = listTimer;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.row_timer,
                parent,
                false
        );
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MyTimerModel item = listTimer.get(position);
        holder.bind(item);
        holder.itemImageBinding.setHandler(this);
    }

    @Override
    public int getItemCount() {
        return listTimer.size();
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void onClickWithData(View view, Object value) {
        MyTimerModel effect = (MyTimerModel) value;
        listener.selectedItem(effect, CodeSelectedItem.CODE_SELECT);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowTimerBinding itemImageBinding;

        MyViewHolder(RowTimerBinding itemImageBinding) {
            super(itemImageBinding.getRoot());
            this.itemImageBinding = itemImageBinding;
        }

        public void bind(Object obj) {
            itemImageBinding.setVariable(BR.model, obj);
            itemImageBinding.executePendingBindings();
        }
    }
}