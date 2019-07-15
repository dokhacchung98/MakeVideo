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
import com.khacchung.makevideo.databinding.RowFolderBinding;
import com.khacchung.makevideo.handler.MyClickHandler;
import com.khacchung.makevideo.handler.MySelectedItemListener;
import com.khacchung.makevideo.model.MyFolderModel;
import com.khacchung.makevideo.util.CodeSelectedItem;

import java.util.ArrayList;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.MyViewHolder> implements MyClickHandler {
    private RowFolderBinding binding;
    private ArrayList<MyFolderModel> listFolder;
    private Context context;
    private MySelectedItemListener listener;

    public FolderAdapter(Context context, ArrayList<MyFolderModel> listFolder, MySelectedItemListener selectFolderListener) {
        this.context = context;
        this.listFolder = listFolder;
        this.listener = selectFolderListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.row_folder,
                parent,
                false
        );
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MyFolderModel model = listFolder.get(position);
        holder.bind(model);
        holder.binding.setMyModel(model);
        holder.binding.setHandler(this);
    }

    @Override
    public int getItemCount() {
        return listFolder.size();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onClickWithData(View view, Object value) {
        listener.selectedItem(value, CodeSelectedItem.CODE_SELECT_FOLDER);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private RowFolderBinding binding;

        MyViewHolder(RowFolderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Object o) {
            binding.setVariable(BR.myModel, o);
            binding.executePendingBindings();
        }

    }
}
