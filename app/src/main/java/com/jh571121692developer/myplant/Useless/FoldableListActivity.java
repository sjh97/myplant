package com.jh571121692developer.myplant.Useless;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.alexvasilkov.android.commons.ui.Views;
import com.alexvasilkov.foldablelayout.FoldableListLayout;
import com.jh571121692developer.myplant.PaintingUtils.PaintingsAdapter;
import com.jh571121692developer.myplant.R;

public class FoldableListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foldable_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FoldableListLayout foldableListLayout = Views.find(this, R.id.foldable_list);
        foldableListLayout.setAdapter(new PaintingsAdapter(this));
    }
}
