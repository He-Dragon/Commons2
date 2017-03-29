package com.example.commons.utils;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.commons.R;

/**
 * Created by 阿龙 on 2017/3/1.
 *
 * 加载数据的loding弹框和仿苹果的提示弹框
 */

public class CustomDialog {

    public static Dialog lodingDialog;//加载数据的loding
    /**
     * 带有文字提示的LodingDialog
     *
     * @param hint 提示信息
     */
    public static Dialog createLodingDialog(Context context, String hint) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_loding, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loding_anim);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        tipTextView.setText(hint);// 设置加载信息
        if (lodingDialog == null) {
            lodingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
            lodingDialog.setCanceledOnTouchOutside(false);
            ;// 可以用“返回键”取消
        }
        lodingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        return lodingDialog;
    }

    /**
     * 只弹出一个一个加载框
     */
    public static Dialog createLodingDialog(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_loding, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loding_anim);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        if (lodingDialog == null) {
            lodingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
            lodingDialog.setCanceledOnTouchOutside(false);
            ;// 可以用“返回键”取消
        }
        lodingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        return lodingDialog;
    }
    /**
     * 取消loding加载框
     * 在页面关闭的时候也应该调用此方法，防止页面关闭后Dialog还存在，出现异常
     */
    public static void cancelLodingDialog() {
        if (lodingDialog != null || lodingDialog.isShowing()) {
            lodingDialog.cancel();
        }
    }

    /**
     * 一个提示按钮的弹框
     *
     * @param titleHint   标题的提示
     * @param hintContent 提示的类容
     * @param affirmHint  确认按钮提示字
     * @param buttonClick 两个按钮点击的回调
     */
    public static void oneButtonHintDialog(Context context, String titleHint, String hintContent
            , String affirmHint, final HintButtonClick buttonClick) {
        final Dialog hintDialog = new Dialog(context, R.style.Dialog_hint);
        hintDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        hintDialog.setContentView(R.layout.dialog_one_hint);
        hintDialog.setCanceledOnTouchOutside(false);
        hintDialog.setCancelable(false);
        WindowManager.LayoutParams params = hintDialog.getWindow().getAttributes();
        TextView hintTitleTv = (TextView) hintDialog.findViewById(R.id.hint_title_tv);
        TextView hintContentTv = (TextView) hintDialog.findViewById(R.id.hint_content_tv);
        Button hintBtn = (Button) hintDialog.findViewById(R.id.hint_btn);
        if (TextUtils.isEmpty(titleHint)) {
            hintTitleTv.setVisibility(View.GONE);
        } else {
            hintTitleTv.setText(titleHint);
        }
        hintContentTv.setText(hintContent);
        hintBtn.setText(affirmHint);
        Window dialogWindow = hintDialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        hintDialog.show();
        hintDialog.getWindow().setAttributes(params);
        hintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClick.affirmButton();
                hintDialog.dismiss();

            }
        });
    }

    /**
     * 两个提示按钮的弹框
     *
     * @param titleHint   标题的提示
     * @param hintContent 提示的类容
     * @param affirmHint  确认按钮提示字
     * @param cancelHint  取消按钮提示字
     * @param buttonClick 两个按钮点击的回调
     */
    public static void twoButtonHintDialog(Context context, String titleHint, String hintContent
            , String affirmHint, String cancelHint, final HintButtonClick buttonClick) {
        final Dialog hintDialog = new Dialog(context, R.style.Dialog_hint);
        hintDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        hintDialog.setContentView(R.layout.dialog_two_hint);
        hintDialog.setCanceledOnTouchOutside(false);
        hintDialog.setCancelable(false);
        WindowManager.LayoutParams params = hintDialog.getWindow().getAttributes();
        TextView hintTitleTv = (TextView) hintDialog.findViewById(R.id.hint_title_tv);
        TextView hintContentTv = (TextView) hintDialog.findViewById(R.id.hint_content_tv);
        Button hintAffirmBtn = (Button) hintDialog.findViewById(R.id.hint_affirm_btn);
        Button hintCancelBtn = (Button) hintDialog.findViewById(R.id.hint_cancel_btn);
        if (TextUtils.isEmpty(titleHint)) {
            hintTitleTv.setVisibility(View.GONE);
        } else {
            hintTitleTv.setText(titleHint);
        }
        hintContentTv.setText(hintContent);
        hintAffirmBtn.setText(affirmHint);
        hintCancelBtn.setText(cancelHint);
        Window dialogWindow = hintDialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        hintDialog.show();
        hintDialog.getWindow().setAttributes(params);
        hintAffirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClick.affirmButton();
                hintDialog.dismiss();

            }
        });
        hintCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClick.cancelButton();
                hintDialog.dismiss();

            }
        });
    }


    /**
     * 页面提示框中确定和取消按钮的回调方法
     */
    public interface HintButtonClick {
        void cancelButton();

        void affirmButton();
    }


}
