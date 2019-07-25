package com.khacchung.makevideo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.khacchung.makevideo.R;
import com.khacchung.makevideo.databinding.RowEditingToolsBinding;
import com.khacchung.makevideo.util.ToolType;

import java.util.ArrayList;
import java.util.List;

public class EditingToolsAdapter extends RecyclerView.Adapter<EditingToolsAdapter.ViewHolder> {

    private List<ToolModel> mToolList = new ArrayList<>();
    private OnItemSelected mOnItemSelected;

    public EditingToolsAdapter(OnItemSelected onItemSelected, Context context) {
        mOnItemSelected = onItemSelected;
        mToolList.add(new ToolModel(context.getResources().getString(R.string.brush), R.drawable.ic_brush, ToolType.BRUSH));
        mToolList.add(new ToolModel(context.getResources().getString(R.string.label_text), R.drawable.ic_text, ToolType.TEXT));
        mToolList.add(new ToolModel(context.getResources().getString(R.string.label_eraser), R.drawable.ic_eraser, ToolType.ERASER));
        mToolList.add(new ToolModel(context.getResources().getString(R.string.label_filter), R.drawable.ic_photo_filter, ToolType.FILTER));
        mToolList.add(new ToolModel(context.getResources().getString(R.string.label_emoji), R.drawable.ic_insert_emoticon, ToolType.EMOJI));
        mToolList.add(new ToolModel(context.getResources().getString(R.string.label_sticker), R.drawable.ic_sticker, ToolType.STICKER));
    }

    public interface OnItemSelected {
        void onToolSelected(ToolType toolType);
    }

    class ToolModel {
        private String mToolName;
        private int mToolIcon;
        private ToolType mToolType;

        ToolModel(String toolName, int toolIcon, ToolType toolType) {
            mToolName = toolName;
            mToolIcon = toolIcon;
            mToolType = toolType;
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowEditingToolsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_editing_tools, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ToolModel item = mToolList.get(position);
        holder.txtTool.setText(item.mToolName);
        holder.imgToolIcon.setImageResource(item.mToolIcon);
    }

    @Override
    public int getItemCount() {
        return mToolList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgToolIcon;
        TextView txtTool;

        ViewHolder(RowEditingToolsBinding binding) {
            super(binding.getRoot());
            imgToolIcon = binding.imgToolIcon;
            txtTool = binding.txtTool;
            itemView.setOnClickListener(v -> mOnItemSelected.onToolSelected(mToolList.get(getLayoutPosition()).mToolType));
        }
    }
}
