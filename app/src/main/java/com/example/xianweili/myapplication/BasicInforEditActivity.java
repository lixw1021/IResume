package com.example.xianweili.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.xianweili.myapplication.R;
import com.example.xianweili.myapplication.model.BasicInfo;
import com.example.xianweili.myapplication.util.ImageUtils;
import com.example.xianweili.myapplication.util.PermissionUtils;


/**
 * Created by xianwei li on 4/13/2017.
 */

public class BasicInforEditActivity extends AppCompatActivity {
    public static final String KEY_BASIC_INFORMATION = "basic information";
    private static final int RESULT_LOAD_IMAGE = 1 ;
    private BasicInfo basicInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_basic_info_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        basicInfo = getIntent().getParcelableExtra(KEY_BASIC_INFORMATION);
        setupUIForEdit(basicInfo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.ic_save:
                saveAndExit();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK){
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                showImage(selectedImageUri);
            }
        }
    }

    private void showImage(Uri selectedImageUri) {
        ImageView imageView = (ImageView)findViewById(R.id.basic_info_edit_image);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        imageView.setTag(selectedImageUri);
        ImageUtils.loadImage(this, selectedImageUri,imageView);
    }

    protected void setupUIForEdit(BasicInfo data){
        ((EditText)findViewById(R.id.basic_info_edit_name)).setText(basicInfo.name);
        ((EditText)findViewById(R.id.basic_info_edit_email)).setText(basicInfo.email);

        if (data.imageUri != null) {
            showImage(data.imageUri);
        }

        findViewById(R.id.basic_info_edit_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PermissionUtils.checkPermission(BasicInforEditActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                    PermissionUtils.requesReadExternalStoragePermission(BasicInforEditActivity.this);
                } else {
                    pickImage();
                }
            }
        });
    }

    protected void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select picture"), RESULT_LOAD_IMAGE);
    }

    private void saveAndExit() {
        if (basicInfo == null) {
            basicInfo = new BasicInfo();
        }
        basicInfo.name = ((EditText)findViewById(R.id.basic_info_edit_name)).getText().toString();
        basicInfo.email = ((EditText)findViewById(R.id.basic_info_edit_email)).getText().toString();
        basicInfo.imageUri = (Uri)findViewById(R.id.basic_info_edit_image).getTag();

        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_BASIC_INFORMATION,basicInfo);
        setResult(Activity.RESULT_OK, resultIntent);
    }
}
