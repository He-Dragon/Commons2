package com.example.commons.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.commons.R;
import com.example.commons.base.BaseFragment;
import com.example.commons.utils.CustomDialog;

//import com.example.commons.TextActivity;
//import com.example.commons.utils.CustomDialog;

//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;

//import java.io.IOException;


/**
 * Created by 阿龙 on 2017/2/27.
 */

public class HomeFragment extends BaseFragment {


    private Button mButton;

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(args);
        return homeFragment;
    }

    @Override
    public int getContentView() {
        return R.layout.fagment_home;
    }

    @Override
    public void initView() {
        mButton = F(R.id.button);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        C(mButton);
    }

    @Override
    public void procssClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                CustomDialog.createLodingDialog(getActivity()).show();//加载框
//        new Thread() {
//                    @Override
//                    public void run() {
//                        super.run();
//                        for (int i = 0; i < 5; i++) {
//                            try {
//                                Document document = Jsoup.connect("http://www.qiushibaike.com/8hr/page/" + i + "/").get();
//                                Elements elements = document.select("a.contentHerf");
//                                for (int k = 0; k < elements.size(); k++) {
//                                    Element element = elements.get(k);
//                                    String href = element.attr("href");
//                                    Document document1 = Jsoup.connect("http://www.qiushibaike.com" + href).get();
//                                    Elements elementscontent = document1.select(".content");
//                                    Log.d(TAG, "run: " + elementscontent.text());
//                                }
//
//
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//
//                    }
//                }.start();
                break;
        }

    }


}
