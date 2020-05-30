package com.jh571121692developer.myplant;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alexvasilkov.android.commons.ui.Views;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.jh571121692developer.myplant.PaintingUtils.Painting;
import com.jh571121692developer.myplant.Utils.ClassShared;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class addDetailsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private TextInputEditText yearTextView;
    private TextInputEditText nameTextView;
    private TextInputEditText locationTextView;
    private SimpleDateFormat simpleDateFormat;
    private TextView addImageTextView;
    private PermissionListener permissionListener;
    private String imageUri = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_details);

        //-------------------------------------------------------
        permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(addDetailsActivity.this, "권한 허가", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(addDetailsActivity.this, "권한 거부\n" + deniedPermissions.toString()
                        , Toast.LENGTH_LONG).show();
            }
        };

        TedPermission.with(addDetailsActivity.this)
                .setPermissionListener(permissionListener)
                .setDeniedMessage("권한이 거부되었습니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용해주세요.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();

        //---------------------------------------------------

        yearTextView = Views.find(this, R.id.yearTextView);
        nameTextView = Views.find(this, R.id.nameTextView);
        locationTextView = Views.find(this, R.id.locationTextView);
        simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
        addImageTextView = Views.find(this, R.id.addList_image);

        TextInputLayout layout = Views.find(this, R.id.layout3);
        layout.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate(2020,4,28,R.style.NumberPickerStyle);
            }
        });

        Button saveButton = Views.find(this, R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassShared classShared = new ClassShared(addDetailsActivity.this);
                ArrayList<Painting> paintingArrayList = classShared.getPaintingArray(getString(R.string.detailsKey));
                int imageId = getResources().getIdentifier("@drawable/ic_launcher_background","drawable", getPackageName());
                String title = nameTextView.getText().toString();
                String year = yearTextView.getText().toString();
                String loction = locationTextView.getText().toString();
                Log.d("croping", "image uri is : " + imageUri);
                Painting painting = new Painting(imageId, title, year, loction, imageUri);
                paintingArrayList.add(painting);
                classShared.putPaintingArray(paintingArrayList, getString(R.string.detailsKey));
                startActivity(new Intent(addDetailsActivity.this, UnfoldableDetailsActivity.class));
                finish();
            }
        });

        addImageTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //https://github.com/ArthurHub/Android-Image-Cropper
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(addDetailsActivity.this);
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        yearTextView.setText(simpleDateFormat.format(calendar.getTime()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                Uri fileUri = result.getUri();
                ImageView preImageView = Views.find(addDetailsActivity.this, R.id.addList_preview);
                imageUri = fileUri.toString();
                Glide.with(addDetailsActivity.this).load(fileUri).into(preImageView);
            }
            else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
                Log.e("croping", error.toString());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void showDate(int year, int monthOfYear, int dayOfMonth, int spinnerTheme){
        new SpinnerDatePickerDialogBuilder()
                .context(addDetailsActivity.this)
                .callback(addDetailsActivity.this)
                .spinnerTheme(spinnerTheme)
                .showTitle(true)
                .showDaySpinner(true)
                .defaultDate(year,monthOfYear,dayOfMonth)
                .build()
                .show();
    }
}