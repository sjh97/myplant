package com.jh571121692developer.myplant.Utils;

import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jh571121692developer.myplant.PaintingUtils.Painting;

public class GlideHelper {

    private GlideHelper(){}

    public static void loadPaintingImage(ImageView image, Painting painting){

//        Glide.with(image.getContext().getApplicationContext())
//                .load(painting.getImageId())
//                .dontAnimate()
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .into(image);

        // imagecrop에서 받은 uri를 string 형태로 저장을 해 놨었다. 이를 다시 uri 형태로 바꾸는 것이 Uri.parse(string) 2020.05.30
        Glide.with(image.getContext().getApplicationContext())
                .load(Uri.parse(painting.getImageUri()))
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(image);
    }
}
