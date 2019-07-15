package com.khacchung.makevideo.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.khacchung.makevideo.R;
import com.khacchung.makevideo.databinding.FragmentBottomStickerEmojiDialogBinding;
import com.khacchung.makevideo.databinding.RowEmojiBinding;

import java.util.ArrayList;

import ja.burhanrashid52.photoeditor.PhotoEditor;

public class EmojiBSFragment extends BottomSheetDialogFragment {
    private FragmentBottomStickerEmojiDialogBinding binding;

    public EmojiBSFragment() {
    }

    private EmojiListener mEmojiListener;

    public interface EmojiListener {
        void onEmojiClick(String emojiUnicode);
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_bottom_sticker_emoji_dialog, null, false);
        dialog.setContentView(binding.getRoot());
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) binding.getRoot().getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
        ((View) binding.getRoot().getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        RecyclerView rvEmoji = binding.rvEmoji;

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 5);
        rvEmoji.setLayoutManager(gridLayoutManager);
        EmojiAdapter emojiAdapter = new EmojiAdapter();
        rvEmoji.setAdapter(emojiAdapter);
    }

    public void setEmojiListener(EmojiListener emojiListener) {
        mEmojiListener = emojiListener;
    }


    public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.ViewHolder> {

        ArrayList<String> emojisList = PhotoEditor.getEmojis(getActivity());

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RowEmojiBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_emoji, parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.txtEmoji.setText(emojisList.get(position));
        }

        @Override
        public int getItemCount() {
            return emojisList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtEmoji;

            ViewHolder(RowEmojiBinding binding) {
                super(binding.getRoot());
                txtEmoji = binding.txtEmoji;

                itemView.setOnClickListener(v -> {
                    if (mEmojiListener != null) {
                        mEmojiListener.onEmojiClick(emojisList.get(getLayoutPosition()));
                    }
                    dismiss();
                });
            }
        }
    }
}