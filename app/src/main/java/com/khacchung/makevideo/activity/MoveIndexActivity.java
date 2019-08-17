package com.khacchung.makevideo.activity;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.khacchung.makevideo.R;
import com.khacchung.makevideo.adapter.ImageMoveAdapter;
import com.khacchung.makevideo.application.MyApplication;
import com.khacchung.makevideo.base.BaseActivity;
import com.khacchung.makevideo.base.ShowLog;
import com.khacchung.makevideo.databinding.ActivityMoveIndexBinding;
import com.khacchung.makevideo.handler.MyClickHandler;
import com.khacchung.makevideo.handler.MySelectedItemListener;
import com.khacchung.makevideo.model.MyImageModel;
import com.khacchung.makevideo.util.CodeSelectedItem;

import java.util.ArrayList;
import java.util.Collections;

public class MoveIndexActivity extends BaseActivity implements MySelectedItemListener, MyClickHandler {
    private ActivityMoveIndexBinding binding;
    private MyApplication myApplication;
    private ArrayList<MyImageModel> listImage;
    private ImageMoveAdapter adapter;

    public static final String[] PERMISSION_LIST = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public static void startIntent(Activity activity) {
        Intent intent = new Intent(activity, MoveIndexActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleToolbar(getResources().getString(R.string.edit_index_image));
        enableBackButton();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_move_index);
        binding.setHandler(this);

        realtimePermission(PERMISSION_LIST);

        myApplication = MyApplication.getInstance();
        listImage = myApplication.getListIamge();
        if (listImage == null || listImage.size() < 3) {
            ShowLog.ShowLog(this, binding.getRoot(), getString(R.string.errror), false);
            finish();
        }

        initView();
    }

    private void initView() {
        adapter = new ImageMoveAdapter(this, listImage, this);

        binding.setLayoutManager(new GridLayoutManager(this, 2));
        binding.setMyAdapter(adapter);

        adapter.notifyDataSetChanged();

        ItemTouchHelper ith = new ItemTouchHelper(_ithCallback);
        ith.attachToRecyclerView(binding.rvListImage);
    }

    ItemTouchHelper.Callback _ithCallback = new ItemTouchHelper.Callback() {
        public boolean onMove(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            Collections.swap(listImage, viewHolder.getAdapterPosition(), target.getAdapterPosition());
            adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        }

        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                    ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.START | ItemTouchHelper.END);
        }
    };

    @Override
    public void selectedItem(Object obj, int code) {

    }

    @Override
    public void selectedItem(Object obj, int code, int p) {
        if (code == CodeSelectedItem.CODE_REMOVE && p < listImage.size() && listImage.size() > 3) {
            listImage.remove(p);
            adapter.notifyDataSetChanged();
        } else if (listImage.size() <= 3) {
            ShowLog.ShowLog(this, binding.getRoot(), getString(R.string.need_3_image), false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_next_and_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_next) {
            if (listImage.size() > 2) {
                CreateVideoActivity.startIntent(this);
                finish();
            } else {
                ShowLog.ShowLog(this, binding.getRoot(), getString(R.string.need_3_image), false);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SelectImageActivity.startIntent(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fl_add_image) {
            SelectImageActivity.startIntent(this);
            finish();
        }
    }

    @Override
    public void onClickWithData(View view, Object value) {

    }
}
