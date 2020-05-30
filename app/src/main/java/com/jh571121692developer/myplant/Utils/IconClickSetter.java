package com.jh571121692developer.myplant.Utils;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.github.zagum.switchicon.SwitchIconView;
import com.jh571121692developer.myplant.CustomPromptDialog.CustomPromptDialog;
import com.jh571121692developer.myplant.R;
import com.jh571121692developer.myplant.UnfoldableDetailsActivity;

import cn.refactor.lib.colordialog.PromptDialog;

public class IconClickSetter {

    Context context;

    public IconClickSetter(Context context) {
        this.context = context;
    }

    public void setAllClickListener(final SwitchIconView switchIconView, final String word, final int drawableId){
        switchIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchIconView.switchState();
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
