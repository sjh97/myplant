package com.jh571121692developer.myplant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alexvasilkov.android.commons.ui.Views;
import com.bumptech.glide.Glide;
import com.github.zagum.switchicon.SwitchIconView;
import com.google.android.material.textfield.TextInputEditText;
import com.jh571121692developer.myplant.DiaryFlipView.CustomDiary;
import com.jh571121692developer.myplant.DiaryFlipView.CustomDiaryDialogFragment;
import com.jh571121692developer.myplant.DiscreteScrollView_Utils.DetailData;
import com.jh571121692developer.myplant.Utils.ClassShared;
import com.jh571121692developer.myplant.Utils.IconClickSetter;
import com.yalantis.flipviewpager.adapter.BaseFlipAdapter;
import com.yalantis.flipviewpager.utils.FlipSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {


    private ListView diaryListView;
    private TextView detailsNameView;
    private String detailkey;
    private String diaryKey;
    ImageView detailsImageView;
    private CustomDiaryAdapter customDiaryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        detailkey = getString(R.string.detailsKey);

        DetailData detailData = (DetailData) getIntent().getParcelableExtra(detailkey);
        int position = getIntent().getIntExtra("position",0);

        diaryKey = getString(R.string.diaryKey) + position;

        detailsNameView = findViewById(R.id.details_name);
        detailsNameView.setText(detailData.getName());
        detailsImageView = findViewById(R.id.details_image);
        Glide.with(this).load(Uri.parse(detailData.getImageUri()))
                .into(detailsImageView);

        IconClickSetter iconClickSetter = new IconClickSetter(DetailsActivity.this, detailData, position);

        final SwitchIconView sunIconView = Views.find(this, R.id.sunButton1);
        sunIconView.setIconEnabled(detailData.getSunIcon());

        final SwitchIconView waterIconView = Views.find(this, R.id.waterButton1);
        waterIconView.setIconEnabled(detailData.getWaterIcon());

        final SwitchIconView fertilizerIconView = Views.find(this, R.id.fertilizerButton1);
        fertilizerIconView.setIconEnabled(detailData.getFertilIcon());

        // 각각의 아이콘마다 들어가는 내용만 다를뿐 기능상의 차이가 없기 때문에 class를 만들어서 간단하게 표현하였다.
        iconClickSetter.setAllClickListener(sunIconView, "햇볕 쬐기", R.drawable.ic_sun);
        iconClickSetter.setAllClickListener(waterIconView, "물주기", R.drawable.ic_water);
        iconClickSetter.setAllClickListener(fertilizerIconView, "영양제 주기", R.drawable.ic_fertilizer);

        diaryListView = findViewById(R.id.diarys);
        FlipSettings settings = new FlipSettings.Builder().defaultPage(1).build();
        ArrayList<CustomDiary> customDiaryArrayList = new ClassShared(this).getCustomDiaryArray(diaryKey);
//        customDiaryArrayList.add(new CustomDiary(R.drawable.attractions, "", R.color.pantoneraWheat, "", Arrays.asList("","","")));
        customDiaryAdapter = new CustomDiaryAdapter(this, customDiaryArrayList, settings);
        diaryListView.setAdapter(customDiaryAdapter);

        //---------------------------------------
        ImageView addDiaryDialogView = findViewById(R.id.importDiaryDialogView);
        addDiaryDialogView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDiaryDialogFragment diaryDialogFragment = new CustomDiaryDialogFragment();
                diaryDialogFragment
//                        .setTitleText("일기를 써보자!")
                        .setPositiveListener("OK", new CustomDiaryDialogFragment.OnPositiveListener() {
                            @Override
                            public void onClick(CustomDiaryDialogFragment dialogFragment) {
                                String date = dialogFragment.getAddDiaryDate().getText().toString();
                                String Uri = dialogFragment.getFileUri().toString();
                                String text = dialogFragment.getAddDiaryDetailWrapper().getEditText().getText().toString();
                                int backgroundColor = dialogFragment.getBackgroundColor();

                                ArrayList<CustomDiary> customDiaryArrayList = new ClassShared(DetailsActivity.this).getCustomDiaryArray(diaryKey);

                                customDiaryArrayList.add(new CustomDiary(Uri, date, backgroundColor, text));
                                new ClassShared(DetailsActivity.this).putDiaryArray(customDiaryArrayList, diaryKey);
                                customDiaryAdapter.notifyDataSetChanged();
                                dialogFragment.dismiss();
                            }
                        }).show(getSupportFragmentManager(),CustomDiaryDialogFragment.TAG_EVENT_DIALOG);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        customDiaryAdapter.notifyDataSetChanged();
    }

    class CustomDiaryAdapter extends BaseFlipAdapter {
        private final int PAGES = 3;
        private int[] IDS_INTEREST = {R.id.interest_1, R.id.interest_2, R.id.interest_3};
        private Context context;

        public CustomDiaryAdapter(Context context, List<CustomDiary> items, FlipSettings settings){
            super(context, items, settings);
            this.context = context;
        }

        @Override
        public View getPage(int position, View convertView, ViewGroup parent, Object diary1, Object diary2) {
            final DiaryHolder holder;

            if(convertView == null){
                holder = new DiaryHolder();
                convertView = getLayoutInflater().inflate(R.layout.diary_merge_page, parent, false);
                holder.leftAvatar = convertView.findViewById(R.id.first);
                holder.rightAvatar = convertView.findViewById(R.id.second);

                holder.infoPage = getLayoutInflater().inflate(R.layout.custom_diary_info, parent, false);
                holder.diaryDate = holder.infoPage.findViewById(R.id.diaryDate);
                holder.diaryDetailView = holder.infoPage.findViewById(R.id.diaryDetailTextView);

                for(int id : IDS_INTEREST)
                    holder.interests.add((EditText) holder.infoPage.findViewById(id));

                convertView.setTag(holder);
            }else{
                holder = (DiaryHolder) convertView.getTag();
            }

            switch (position){
                case 1 :
//                    holder.leftAvatar.setImageResource(((CustomDiary) diary1).getAvatar());
                    Glide.with(convertView.getContext())
                            .load(Uri.parse(((CustomDiary) diary1).getAvatarUri()))
                            .into(holder.leftAvatar);
//                    holder.leftAvatar.setImageURI(Uri.parse(((CustomDiary) diary1).getAvatarUri()));
                    if(diary2 != null){
//                        holder.rightAvatar.setImageURI(Uri.parse(((CustomDiary) diary2).getAvatarUri()));
                        Glide.with(convertView.getContext())
                                .load(Uri.parse(((CustomDiary) diary2).getAvatarUri()))
                                .into(holder.rightAvatar);
//                        holder.rightAvatar.setImageResource(((CustomDiary) diary2).getAvatar());
                    }
                    break;
                default :
                    fillHolder(holder, position == 0 ? (CustomDiary) diary1 : (CustomDiary) diary2);
                    holder.infoPage.setTag(holder);
                    return holder.infoPage;
            }
            return convertView;
        }

        @Override
        public int getPagesCount() {
            return PAGES;
        }
    }

    private void fillHolder(DiaryHolder holder, CustomDiary diary){
        if(diary == null)
            return;
        holder.infoPage.setBackgroundColor(diary.getBackground());
//        holder.infoPage.setBackgroundColor(getColor(diary.getBackground()));
        holder.diaryDate.setText(diary.getDate());
        holder.diaryDetailView.setText(diary.getDiaryText());
    }

    class DiaryHolder{
        ImageView leftAvatar;
        ImageView rightAvatar;
        View infoPage;

        List<EditText> interests = new ArrayList<>();
        TextView diaryDate;
        TextInputEditText diaryDetailView;
    }

//    class DiaryAdapter extends BaseFlipAdapter {
//        private final int PAGES = 3;
//        private int[] IDS_INTEREST = {R.id.interest_1, R.id.interest_2, R.id.interest_3,
//                R.id.interest_4, R.id.interest_5};
//
//        public DiaryAdapter(Context context, List<Diary> items, FlipSettings settings){
//            super(context, items, settings);
//        }
//
//        @Override
//        public View getPage(int position, View convertView, ViewGroup parent, Object diary1, Object diary2) {
//            final DiaryHolder holder;
//
//            if(convertView == null){
//                holder = new DiaryHolder();
//                convertView = getLayoutInflater().inflate(R.layout.diary_merge_page,
//                        parent, false);
//                holder.leftAvatar = convertView.findViewById(R.id.first);
//                holder.rightAvatar = convertView.findViewById(R.id.second);
//                holder.infoPage = getLayoutInflater().inflate(R.layout.diary_info, parent,
//                        false);
//                holder.nickname = holder.infoPage.findViewById(R.id.nickname);
//
//                for(int id : IDS_INTEREST)
//                    holder.interests.add((TextView) holder.infoPage.findViewById(id));
//
//                convertView.setTag(holder);
//            }else{
//                holder = (DiaryHolder) convertView.getTag();
//            }
//
//            switch (position){
//                case 1 :
//                    holder.leftAvatar.setImageResource(((Diary) diary1).getAvatar());
//                    if(diary2 != null){
//                        holder.rightAvatar.setImageResource(((Diary) diary2).getAvatar());
//                    }
//                    break;
//                default :
//                    fillHolder(holder, position == 0 ? (Diary) diary1 : (Diary) diary2);
//                    holder.infoPage.setTag(holder);
//                    return holder.infoPage;
//            }
//            return convertView;
//        }
//
//        @Override
//        public int getPagesCount() {
//            return PAGES;
//        }
//    }
//
//    private void fillHolder(DiaryHolder holder, Diary diary){
//        if(diary == null)
//            return;
//        Iterator<TextView> iviews = holder.interests.iterator();
//        Iterator<String> iInterests = diary.getInterests().iterator();
//        while (iviews.hasNext() && iInterests.hasNext()){
//            iviews.next().setText(iInterests.next());
//        }
//        holder.infoPage.setBackgroundColor(getResources().getColor(diary.getBackground()));
//    }
//
//    class DiaryHolder{
//        ImageView leftAvatar;
//        ImageView rightAvatar;
//        View infoPage;
//
//        List<TextView> interests = new ArrayList<>();
//        TextView nickname;
//    }
}
