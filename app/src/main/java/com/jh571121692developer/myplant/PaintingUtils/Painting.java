package com.jh571121692developer.myplant.PaintingUtils;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;

import com.jh571121692developer.myplant.R;
import com.jh571121692developer.myplant.Utils.ClassShared;

import java.util.ArrayList;

public class Painting {

    private final int imageId;
    private final String title;
    private final String year;
    private final String location;
    private final String imageUri;

    public Painting(int imageId, String title, String year, String location, String imageUri) {
        this.imageId = imageId;
        this.title = title;
        this.year = year;
        this.location = location;
        this.imageUri = imageUri;
    }

    public int getImageId() {
        return imageId;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getLocation() {
        return location;
    }

    public String getImageUri() {
        return imageUri;
    }

    public static Painting[] getAllPaintings(Context context){

        String key = context.getString(R.string.detailsKey);

        ClassShared classShared = new ClassShared(context);
        ArrayList<Painting> paintingList = classShared.getPaintingArray(key);

        ArrayList<Integer> imageIdList = new ArrayList<>();
        ArrayList<String> titleList = new ArrayList<>();
        ArrayList<String> yearList = new ArrayList<>();
        ArrayList<String> locationList = new ArrayList<>();
        ArrayList<String> imageUriList = new ArrayList<>();

        for(Painting painting : paintingList){
            imageIdList.add(painting.getImageId());
            titleList.add(painting.getTitle());
            yearList.add(painting.getYear());
            locationList.add(painting.getLocation());
            imageUriList.add(painting.getImageUri());
        }

        String[] titles = titleList.toArray(new String[0]);
        String[] years = yearList.toArray(new String[0]);
        String[] locations = locationList.toArray(new String[0]);
        Integer[] imageId = imageIdList.toArray(new Integer[0]);
        String[] imageUris = imageUriList.toArray(new String[0]);
//        TypedArray images = res.obtainTypedArray(R.array.paintings_images);

        int size = titles.length;
        Painting[] paintings = new Painting[size];

        for(int i = 0; i < size; i++){
//            final int imageId = images.getResourceId(i, -1);
            paintings[i] = new Painting(imageId[i], titles[i], years[i], locations[i], imageUris[i]);
        }

//        images.recycle();

        return paintings;
    }

}
