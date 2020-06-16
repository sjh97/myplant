package com.jh571121692developer.myplant.DiaryFlipView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.jh571121692developer.myplant.R;
import com.jh571121692developer.myplant.Utils.ColorPickerDialog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class CustomDiaryDialogFragment extends DialogFragment {

    public static final String TAG_EVENT_DIALOG = "dialog_event";

    private TextView mTitleView, mButtonView;
    private OnPositiveListener mOnPositiveListener;
    CharSequence mTitle, mButton;
    ImageView addDatePickerView, addGalleryView, addColorView;
    TextView addDiaryDate;
    TextInputLayout addDiaryDetailWrapper;
    Uri fileUri;
    int backgroundColor;
    private final static int REQUEST_CODE = 1002;//******

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_custom_diary_promptdialog, container);
        addDatePickerView = view.findViewById(R.id.add_diaryDatePicker);
        addGalleryView = view.findViewById(R.id.add_diaryGallery);
        addDiaryDate = view.findViewById(R.id.add_diaryDate);
        addDiaryDetailWrapper = view.findViewById(R.id.add_diaryDetailTextViewWrapper);
        addColorView = view.findViewById(R.id.add_diaryColor);

        mTitleView = view.findViewById(R.id.tvTitle);
        mButtonView = view.findViewById(R.id.btnPositive);
        mTitleView.setText(mTitle);
        mButtonView.setText(mButton);
        mButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnPositiveListener != null){
                    mOnPositiveListener.onClick(CustomDiaryDialogFragment.this);
                }
            }
        });

        addColorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialog colorPickerDialog = new ColorPickerDialog();
                colorPickerDialog.setPositiveListener("OK", new ColorPickerDialog.OnPositiveListener() {
                    @Override
                    public void onClick(ColorPickerDialog dialog, int color) {
                        addColorView.setBackgroundColor(color);
                        backgroundColor = color;
                        dialog.dismiss();
                    }
                }).show(getFragmentManager(),ColorPickerDialog.COLOR_PICKER_TAG);
            }
        });

        SimpleDateFormat dateFormat = new SimpleDateFormat("YY.MM.DD");

        addDiaryDate.setText(dateFormat.format(new Date()));

        addGalleryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE);

            }
        });
        return view;
    }

    public TextView getAddDiaryDate() {
        return addDiaryDate;
    }

    public TextInputLayout getAddDiaryDetailWrapper() {
        return addDiaryDetailWrapper;
    }

    public Uri getFileUri() {
        return fileUri;
    }

    public int getBackgroundColor(){
        return backgroundColor;
    }

    public CustomDiaryDialogFragment setTitleText(CharSequence title) {
        mTitle = title;
        return this;
    }

    public CustomDiaryDialogFragment setPositiveListener(CharSequence btnText, OnPositiveListener l){
        this.mButton = btnText;
        this.mOnPositiveListener = l;
        return this;
    }

    public interface OnPositiveListener {
        void onClick(CustomDiaryDialogFragment dialogFragment);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("fileUri", requestCode + "");
        if(requestCode == REQUEST_CODE){
            fileUri = data.getData();
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), fileUri);
            } catch (IOException e) {
                bitmap = null;
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        //https://wooooooak.github.io/android/2019/11/23/dialogFragment%EC%82%AC%EC%9D%B4%EC%A6%88%EB%B3%80%EA%B2%BD/
        //resize를 할 시에는 반드시 onResume에서 하자!
        super.onResume();
        resizeDialog();
    }

    private void resizeDialog() {
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = (int)(getScreenSize(getContext()).x * 0.7);
        getDialog().getWindow().setAttributes(params);
    }

    public static Point getScreenSize(Context context) {
        //https://wooooooak.github.io/android/2019/11/23/dialogFragment%EC%82%AC%EC%9D%B4%EC%A6%88%EB%B3%80%EA%B2%BD/
        Point point = new Point();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getSize(point);
        return point;
    }
}
