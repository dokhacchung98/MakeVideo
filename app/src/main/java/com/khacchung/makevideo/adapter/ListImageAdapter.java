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
import com.khacchung.makevideo.databinding.RowImageEditBinding;
import com.khacchung.makevideo.handler.MyClickHandler;
import com.khacchung.makevideo.handler.MySelectedItemListener;
import com.khacchung.makevideo.model.MyImageModel;
import com.khacchung.makevideo.util.CodeSelectedItem;

import java.util.ArrayList;

public class ListImageAdapter extends RecyclerView.Adapter<ListImageAdapter.MyViewHolder> {
    private ArrayList<MyImageModel> listModel;
    private RowImageEditBinding binding;
    private boolean isHidden;
    private Context context;
    private MySelectedItemListener selectedItemListener;

    public ListImageAdapter(Context context, ArrayList<MyImageModel> listModel, boolean isHidden) {
        this.context = context;
        this.listModel = listModel;
        this.isHidden = isHidden;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.row_image_edit,
                parent,
                false
        );
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MyImageModel model = listModel.get(position);
        holder.bind(model);
        holder.binding.setIsHiden(isHidden);
        holder.binding.setMyClick(new MyClickHandler() {
            @Override
            public void onClick(View view) {

            }

            @Override
            public void onClickWithData(View view, Object value) {
                if (selectedItemListener != null)
                    if (view.getId() == R.id.imgThumbnail) {
                        selectedItemListener.selectedItem(value, CodeSelectedItem.CODE_SELECT, position);
                        selectedItemListener.selectedItem(value, CodeSelectedItem.CODE_SELECT);
                    }
            }
        });
        holder.binding.setMyImageModel(model);
    }

    @Override
    public int getItemCount() {
        return listModel.size();
    }

    public void setSelectedItemListener(MySelectedItemListener listener) {
        this.selectedItemListener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RowImageEditBinding binding;

        MyViewHolder(RowImageEditBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Object o) {
            binding.setVariable(BR.myImageModel, o);
            binding.executePendingBindings();
        }
    }
}
