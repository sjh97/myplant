package com.jh571121692developer.myplant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alexvasilkov.android.commons.ui.Views;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.jh571121692developer.myplant.PaintingUtils.Painting;
import com.jh571121692developer.myplant.Utils.ClassShared;
import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class TestActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private TextInputEditText yearTextView;
    private TextInputEditText nameTextView;
    private TextInputEditText locationTextView;
    private SimpleDateFormat simpleDateFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        yearTextView = Views.find(this, R.id.yearTextView);
        nameTextView = Views.find(this, R.id.nameTextView);
        locationTextView = Views.find(this, R.id.locationTextView);
        simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);

        TextInputLayout layout = Views.find(this, R.id.layout3);
        layout.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate(2020,4,28,R.style.NumberPickerStyle);
            }
        });

        Button button = Views.find(this, R.id.saveButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassShared classShared = new ClassShared(TestActivity.this);
                ArrayList<Painting> paintingArrayList = classShared.getPaintingArray(getString(R.string.detailsKey));
                int imageId = getResources().getIdentifier("@drawable/ic_launcher_background","drawable", getPackageName());
                String title = nameTextView.getText().toString();
                String year = yearTextView.getText().toString();
                String loction = locationTextView.getText().toString();
                Painting painting = new Painting(imageId, title, year, loction);
                paintingArrayList.add(painting);
                classShared.putPaintingArray(paintingArrayList, getString(R.string.detailsKey));
                startActivity(new Intent(TestActivity.this, UnfoldableDetailsActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        yearTextView.setText(simpleDateFormat.format(calendar.getTime()));
    }

    public void showDate(int year, int monthOfYear, int dayOfMonth, int spinnerTheme){
        new SpinnerDatePickerDialogBuilder()
                .context(TestActivity.this)
                .callback(TestActivity.this)
                .spinnerTheme(spinnerTheme)
                .showTitle(true)
                .showDaySpinner(true)
                .defaultDate(year,monthOfYear,dayOfMonth)
                .build()
                .show();
    }
}
