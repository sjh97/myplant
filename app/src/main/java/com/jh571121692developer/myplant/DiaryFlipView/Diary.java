package com.jh571121692developer.myplant.DiaryFlipView;

import java.util.ArrayList;
import java.util.List;

public class Diary {
    private int avatar;
    private String nickname;
    private int background;
    private List<String> interests = new ArrayList<>();

    public Diary(int avatar, String nickname, int background, List<String> interests) {
        this.avatar = avatar;
        this.nickname = nickname;
        this.background = background;
        this.interests = interests;
    }

    public int getAvatar() {
        return avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public int getBackground() {
        return background;
    }

    public List<String> getInterests() {
        return interests;
    }
}
