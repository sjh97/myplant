package com.jh571121692developer.myplant.DiaryFlipView;

import java.util.ArrayList;
import java.util.List;

public class CustomDiary {
    private String avatarUri;
    private String date;
    private int background;
    private String diaryText;

    public CustomDiary(String avatarUri, String date, int background, String diaryText) {
        this.avatarUri = avatarUri;
        this.date = date;
        this.background = background;
        this.diaryText = diaryText;
    }

    public String getAvatarUri() {
        return avatarUri;
    }

    public String getDate() {
        return date;
    }

    public int getBackground() {
        return background;
    }

    public String getDiaryText() {
        return diaryText;
    }
}
