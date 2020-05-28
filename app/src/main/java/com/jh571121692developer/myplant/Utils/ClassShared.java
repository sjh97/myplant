package com.jh571121692developer.myplant.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.alexvasilkov.android.commons.ui.ContextHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jh571121692developer.myplant.PaintingUtils.DetailData;
import com.jh571121692developer.myplant.PaintingUtils.Painting;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ClassShared {
    private Context context;
    private Gson gson = new Gson();

    public ClassShared(Context context) {
        this.context = context;
    }

    public void putArray(ArrayList<DetailData> arrayList, String key){
        SharedPreferences preferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String classGson = gson.toJson(arrayList);
        editor.putString(key, classGson);
        editor.apply();
    }

    public ArrayList<DetailData> getArray(String key){
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

    public void putPaintingArray(ArrayList<Painting> arrayList, String key){
        SharedPreferences preferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String classGson = gson.toJson(arrayList);
        editor.putString(key, classGson);
        editor.apply();
    }

    public ArrayList<Painting> getPaintingArray(String key){
        SharedPreferences preferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        String classGson = preferences.getString(key, "");
        Type type = new TypeToken<ArrayList<Painting>>(){}.getType();
        ArrayList<Painting> arrayList;
        if(classGson.equals("")){
            arrayList = new ArrayList<>();
        }else {
            arrayList = gson.fromJson(classGson, type);
        }

        return arrayList;
    }
}
