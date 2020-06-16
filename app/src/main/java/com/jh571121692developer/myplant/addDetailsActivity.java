package com.jh571121692developer.myplant;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.alexvasilkov.android.commons.ui.Views;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.jh571121692developer.myplant.AlarmManager.DeviceBootReceiver;
import com.jh571121692developer.myplant.DiscreteScrollView_Utils.DetailData;
import com.jh571121692developer.myplant.Utils.ClassShared;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private Button saveButton;
    private TimePicker picker;
    private String detailsKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_details);
        detailsKey = getString(R.string.detailsKey);

        //-------------------------------------------------------
        permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
//                Toast.makeText(addDetailsActivity.this, "권한 허가", Toast.LENGTH_LONG).show();
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

        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour, hour_24, minute;
                String am_pm;
                if(Build.VERSION.SDK_INT >= 23){
                    hour_24 = picker.getHour();
                    minute = picker.getMinute();
                }
                else{
                    hour_24 = picker.getCurrentHour();
                    minute = picker.getCurrentMinute();
                }
                if(hour_24 > 12){
                    am_pm = "PM";
                    hour = hour_24 - 12;
                }
                else {
                    hour = hour_24;
                    am_pm = "AM";
                }

                //현재 지정된 시간으로 알람 시간 설정
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, hour_24);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);

                //이미 지난 시간을 지정했다면 다음날 같은 시간으로 설정
                if(calendar.before(Calendar.getInstance())){
                    calendar.add(Calendar.DATE, 1);
                }

                Date currentDateTime = calendar.getTime();
                String date_text = new SimpleDateFormat("yyyy년 MM월 dd일 EE요일 a hh시 mm분 ", Locale.getDefault()).format(currentDateTime);
                Toast.makeText(getApplicationContext(),date_text + "으로 알람이 설정되었습니다!", Toast.LENGTH_SHORT).show();

                //Preference에 설정한 값 저장
                SharedPreferences.Editor editor = getSharedPreferences("daily alarm", MODE_PRIVATE).edit();
                editor.putLong("nextNotifyTime", (long) calendar.getTimeInMillis());
                editor.apply();

                diaryNotification(calendar);
                //------------------------------------------------------
                ArrayList<DetailData> detailDataList = new ArrayList<>();
                detailDataList.add(new DetailData(nameTextView.getText().toString(), yearTextView.getText().toString(),
                        imageUri, false, false, false));
                detailDataList.addAll(new ClassShared(addDetailsActivity.this).getDetailArray(detailsKey));
                new ClassShared(addDetailsActivity.this).putDetailArray(detailDataList, detailsKey);
                //--------------------------------------------------------
                startActivity(new Intent(addDetailsActivity.this, MainViewActivity.class));
                finish();

            }
        });

        //--------------------------------------------------------------------
        picker = Views.find(this, R.id.timePicker);
        picker.setIs24HourView(true);

        //앞서 설정한 값으로 보여주기
        SharedPreferences sharedPreferences = getSharedPreferences("daily alarm", MODE_PRIVATE);
        final long millis = sharedPreferences.getLong("nextNotifyTime", Calendar.getInstance().getTimeInMillis());

        Calendar nextNotifyTime = new GregorianCalendar();
        nextNotifyTime.setTimeInMillis(millis);

        Date nextDate = nextNotifyTime.getTime();
        String date_text = new SimpleDateFormat("yyyy년 MM월 dd일 EE요일 a hh시 mm분 ", Locale.getDefault()).format(nextDate);
        Toast.makeText(getApplicationContext(),"[처음 실행시] 다음 알람은 " + date_text + "으로 알람이 설정되었습니다!", Toast.LENGTH_SHORT).show();

        //이전 설정값으로 TimePicker 초기화
        Date currentTime = nextNotifyTime.getTime();
        SimpleDateFormat HourFormat = new SimpleDateFormat("kk", Locale.getDefault());
        SimpleDateFormat MinuteFormat = new SimpleDateFormat("mm", Locale.getDefault());

        int pre_hour = Integer.parseInt(HourFormat.format(currentTime));
        int pre_minute = Integer.parseInt(MinuteFormat.format(currentTime));

        if(Build.VERSION.SDK_INT >= 23){
            picker.setHour(pre_hour);
            picker.setMinute(pre_minute);
        }
        else {
            picker.setCurrentHour(pre_hour);
            picker.setCurrentMinute(pre_minute);
        }

//        Button saveButton = Views.find(this, R.id.saveButton);
//        saveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                int hour, hour_24, minute;
//                String am_pm;
//                if(Build.VERSION.SDK_INT >= 23){
//                    hour_24 = picker.getHour();
//                    minute = picker.getMinute();
//                }
//                else{
//                    hour_24 = picker.getCurrentHour();
//                    minute = picker.getCurrentMinute();
//                }
//                if(hour_24 > 12){
//                    am_pm = "PM";
//                    hour = hour_24 - 12;
//                }
//                else {
//                    hour = hour_24;
//                    am_pm = "AM";
//                }
//
//                //현재 지정된 시간으로 알람 시간 설정
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTimeInMillis(System.currentTimeMillis());
//                calendar.set(Calendar.HOUR_OF_DAY, hour_24);
//                calendar.set(Calendar.MINUTE, minute);
//                calendar.set(Calendar.SECOND, 0);
//
//                //이미 지난 시간을 지정했다면 다음날 같은 시간으로 설정
//                if(calendar.before(Calendar.getInstance())){
//                    calendar.add(Calendar.DATE, 1);
//                }
//
//                Date currentDateTime = calendar.getTime();
//                String date_text = new SimpleDateFormat("yyyy년 MM월 dd일 EE요일 a hh시 mm분 ", Locale.getDefault()).format(currentDateTime);
//                Toast.makeText(getApplicationContext(),date_text + "으로 알람이 설정되었습니다!", Toast.LENGTH_SHORT).show();
//
//                //Preference에 설정한 값 저장
//                SharedPreferences.Editor editor = getSharedPreferences("daily alarm", MODE_PRIVATE).edit();
//                editor.putLong("nextNotifyTime", (long) calendar.getTimeInMillis());
//                editor.apply();
//
//                diaryNotification(calendar);
//
//
//                //----------------------------------------
//                ClassShared classShared = new ClassShared(addDetailsActivity.this);
//                ArrayList<Painting> paintingArrayList = classShared.getPaintingArray(getString(R.string.detailsKey));
//                int imageId = getResources().getIdentifier("@drawable/ic_launcher_background","drawable", getPackageName());
//                String title = nameTextView.getText().toString();
//                String year = yearTextView.getText().toString();
//                String loction = locationTextView.getText().toString();
//                Log.d("croping", "image uri is : " + imageUri);
//                Painting painting = new Painting(imageId, title, year, loction, imageUri);
//                paintingArrayList.add(painting);
//                classShared.putPaintingArray(paintingArrayList, getString(R.string.detailsKey));
//                startActivity(new Intent(addDetailsActivity.this, UnfoldableDetailsActivity.class));
//                finish();
//            }
//
//        });

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

    void diaryNotification(Calendar calendar){
        Boolean dailyNotify = true;//무조건 알람을 사용

        PackageManager pm = this.getPackageManager();
        ComponentName receiver = new ComponentName(this, DeviceBootReceiver.class);
        Intent alarmIntent = new Intent(this, AlarmManager.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // 사용자가 매일 알람을 허용했다면
        if(dailyNotify){
            if(alarmManager != null){
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pendingIntent);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }
            }

            //부팅 후 실행되는 리시버 사용가능하게 설정
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
                    PackageManager.DONT_KILL_APP);
        }
    }
}