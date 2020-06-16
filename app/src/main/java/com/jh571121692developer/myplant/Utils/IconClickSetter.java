package com.jh571121692developer.myplant.Utils;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.github.zagum.switchicon.SwitchIconView;
import com.jh571121692developer.myplant.CustomPromptDialog.CustomPromptDialog;
import com.jh571121692developer.myplant.DiscreteScrollView_Utils.DetailData;
import com.jh571121692developer.myplant.R;

import java.util.ArrayList;

import cn.refactor.lib.colordialog.PromptDialog;

public class IconClickSetter {

    Context context;
    DetailData detailData;
    int postion;
    String detailsKey;
    ArrayList<DetailData> detailDataList;

    public IconClickSetter(Context context, DetailData detailData, int postion) {
        this.context = context;
        this.detailData = detailData;
        detailsKey = context.getString(R.string.detailsKey);
        detailDataList = new ClassShared(context).getDetailArray(detailsKey);
        this.postion = postion;
    }

    public void setAllClickListener(final SwitchIconView switchIconView, final String word, final int drawableId){
        switchIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("sibal","get" + switchIconView.getId());
                Log.d("sibal","water" + R.id.waterButton1);

                switch (switchIconView.getId()){
                    case R.id.waterButton1 :
                        switchIconView.switchState();
                        detailData.setWaterIcon(!detailData.getWaterIcon());
                        break;

                    case R.id.sunButton1 :
                        switchIconView.switchState();
                        detailData.setSunIcon(!detailData.getSunIcon());
                        break;

                    case R.id.fertilizerButton1 :
                        switchIconView.switchState();
                        detailData.setFertilIcon(!detailData.getFertilIcon());
                        break;

                    default:
                        switchIconView.switchState();
                        break;
                }

                ArrayList<DetailData> detailDataList = new ClassShared(context).getDetailArray(detailsKey);


                detailDataList.set(postion,detailData);
                new ClassShared(context).putDetailArray(detailDataList,detailsKey);
            }
        });

        switchIconView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final CustomPromptDialog customPromptDialog = new CustomPromptDialog(context, context.getDrawable(drawableId));

                customPromptDialog
                        .setDialogType(PromptDialog.DIALOG_TYPE_INFO)
                        .setTitleText(word + " 알람 설정")
                        .setContentText("일 후에 " + word)
                        .setPositiveListener("OK", new CustomPromptDialog.OnPositiveListener() {
                            @Override
                            public void onClick(CustomPromptDialog dialog) {
                                int current = customPromptDialog.getCurrentDay();
                                Log.d("current_date", current + "");
                                dialog.dismiss();
                            }
                        })
                        .show();

                //return 값을 true로 하면 longclicklistener만 작동 onclicklistener는 작동하지 않음.
                return true;
            }
        });
    }
}
