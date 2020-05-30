package com.jh571121692developer.myplant;

import androidx.appcompat.app.AppCompatActivity;
import cn.refactor.lib.colordialog.ColorDialog;
import cn.refactor.lib.colordialog.PromptDialog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alexvasilkov.android.commons.texts.SpannableBuilder;
import com.alexvasilkov.android.commons.ui.Views;
import com.alexvasilkov.foldablelayout.UnfoldableView;
import com.alexvasilkov.foldablelayout.shading.GlanceFoldShading;
import com.github.zagum.switchicon.SwitchIconView;
import com.jh571121692developer.myplant.CustomPromptDialog.CustomPromptDialog;
import com.jh571121692developer.myplant.PaintingUtils.Painting;
import com.jh571121692developer.myplant.PaintingUtils.PaintingsAdapter;
import com.jh571121692developer.myplant.Utils.GlideHelper;
import com.jh571121692developer.myplant.Utils.IconClickSetter;

public class UnfoldableDetailsActivity extends AppCompatActivity {

    private View listTouchInterceptor;
    private View detailsLayout;
    private UnfoldableView unfoldableView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unfoldable_details);

//        ArrayList<Painting> paintingArrayList = new ArrayList<>();
//        int imageId = getResources().getIdentifier("@drawable/ic_launcher_background","drawable", getPackageName());
//        String title = "title";
//        String year = "year";
//        String loction = "location";
//        Painting painting = new Painting(imageId, title, year, loction);
//        paintingArrayList.add(painting);
//        ClassShared classShared = new ClassShared(this);
//        classShared.putPaintingArray(paintingArrayList, getString(R.string.detailsKey));

        final ListView listView = Views.find(this, R.id.list_view);
        listView.setAdapter(new PaintingsAdapter(this));

//        ImageView imageView = Views.find(this, R.id.add_view_item_image);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int imageId = getResources().getIdentifier("@drawable/ic_launcher_background","drawable", getPackageName());
//                String title = "title";
//                String year = "year";
//                String loction = "location";
//                Painting painting = new Painting(imageId, title, year, loction);
//                openDetails(view, painting);
//            }
//        });

        FrameLayout frameLayout = Views.find(this, R.id.add_list_view);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UnfoldableDetailsActivity.this, addDetailsActivity.class));
            }
        });

        listTouchInterceptor = Views.find(this, R.id.touch_interceptor_view);
        listTouchInterceptor.setClickable(false);

        detailsLayout = Views.find(this, R.id.details_layout);
        detailsLayout.setVisibility(View.INVISIBLE);

        unfoldableView = Views.find(this, R.id.unfoldable_view);

        Bitmap glance = BitmapFactory.decodeResource(getResources(), R.drawable.unfold_glance);
        unfoldableView.setFoldShading(new GlanceFoldShading(glance));

        unfoldableView.setOnFoldingListener(new UnfoldableView.SimpleFoldingListener(){
            @Override
            public void onUnfolding(UnfoldableView unfoldableView) {
                listTouchInterceptor.setClickable(true);
                detailsLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onUnfolded(UnfoldableView unfoldableView) {
                listTouchInterceptor.setClickable(false);
            }

            @Override
            public void onFoldingBack(UnfoldableView unfoldableView) {
                listTouchInterceptor.setClickable(true);
            }

            @Override
            public void onFoldedBack(UnfoldableView unfoldableView) {
                listTouchInterceptor.setClickable(false);
                detailsLayout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFoldProgress(UnfoldableView unfoldableView, float progress) {
                super.onFoldProgress(unfoldableView, progress);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(unfoldableView != null
        && (unfoldableView.isUnfolded() || unfoldableView.isUnfolding())){
            unfoldableView.foldBack();
        }else{
            super.onBackPressed();
        }
    }

    public void openDetails(View coverView, Painting painting){
        final ImageView image = Views.find(detailsLayout, R.id.details_image);
        final TextView title = Views.find(detailsLayout, R.id.details_title);
        final TextView description = Views.find(detailsLayout, R.id.details_text);

        GlideHelper.loadPaintingImage(image, painting);
        title.setText(painting.getTitle());

        SpannableBuilder builder = new SpannableBuilder(this);
        builder
                .createStyle().setFont(Typeface.DEFAULT_BOLD).apply()
                .append(R.string.year).append(": ")
                .clearStyle()
                .append(painting.getYear()).append("\n")
                .createStyle().setFont(Typeface.DEFAULT_BOLD).apply()
                .append(R.string.location).append(" : ")
                .clearStyle()
                .append(painting.getLocation());
        description.setText(builder.build());

        IconClickSetter iconClickSetter = new IconClickSetter(UnfoldableDetailsActivity.this);

        final SwitchIconView sunIconView = Views.find(this, R.id.sunButton);

        final SwitchIconView waterIconView = Views.find(this, R.id.waterButton);

        final SwitchIconView fertilizerIconView = Views.find(this, R.id.fertilizerButton);

        // 각각의 아이콘마다 들어가는 내용만 다를뿐 기능상의 차이가 없기 때문에 class를 만들어서 간단하게 표현하였다.
        iconClickSetter.setAllClickListener(sunIconView, "햇볕 쬐기", R.drawable.ic_sun);
        iconClickSetter.setAllClickListener(waterIconView, "물주기", R.drawable.ic_water);
        iconClickSetter.setAllClickListener(fertilizerIconView, "영양제 주기", R.drawable.ic_fertilizer);


        unfoldableView.unfold(coverView, detailsLayout);
    }



}
