package com.example.commons.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;


/**
 *  常用工具
 *
 */
public class ToolsUtils {

	/* 》》》》》》》》》》》》》》》》》图片等比列缩放会改变图片的大小，有压缩的效果》》》》》》》》》》》》》》》》》》》》 */

    /**
     * 图片的等比列缩放（有压缩效果的）
     */
    public static Bitmap createImageThumbnail(String filePath) {
        Bitmap bitmap = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, opts);

        opts.inSampleSize = computeSampleSize(opts, -1, 128 * 128);
        opts.inJustDecodeBounds = false;

        try {
            bitmap = BitmapFactory.decodeFile(filePath, opts);
        } catch (Exception e) {
            // : handle exception
        }
        return bitmap;
    }

    /**
     * 返回缩放比列
     */
    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    /**
     * 获取图片的宽高计算缩放比列
     */

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128
                : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

	/* 》》》》》》》》》》》》》》》》》 传入图片路径按比例大小压缩方法最后在做质量的压缩》》》》》》》》》》》》》》》》》》 */

    /**
     * 图片按比例大小压缩方法（根据路径获取图片并压缩）：
     */
    public static Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 1080f;// 这里设置高度为800f
        float ww = 720f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

	/* 》》》》》》》》》》》》》》》》》 传入图片Bitmap按比例大小压缩方法最后在做质量的压缩》》》》》》》》》》》》》》》》》》 */

    /**
     * 图片按比例大小压缩方法（根据Bitmap图片压缩）
     */
    public static Bitmap comp(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 1080f;// 这里设置高度为800f
        float ww = 720f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

	/* 》》》》》》》》》》》》》》》》》》图片质量的压缩（直接压缩会造成图片模糊所以先等比列压缩）》》》》》》》》》》》》》 */

    /**
     * 质量压缩方法
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

	/* 》》》》》》》》》》》》》》》》》》根据图片的路径旋转图片的角度》》》》》》》》》》》》》》》》》》》 */

    /**
     * 获取图片旋转角度 get the orientation of the bitmap
     * {@link ExifInterface}
     *
     * @param path
     * @return
     */
    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public final static int getDegress(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片 rotate the bitmap
     *
     * @param path
     * @return
     */
    public static Bitmap rotateBitmap(String path) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);//图片路径转化成bitmap
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(getDegress(path));
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
            return bitmap;
        }
        return bitmap;
    }

	/* 》》》》》》》》》》》》》》》返回图片的缩放比列，不太会改变图片的质量 没有什么压缩效果》》》》》》》》》》》》》》》》》 */

    /**
     * 返回图片的缩放比（不会太改变图片的质量 解决：BitmapFactory.decodeFile 的内存溢出的问题）
     * <p>
     * 这个要先用 BitmapFactory这个方法设置图片的 options.inJustDecodeBounds = true
     * 来只获取bitmap的宽高;
     **/

    public final static int caculateInSampleSize(BitmapFactory.Options options, int rqsW, int rqsH) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (rqsW == 0 || rqsH == 0)
            return 1;
        if (height > rqsH || width > rqsW) {
            final int heightRatio = Math.round((float) height / (float) rqsH);
            final int widthRatio = Math.round((float) width / (float) rqsW);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 获取图片的宽高
     */

    public final static BitmapFactory.Options getBitmapOptions(String srcPath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcPath, options);
        return options;
    }

	/* 》》》》》》》》》》》》》》》》》》bitmap 转换成base64》》》》》》》》》》》》》》》》》 */

    /**
     * @Title: imgToBase64 @Description: (这里用一句话描述这个方法的作用) @param
     * bitmap @return @throws
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.NO_WRAP);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * <p>
     * 将文件转成base64 字符串
     * </p>
     *
     * @param path 文件路径
     * @return
     * @throws Exception
     */
    public static String FileToBase64Str(String path) throws Exception {
        File file = new File(path);
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        byte[] buffer = new byte[(int) file.length()];
        in.read(buffer);
        in.close();
        return Base64.encodeToString(buffer, Base64.DEFAULT);
    }

	/* 》》》》》》》》》》》》》》》》》》》》》》》文件的创建》》》》》》》》》》》》》》》》》》》 */

    /**
     * 创建文件夹
     * <p>
     * 列子：fileName = "/Image" 必须要添加 下面两个权限
     * <!--往sdcard中写入数据的权限 -->
     * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"></uses-permission>
     * <!--在sdcard中创建/删除文件的权限 -->
     * <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"></uses-permissi
     */

    public static void creationFile(String fileName) {
        String fileSd = Environment.getExternalStorageDirectory().toString();// 获取Sd卡路径
        File file = new File(fileSd + fileName);
        if (!file.exists()) {// 如果文件存在了就不创建了
            file.mkdir();// 创建文件
        }
    }

	/* 》》》》》》》》》》》》》》保存bitmap到sd卡中》》》》》》》》》》》》》》》》》 */

    /**
     * 保存bitmap到sd卡中 saveName：保存图片的名字 fileName:保存图片到某个文件夹下
     */
    public static void saveBitmap(Bitmap bitmap, String fileName, String saveName) {
        String file = Environment.getExternalStorageDirectory().toString();// 获取Sd卡路径
        File fileBimap = new File(file + "/" + fileName);// 创建一个保存图片的文件夹SAVEIMAGE
        if (!fileBimap.exists()) {
            fileBimap.mkdir();
        }
        File fileBit = new File(fileBimap.getAbsolutePath() + "/" + saveName + ".jpg");// 将图片保存到SAVEIMAGE文件下
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(fileBit);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	/* 》》》》》》》》》》》》》》》》》文件的删除》》》》》》》》》》》》》》》》》》 */

    /**
     * 文件删除 fileName：文件夹的名字列:"/data"
     */

    public static void  deleteAllFiles(String filePath) {
		File root = new File(filePath);
		if (!root.exists()) { // 判断文件是否存在
			return;
		}
        File files[] = root.listFiles();  
        if (files != null)  
            for (File f : files) {  
                if (f.isDirectory()) { // 判断是否为文件夹  
                    deleteAllFiles(f.getAbsolutePath());  
                    try {  
                        f.delete();  
                    } catch (Exception e) {  
                    }  
                } else {  
                    if (f.exists()) { // 判断是否存在  
                        deleteAllFiles(f.getAbsolutePath());  
                        try {  
                            f.delete();  
                        } catch (Exception e) {  
                        }  
                    }  
                }  
            }  
        root.delete();
    } 

	/* 》》》》》》》》》》》》》》获取文件以及bitmap的大小单位为kb》》》》》》》》》》》》 */

    /**
     * 获取文件的大小（kb为单位）
     */
    public static int fileSize(String path) {
        File file = new File(path);
        return (int) (file.length() / 1024);
    }

    /**
     * 获取图片的大小
     */
    public static int bitmapSize(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int size = baos.toByteArray().length / 1024;
        return size;
    }

	/* 》》》》》》》》》》》》》》》》》》dp--xp 的转化》》》》》》》》》》》》》》》》》》》 */

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        Log.i("dip2px", "scale=" + scale);
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        Log.i("dip2px", "scale=" + scale);
        return (int) (pxValue / scale + 0.5f);
    }

	/* 》》》》》》》》》》》》》》》》》手机信息的获取》》》》》》》》》》》》》》》》 */

    /**
     * 获取手机相关的例如品牌型号等信息
     *
     * @param context
     */
    public static void getphoneInfo(Context context) {

        TelephonyManager mTm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = mTm.getDeviceId(); // imei
        String imsi = mTm.getSubscriberId(); // imsi
        String mbrand = Build.BRAND;// 手机品牌
        String mtype = Build.MODEL; // 手机型号
        String mphonenumer = mTm.getLine1Number(); // 手机号码，有的可得，有的不可得
    }

	/* 》》》》》》》》》》》》》》》》》》获取屏幕的宽高》》》》》》》》》》》》》》》》 */

    /**
     * 返回屏幕宽度
     */
    public static int getwidth(Activity context) {
        WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = mWindowManager.getDefaultDisplay().getWidth();
        return width;
    }

    /**
     * 返回屏幕高度
     */
    public static int getheight(Activity context) {
        WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = mWindowManager.getDefaultDisplay().getHeight();
        return height;
    }



	/* 》》》》》》》》》》》》》》座机和手机的验证》》》》》》》》》》》》》》》 */

    /**
     * 是否电话验证
     */
    public static boolean isMobileNO(String mobiles) {
        /*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
        String telRegex = "[1][3587]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        } else
            return mobiles.matches(telRegex);

    }

    /**
     * 座机验证
     */
    public static boolean isPlane(String mobiles) {
        String fixPhone = "(^0\\d{2}-?\\d{8}$)|(^0\\d{3}-?\\d{7,8}$)|(^\\(0\\d{2}\\)-?\\d{8}$)|(^\\(0\\d{3}\\)-?\\d{7,8}$)";
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        } else
            return mobiles.matches(fixPhone);

    }

	/* 》》》》》》》》》》》》》小数点保留后一位》》》》》》》》》》》》》》 */

    /**
     * 小数点保留后一位，如果第一位输入的是0的时候后面只能输入为 小数点
     */

    public static void decimalsLength(final EditText mEditText) {
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //  Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //  Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                //  Auto-generated method stub
                String ediContetn = s.toString().trim();
                if (s != null && !"".equals(ediContetn)) {

                    if (ediContetn.equals(".")) {// 判断第一位输入的是不是.
                        mEditText.setText("");
                    } else {
                        if (ediContetn.substring(0, 1).equals("0")) {// 判断第一位输入的是不是0
                            if (ediContetn.length() > 1) {
                                if (!ediContetn.substring(1, 2).equals(".")) {// 如果第一位为0后面只能输入小数点
                                    mEditText.setText("0");
                                    mEditText.setSelection(mEditText.getText().toString().trim().length());// 让光标在字符的后面
                                }
                                if (ediContetn.length() > 3) {// 如果第一位是0的时候限制只能输入小数点后一位
                                    mEditText.setText(ediContetn.substring(0, 3));
                                    mEditText.setSelection(mEditText.getText().toString().trim().length());
                                }
                            }
                        } else {// 第一位不是0时候
                            if (ediContetn.contains(".")) {// 如果输入的字符中带有 .
                                // 就截取小数点后一位
                                if (ediContetn.substring(ediContetn.indexOf("."), ediContetn.length()).length() > 2) {
                                    mEditText.setText(ediContetn.substring(0, ediContetn.indexOf(".") + 2));
                                    mEditText.setSelection(mEditText.getText().toString().trim().length());
                                }
                            }

                        }
                    }

                }
            }
        });
    }
	/* 》》》》》》》》》》》》》webView展示时候图片过大，解决页面左右滑动》》》》》》》》》》》》》》 */

    /**
     * webView展示时候图片过大，解决页面左右滑动(加载附文本)
     *
     * @htmltext 请求下来的附文本的内容 需要导入 jsoup-1.8.1 的jar包
     */
//	@SuppressLint("SetJavaScriptEnabled")
//	public static void getHtmlContent(WebView mWebView, String htmltext) {
//
//		WebSettings webSettings = mWebView.getSettings();
//		webSettings.setJavaScriptEnabled(true);
//		webSettings.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
//		webSettings.setUseWideViewPort(true);
//		webSettings.setLoadWithOverviewMode(true);
//
//		Document doc_Dis = (Document) Jsoup.parse(htmltext);
//		Elements ele_Img = ((Element) doc_Dis).getElementsByTag("img");
//		if (ele_Img.size() != 0) {
//			for (Element e_Img : ele_Img) {
//				e_Img.attr("width", "100%");
//				e_Img.attr("height", "auto");
//			}
//		}
//		String newHtmlContent = doc_Dis.toString();
//		mWebView.loadDataWithBaseURL("", newHtmlContent, "text/html", "UTF-8", "");
//	}

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersion(Context context) {
        String version = "";
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return version;
        }
    }
    /**
     * 获取本应用的版本号
     *
     * @param context
     * @return
     */

    public static String getCurrVersionName(Context context) {
        String versionCode = null;
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            versionCode = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 判断sdk版本
     *
     * @return
     */
    public static int getSDKVersion(Activity mActivity, boolean on) {
        if (Build.VERSION.SDK_INT < 19) {
            return 1;
        } else {
            Window win = mActivity.getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);
            return 0;
        }
    }

    /**
     * 用于获取状态栏的高度。
     *
     * @return 返回状态栏高度的dp。
     */
    public static int getStatusBarHeight(Activity mActivity) {
        int statusBarHeight = 0;
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object o = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = (Integer) field.get(o);
            statusBarHeight = mActivity.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
            Rect frame = new Rect();
            mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            statusBarHeight = frame.top;
        }

        final float scale = mActivity.getResources().getDisplayMetrics().density;
        return (int) ((float) statusBarHeight / scale + 0.5f);
    }
    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "B";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "K";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "M";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "G";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "T";
    }
    /**
     * 获取没有虚拟按键的屏幕高度
     */
    public int getNoHasVirtualKey(Activity activity) {
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();
        return height;
    }

    /**
     * 获取包含虚拟按键的屏幕高度
     */
    private int getHasVirtualKey(Activity activity) {
        int dpi = 0;
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            dpi = dm.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;
    }

}
