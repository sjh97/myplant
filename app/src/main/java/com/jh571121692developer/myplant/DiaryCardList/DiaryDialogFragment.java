package com.jh571121692developer.myplant.DiaryCardList;

import android.content.Context;
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

import com.google.android.material.textfield.TextInputLayout;
import com.jh571121692developer.myplant.R;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DiaryDialogFragment extends DialogFragment {

    public static final String TAG_EVENT_DIALOG = "dialog_event";

    private TextView mTitleView, mButtonView;
    private OnPositiveListener mOnPositiveListener;
    CharSequence mTitle, mButton;
    ImageView addDatePickerView, addGalleryView;
    TextView addDiaryDate;
    TextInputLayout addDiaryDetailWrapper;
    private final static int REQUEST_CODE = 1002;//******

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_custom_diary_promptdialog, container);
        addDatePickerView = view.findViewById(R.id.add_diaryDatePicker);
        addGalleryView = view.findViewById(R.id.add_diaryGallery);
        addDiaryDate = view.findViewById(R.id.add_diaryDate);
        addDiaryDetailWrapper = view.findViewById(R.id.add_diaryDetailTextViewWrapper);

        mTitleView = view.findViewById(R.id.tvTitle);
        mButtonView = view.findViewById(R.id.btnPositive);
        mTitleView.setText(mTitle);
        mButtonView.setText(mButton);
        mButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnPositiveListener != null){
                    mOnPositiveListener.onClick(DiaryDialogFragment.this);
                }
            }
        });

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

    public DiaryDialogFragment setTitleText(CharSequence title) {
        mTitle = title;
        return this;
    }

    public DiaryDialogFragment setPositiveListener(CharSequence btnText, OnPositiveListener l){
        this.mButton = btnText;
        this.mOnPositiveListener = l;
        return this;
    }

    public interface OnPositiveListener {
        void onClick(DiaryDialogFragment dialogFragment);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("fileUri", requestCode + "");
        if(requestCode == REQUEST_CODE){
            Uri fileUri = data.getData();
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
