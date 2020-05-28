package com.jh571121692developer.myplant.Utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jh571121692developer.myplant.PaintingUtils.Painting;

public class GlideHelper {

    private GlideHelper(){}

    public static void loadPaintingImage(ImageView image, Painting painting){
        Glide.with(image.getContext().getApplicationContext())
                .load(painting.getImageId())
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(image);
    }
}
