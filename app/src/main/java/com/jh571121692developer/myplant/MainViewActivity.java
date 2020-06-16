package com.jh571121692developer.myplant;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.jh571121692developer.myplant.DiscreteScrollView_Utils.DetailData;
import com.jh571121692developer.myplant.DiscreteScrollView_Utils.DiscreteScrollAdapter;
import com.jh571121692developer.myplant.Utils.ClassShared;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;

public class MainViewActivity extends AppCompatActivity implements DiscreteScrollView.OnItemChangedListener{

    private DiscreteScrollView scrollView;
    String detailsKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);
        detailsKey = getString(R.string.detailsKey);

        ArrayList<DetailData> detailDataList = new ClassShared(MainViewActivity.this).getDetailArray(detailsKey);
        //마지막에 항상 더하기 버튼을 눌러주기 위함임
        detailDataList.add(new DetailData().setLastDetail("initial"));

        scrollView = findViewById(R.id.discreteScrollView);
        scrollView.setAdapter(new DiscreteScrollAdapter(MainViewActivity.this, detailDataList));
        scrollView.setItemTransitionTimeMillis(250);
        scrollView.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());
    }

    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int position) {

    }
}
