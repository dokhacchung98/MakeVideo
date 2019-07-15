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
import com.khacchung.makevideo.databinding.RowItemMoveBinding;
import com.khacchung.makevideo.handler.MyClickHandler;
import com.khacchung.makevideo.handler.MySelectedItemListener;
import com.khacchung.makevideo.model.MyImageModel;
import com.khacchung.makevideo.util.CodeSelectedItem;

import java.util.ArrayList;

public class ImageMoveAdapter extends RecyclerView.Adapter<ImageMoveAdapter.MyViewHolder> {
    private RowItemMoveBinding binding;
    private ArrayList<MyImageModel> listModel;
    private Context context;
    private MySelectedItemListener listener;

    public ImageMoveAdapter(Context context, ArrayList<MyImageModel> listModel, MySelectedItemListener mySelectedItemListener) {
        this.context = context;
        this.listModel = listModel;
        this.listener = mySelectedItemListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.row_item_move,
                parent,
                false
        );
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MyImageModel model = listModel.get(position);
        holder.bind(model);
        holder.binding.setModel(model);
        holder.binding.setHadler(new MyClickHandler() {
            @Override
            public void onClick(View view) {

            }

            @Override
            public void onClickWithData(View view, Object value) {
                if (view.getId() == R.id.imgClose) {
                    listener.selectedItem(value, CodeSelectedItem.CODE_REMOVE, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listModel.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private RowItemMoveBinding binding;

        MyViewHolder(RowItemMoveBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Object o) {
            binding.setVariable(BR.model, o);
            binding.executePendingBindings();
        }
    }
}
