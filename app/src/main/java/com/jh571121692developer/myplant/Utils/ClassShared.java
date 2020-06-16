package com.jh571121692developer.myplant.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jh571121692developer.myplant.DiaryFlipView.CustomDiary;
import com.jh571121692developer.myplant.DiscreteScrollView_Utils.DetailData;
import com.ramotion.expandingcollection.ECCardData;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ClassShared {
    private Context context;
    private Gson gson = new Gson();

    public ClassShared(Context context) {
        this.context = context;
    }

    public void putDetailArray(ArrayList<DetailData> arrayList, String key){
        SharedPreferences preferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String classGson = gson.toJson(arrayList);
        editor.putString(key, classGson);
        editor.apply();
    }

    public ArrayList<DetailData> getDetailArray(String key){
        SharedPreferences preferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        String classGson = preferences.getString(key, "");
        Type type = new TypeToken<ArrayList<DetailData>>(){}.getType();
        ArrayList<DetailData> arrayList;
        if(classGson.equals("")){
            arrayList = new ArrayList<>();
        }else {
            arrayList = gson.fromJson(classGson, type);
        }

        return arrayList;
    }

    public void putDiaryArray(ArrayList<CustomDiary> arrayList, String key){
        SharedPreferences preferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String classGson = gson.toJson(arrayList);
        editor.putString(key, classGson);
        editor.apply();
    }

    public ArrayList<CustomDiary> getCustomDiaryArray(String key){
        SharedPreferences preferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        String classGson = preferences.getString(key, "");
        Type type = new TypeToken<ArrayList<CustomDiary>>(){}.getType();
        ArrayList<CustomDiary> arrayList;
        if(classGson.equals("")){
            arrayList = new ArrayList<>();
        }else {
            arrayList = gson.fromJson(classGson, type);
        }

        return arrayList;
    }

    public void putCardDataArray(ArrayList<ECCardData> arrayList, String key){
        SharedPreferences preferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String classGson = gson.toJson(arrayList);
        editor.putString(key, classGson);
        editor.apply();
    }

    public ArrayList<ECCardData> getCardDataArray(String key){
        SharedPreferences preferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        String classGson = preferences.getString(key, "");
        Type type = new TypeToken<ArrayList<ECCardData>>(){}.getType();
        ArrayList<ECCardData> arrayList;
        if(classGson.equals("")){
            arrayList = new ArrayList<>();
        }else {
            arrayList = gson.fromJson(classGson, type);
        }

        return arrayList;
    }
}
