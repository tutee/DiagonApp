package com.rightdecisions.diagonapp.activities;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by Tute on 01/01/2017.
 */

public class GlideConfiguration implements GlideModule{

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // Change bitmap format to ARGB_8888
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        // register ModelLoaders here.
    }


}
