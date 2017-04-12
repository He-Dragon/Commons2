package com.example.commons.base;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.request.target.ViewTarget;
import com.example.commons.R;

/**
 * Created by 阿龙 on 2017/4/11.
 * Glide 一些常用方法的说明：
 * .placeholder（R.mipmap.place）加载图片时候的默认图片
 * .error(R.mipmap.icon_photo_error)图片加载失败的默认图片
 * .crossFade(5000) 修改淡入淡出动画时间。Glide默认是包含淡入淡出动画的时间为300ms(毫秒)
 * .dontAnimate()取消淡入淡出动画
 * .asBitmap()显示gif静态图片只加载gif的第一帧
 * .asGif()显示gif动态图片
 * .skipMemoryCache(true)true:内存不缓存 false:内存缓存处理图
 * .diskCacheStrategy(DiskCacheStrategy.ALL) ALL:磁盘缓存缓存所有图片 NONE:不缓存 SOURCE:只缓存原图 RESULT:只缓存处理图—默认值
 * .clearMemory()清除所有内存缓存(需要在Ui线程操作)
 * .clearDiskCache()清除所有磁盘缓存(需要在子线程操作)
 * SimpleTarget target = new SimpleTarget<Bitmap>( 250, 250 ) ； into( target )返回一个bitmap对象，用这个在调用into（）方法前要调用asBitmap()告诉 Glide 我们需要一个图像
 *
 */

public class BaseGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {

        ViewTarget.setTagId(R.id.image_tag);
        /**
         * 自定义图片质量,默认是RGB_565
         * ARGB_8888 :32位图,带透明度,每个像素占4个字节
         *ARGB_4444 :16位图,带透明度,每个像素占2个字节
         *RGB_565 :16位图,不带透明度,每个像素占2个字节
         *ALPHA_8 :32位图,只有透明度,不带颜色,每个像素占4个字节
         * */
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        /**
         *内存缓存
         *Glide提供了一个类MemorySizeCalculator，用于决定内存缓存大小以及 bitmap 的缓存池。
         *bitmap 池维护了你 App 的堆中的图像分配。
         *正确的 bitmpa 池是非常必要的，因为它避免很多的图像重复回收，这样可以确保垃圾回收器的管理更加合理。
         *它的默认计算实现
         **/
        MemorySizeCalculator memorySizeCalculator = new MemorySizeCalculator(context);
        int defaultMemoryCacheSize = memorySizeCalculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = memorySizeCalculator.getBitmapPoolSize();
        builder.setMemoryCache(new LruResourceCache((int) (defaultMemoryCacheSize * 1.2f)));
        builder.setBitmapPool(new LruBitmapPool((int) (defaultBitmapPoolSize * 1.2f)));

        /**
         *磁盘缓存
         *Glide图片缓存有两种情况，一种是内部磁盘缓存另一种是外部磁盘缓存。
         *我们可以通过 builder.setDiskCache（）设置，并且Glide已经封装好了两个类实现外部和内部磁盘缓存，
         *分别是InternalCacheDiskCacheFactory和ExternalCacheDiskCacheFactory
         **/
        //磁盘缓存100M，默认250M
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, 100 * 1024 * 1024));
        //builder.setDiskCache(new ExternalCacheDiskCacheFactory(context,100*1024*1024));

    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
