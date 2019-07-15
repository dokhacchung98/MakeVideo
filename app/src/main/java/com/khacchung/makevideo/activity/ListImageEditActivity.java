package com.khacchung.makevideo.activity;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;

import com.khacchung.makevideo.R;
import com.khacchung.makevideo.adapter.FolderAdapter;
import com.khacchung.makevideo.adapter.ListImageAdapter;
import com.khacchung.makevideo.base.BaseActivity;
import com.khacchung.makevideo.databinding.ActivityListImageEditBinding;
import com.khacchung.makevideo.handler.MySelectedItemListener;
import com.khacchung.makevideo.model.MyFolderModel;
import com.khacchung.makevideo.model.MyImageModel;
import com.khacchung.makevideo.util.CodeSelectedItem;

import java.io.File;
import java.util.ArrayList;

public class ListImageEditActivity extends BaseActivity implements MySelectedItemListener {
    private ActivityListImageEditBinding binding;
    private ArrayList<MyFolderModel> listFolder;
    private ArrayList<MyImageModel> listImage;
    private ArrayList<MyImageModel> listImageByFolder;

    private FolderAdapter folderAdapter;
    private ListImageAdapter listImageAdapter;

    public static void startIntent(Activity activity) {
        Intent intent = new Intent(activity, ListImageEditActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleToolbar("Chỉnh Sửa Hình Ảnh");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list_image_edit);

        listFolder = new ArrayList<>();
        listImage = new ArrayList<>();
        listImageByFolder = new ArrayList<>();

        folderAdapter = new FolderAdapter(this, listFolder, this);
        listImageAdapter = new ListImageAdapter(this, listImageByFolder, false);
        listImageAdapter.setSelectedItemListener(this);

        binding.setFolderAdapter(folderAdapter);
        binding.setListImageAdapter(listImageAdapter);

        binding.setLayoutManagerFolder(new GridLayoutManager(this, 1));
        binding.setLayoutManagerImage(new GridLayoutManager(this, 2));

        getAllFolderContaningImage();
    }

    private void getAllFolderContaningImage() {
        Cursor query = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{"_data", "_id", "bucket_display_name", "bucket_id", "datetaken"},
                null, null, "bucket_display_name DESC");
        if (query.moveToFirst()) {
            int columnIndex = query.getColumnIndex("bucket_display_name");
            int columnIndex3 = query.getColumnIndex("datetaken");
            do {
                String imagePath = query.getString(query.getColumnIndex("_data"));
                if (!imagePath.endsWith(".gif")) {
                    query.getString(columnIndex3);
                    String string2 = query.getString(columnIndex);
                    boolean check = true;
                    String path = new File(imagePath).getParent();
                    for (MyFolderModel t : listFolder) {
                        if (t.getPathFolder().trim().equals(path.trim())) {
                            check = false;
                            break;
                        }
                    }

                    listImage.add(new MyImageModel(imagePath, false, path));

                    if (check) {
                        listFolder.add(new MyFolderModel(string2, path, false));
                    }
                }
            } while (query.moveToNext());
        }

        if (listFolder.size() > 0) {
            listFolder.get(0).setSelected(true);
            getAllImageFromPathFolder(listFolder.get(0).getPathFolder());
        }

    }

    private void getAllImageFromPathFolder(String pathFolder) {
        listImageByFolder.clear();
        for (MyImageModel tmp : listImage) {
            if (tmp.getPathParent().trim().equals(pathFolder.trim())) {
                listImageByFolder.add(tmp);
            }
        }
        listImageAdapter.notifyDataSetChanged();
    }

    @Override
    public void selectedItem(Object obj, int code) {
        switch (code) {
            case CodeSelectedItem.CODE_SELECT_FOLDER:
                MyFolderModel folderModel = (MyFolderModel) obj;
                folderModel.setSelected(false);
                for (MyFolderModel t : listFolder) {
                    t.setSelected(false);
                }
                int indexFolder = listFolder.indexOf(folderModel);
                if (indexFolder != -1) {
                    folderModel.setSelected(true);
                    listFolder.set(indexFolder, folderModel);
                    getAllImageFromPathFolder(listFolder.get(indexFolder).getPathFolder());
                }
                break;
            case CodeSelectedItem.CODE_EDIT:
                EditImageActivity.startInternt(this, ((MyImageModel) obj).getPathImage(), binding.getRoot(), false);
                break;
            case CodeSelectedItem.CODE_SELECT:
                ShowImageFullScreenActivity.startIntent(this, (MyImageModel) obj);
                break;
        }
    }

    @Override
    public void selectedItem(Object obj, int code, int p) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EditImageActivity.REQUEST_CODE_EDIT_IMAGE && requestCode == Activity.RESULT_OK && data != null) {
            String pathNew = data.getStringExtra(EditImageActivity.PATH_IMAGE_NEW);
            if (!pathNew.isEmpty()) {
                File file = new File(pathNew);
                if (file.exists()) {
                    MyImageModel tmp = new MyImageModel();
                    tmp.setPathImage(pathNew);
                    tmp.setPathParent(file.getParent());
                    tmp.setSelected(false);
                    listImage.add(tmp);
                    getAllImageFromPathFolder(tmp.getPathParent());
                }
            }
        }
    }
}
