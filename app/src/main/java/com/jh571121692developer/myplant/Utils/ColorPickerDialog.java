package com.jh571121692developer.myplant.Utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jh571121692developer.myplant.R;

import org.w3c.dom.Text;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ColorPickerDialog extends DialogFragment {

    Button okButton;
    CharSequence buttonText;
    OnPositiveListener positiveListener;
    RelativeLayout gridLayoutWrapper;
    GridLayout colorGridLayout;
    ImageView colorPickerImageView, searchPreImageView, userColorImageView;
    TextView colorPickerTextView;
    EditText searchEditText;
    int gridColor = Color.parseColor("#000000");
    public final static String COLOR_PICKER_TAG = "COLOR_PICKER_TAG";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.colorpicker_dialog, container);
        okButton = view.findViewById(R.id.okButton);
        gridLayoutWrapper = view.findViewById(R.id.colorPickerGridLayoutWrapper);
        colorGridLayout = view.findViewById(R.id.colorPickerGridLayout);
        colorPickerImageView = view.findViewById(R.id.colorPickerGridLayout_preimageView);
        colorPickerTextView = view.findViewById(R.id.colorPickerGridLayout_preTextView);
        searchEditText = view.findViewById(R.id.colorPickerSearch_EditTextView);
        searchPreImageView = view.findViewById(R.id.colorPickerSearch_preimageView);
        userColorImageView = view.findViewById(R.id.colorPickerGridLayout_userColor);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(positiveListener != null){
                    positiveListener.onClick(ColorPickerDialog.this, gridColor);
                }
            }
        });

        for(int i = 0; i< colorGridLayout.getChildCount(); i++){
            final ImageView imageView = (ImageView) colorGridLayout.getChildAt(i);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gridColor = ((ColorDrawable) imageView.getBackground()).getColor();
                    colorPickerImageView.setBackgroundColor(gridColor);
                    colorPickerTextView.setText("#" + String.format("%06X",(0xFFFFFF & gridColor)));
                }
            });
        }
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String colorHexaCode = "#" + charSequence.toString();
                if(charSequence.toString().length() == 6){
                    searchPreImageView.setBackgroundColor(Color.parseColor(colorHexaCode));
                    userColorImageView.setBackgroundColor(Color.parseColor(colorHexaCode));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    public ColorPickerDialog setPositiveListener(CharSequence btnText, OnPositiveListener l){
        this.buttonText = btnText;
        this.positiveListener = l;
        return this;
    }

    public interface OnPositiveListener{
        void onClick(ColorPickerDialog dialog, int color);
    }

    @Override
    public void onResume() {
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