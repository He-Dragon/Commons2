package com.example.commons.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.commons.R;

/**
 * Created by 阿龙 on 2017/2/28.
 *  PullRefreshView 刷新加载控件，适用于listview 或者 ScrollView 或者 RecyclerView
 */

public class PullRefreshView extends ViewGroup {

    private View mHeadView;//刷新头部的View
    private View mFootView;//加载底部的View
    private TextView mHeaderText;//头部的文字View
    private ImageView mHeaderArrow;//头部箭头View
    private ProgressBar mHeaderProgressBar;//头部正在刷新时候的状态view
    private TextView mFooterText;//底部刷新文字说明
    private ProgressBar mFooterProgressBar;//底部正在加载时候的状态

    private int mLayoutContentHeight;//viewgroup中内容的高度
    private int mHeaderHeight;//刷新头部view的高度
    private int mFooterHeight;//加载底部view的高度
    private int mlastMoveY;//记录点击位置
    private int mLastYIntercept;//用于记录手指向上还是线下滑动


    private Status mStatus = Status.NORMAL;

    private void updateStatus(Status status) {
        mStatus = status;
    }

    private enum Status {
        NORMAL,//没有任何操作
        TRY_REFRESH,//开始向下滑动
        REFRESHING,//正在刷新
        TRY_LOADMORE,//开始向上滑动
        LOADING;//正在加载
    }

    /**
     * 刷新和加载完了的回调
     */
    public interface OnRefreshListener {
        void refreshFinished();

        void loadMoreFinished();
    }

    private OnRefreshListener mRefreshListener;

    public void setRefreshListener(OnRefreshListener mRefreshListener) {
        this.mRefreshListener = mRefreshListener;
    }

    public PullRefreshView(Context context) {
        super(context);
    }

    public PullRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
    }

    /**
     * 自己应该在父容器中的位置
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mLayoutContentHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child == mHeadView) {//头部的位置
                mHeaderHeight = child.getMeasuredHeight();
                child.layout(0, 0 - mHeaderHeight, 0, mLayoutContentHeight);
            } else if (child == mFootView) {//加载底部的位置
                mFooterHeight = child.getMeasuredHeight();
                child.layout(0, mLayoutContentHeight, 0, mLayoutContentHeight + mFooterHeight);
            } else {//中间listview 或者 ScrollView 或者 RecyclerView 的位置
                if (child instanceof ScrollView) {//如果是ScrollView就是当前的高度
                    mLayoutContentHeight += getMeasuredHeight();
                    continue;
                }
                mLayoutContentHeight += child.getMeasuredHeight();
            }

        }

    }

    /**
     * 当在xml中加载完了所有child时候调用
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        addHeader();
        addFooter();
    }

    /**
     * 添加头部view到viewgroup中
     */
    private void addHeader() {
        mHeadView = LayoutInflater.from(getContext()).inflate(R.layout.pull_header, null, false);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(mFootView, params);//把头部刷新的View放到当前ViewGroup中
        mHeaderText = (TextView) findViewById(R.id.header_text);
        mHeaderProgressBar = (ProgressBar) findViewById(R.id.header_progressbar);
        mHeaderArrow = (ImageView) findViewById(R.id.header_arrow);
    }

    /**
     * 添加底部view到viewgroup中
     */
    private void addFooter() {
        mFootView = LayoutInflater.from(getContext()).inflate(R.layout.pull_footer, null, false);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(mFootView, params);//把底部加载的View放到当前ViewGroup中
        mFooterText = (TextView) findViewById(R.id.footer_text);
        mFooterProgressBar = (ProgressBar) findViewById(R.id.footer_progressbar);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = false;//是否需要拦截此事件
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mlastMoveY = y;
                intercept = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (y > mLastYIntercept) {
                    View child = getChildAt(1);
                    intercept = getRefreshIntercept(child);

                    if (intercept) {
                        updateStatus(mStatus.TRY_REFRESH);
                    }
                } else if (y < mLastYIntercept) {
                    View child = getChildAt(1);
                    intercept = getLoadMoreIntercept(child);

                    if (intercept) {
                        updateStatus(mStatus.TRY_LOADMORE);
                    }
                } else {
                    intercept = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                break;
        }
        mLastYIntercept = y;
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int y = (int) event.getY();

        // 正在刷新或加载更多，避免重复
        if (mStatus == Status.REFRESHING || mStatus == Status.LOADING) {
            return true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mlastMoveY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int dy = mlastMoveY - y;


                // 一直在下拉
                if (getScrollY() <= 0 && dy <= 0) {
                    if (mStatus == Status.TRY_LOADMORE) {
                        scrollBy(0, dy / 30);
                    } else {
                        scrollBy(0, dy / 3);
                    }
                }
                // 一直在上拉
                else if (getScrollY() >= 0 && dy >= 0) {
                    if (mStatus == Status.TRY_REFRESH) {
                        scrollBy(0, dy / 30);
                    } else {
                        scrollBy(0, dy / 3);
                    }
                } else {
                    scrollBy(0, dy / 3);
                }

                beforeRefreshing(dy);
                beforeLoadMore();

                break;
            case MotionEvent.ACTION_UP:
                // 下拉刷新，并且到达有效长度
                if (getScrollY() <= -mHeaderHeight) {
                    releaseWithStatusRefresh();
                    if (mRefreshListener != null) {
                        mRefreshListener.refreshFinished();
                    }
                }
                // 上拉加载更多，达到有效长度
                else if (getScrollY() >= mFooterHeight) {
                    releaseWithStatusLoadMore();
                    if (mRefreshListener != null) {
                        mRefreshListener.loadMoreFinished();
                    }
                } else {
                    releaseWithStatusTryRefresh();
                    releaseWithStatusTryLoadMore();
                }
                break;
        }

        mlastMoveY = y;
        return super.onTouchEvent(event);
    }

    /**
     * 刷新是否拦截
     * 返回位true就是要拦截事件
     */
    private boolean getRefreshIntercept(View child) {
        boolean intercept = false;

        if (child instanceof AdapterView) {
            intercept = adapterViewRefreshIntercept(child);
        } else if (child instanceof ScrollView) {
            intercept = scrollViewRefreshIntercept(child);
        } else if (child instanceof RecyclerView) {
            intercept = recyclerViewRefreshIntercept(child);
        }
        return intercept;
    }

    /**
     * 加载是否拦截
     * 返回位true就是要拦截事件
     */
    private boolean getLoadMoreIntercept(View child) {
        boolean intercept = false;
        if (child instanceof AdapterView) {
            intercept = adapterViewLoadMoreIntercept(child);
        } else if (child instanceof ScrollView) {
            intercept = scrollViewLoadMoreIntercept(child);
        } else if (child instanceof RecyclerView) {
            intercept = recyclerViewLoadMoreIntercept(child);
        }
        return intercept;
    }

    /**
     * 判断adapter是否在最顶部，如果在就不拦截，让事件传播到onTouchEvent（）方法，出现头部mHeadView
     */
    private boolean adapterViewRefreshIntercept(View child) {
        boolean intercept = true;
        AdapterView adapterChild = (AdapterView) child;
        if (adapterChild.getFirstVisiblePosition() != 0
                || adapterChild.getChildAt(0).getTop() != 0) {
            intercept = false;
        }
        return intercept;
    }

    /**
     * 判断adapter是否在最底部，如果在就不拦截，让事件传播到onTouchEvent（）方法，出现底部mFootView
     */
    private boolean adapterViewLoadMoreIntercept(View child) {
        boolean intercept = false;
        AdapterView adapterChild = (AdapterView) child;
        if (adapterChild.getLastVisiblePosition() == adapterChild.getCount() - 1 &&
                (adapterChild.getChildAt(adapterChild.getChildCount() - 1).getBottom() >= getMeasuredHeight())) {
            intercept = true;
        }
        return intercept;
    }

    /**
     * 判断scrollView是否在最顶部，如果在就不拦截，让事件传播到onTouchEvent（）方法，出现头部mHeadView
     */
    private boolean scrollViewRefreshIntercept(View child) {
        boolean intercept = false;
        if (child.getScrollY() <= 0) {
            intercept = true;
        }
        return intercept;
    }

    /**
     * 判断scrollView是否在最底部，如果在就不拦截，让事件传播到onTouchEvent（）方法，出现底部mFootView
     */
    private boolean scrollViewLoadMoreIntercept(View child) {
        boolean intercept = false;
        ScrollView scrollView = (ScrollView) child;
        View scrollChild = scrollView.getChildAt(0);

        if (scrollView.getScrollY() >= (scrollChild.getHeight() - scrollView.getHeight())) {
            intercept = true;
        }
        return intercept;
    }

    /**
     * 判断recyclerView是否在最顶部，如果在就不拦截，让事件传播到onTouchEvent（）方法，出现头部mHeadView
     */
    private boolean recyclerViewRefreshIntercept(View child) {
        boolean intercept = false;

        RecyclerView recyclerView = (RecyclerView) child;
        if (recyclerView.computeVerticalScrollOffset() <= 0) {
            intercept = true;
        }
        return intercept;
    }

    /**
     * 判断ecyclerView是否在最底部，如果在就不拦截，让事件传播到onTouchEvent（）方法，出现底部mFootView
     */
    private boolean recyclerViewLoadMoreIntercept(View child) {
        boolean intercept = false;

        RecyclerView recyclerView = (RecyclerView) child;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange()) {
            intercept = true;
        }

        return intercept;
    }


    /**
     * 修改header和footer的状态
     */
    public void beforeRefreshing(float dy) {
        //计算旋转角度
        int scrollY = Math.abs(getScrollY());
        scrollY = scrollY > mHeaderHeight ? mHeaderHeight : scrollY;
        float angle = (float) (scrollY * 1.0 / mHeaderHeight * 180);
        mHeaderArrow.setRotation(angle);


        if (getScrollY() <= -mHeaderHeight) {
            mHeaderText.setText("松开刷新");
        } else {
            mHeaderText.setText("下拉刷新");
        }
    }

    public void beforeLoadMore() {
        if (getScrollY() >= mHeaderHeight) {
            mFooterText.setText("松开加载更多");
        } else {
            mFooterText.setText("上拉加载更多");
        }
    }

    public void refreshFinished() {
        scrollTo(0, 0);
        mHeaderText.setText("下拉刷新");
        mHeaderProgressBar.setVisibility(GONE);
        mHeaderArrow.setVisibility(VISIBLE);
        updateStatus(Status.NORMAL);
    }

    public void loadMoreFinished() {
        mFooterText.setText("上拉加载");
        mFooterProgressBar.setVisibility(GONE);
        scrollTo(0, 0);
        updateStatus(Status.NORMAL);
    }

    private void releaseWithStatusTryRefresh() {
        scrollBy(0, -getScrollY());
        mHeaderText.setText("下拉刷新");
        updateStatus(Status.NORMAL);
    }

    private void releaseWithStatusTryLoadMore() {
        scrollBy(0, -getScrollY());
        mFooterText.setText("上拉加载更多");
        updateStatus(Status.NORMAL);
    }

    private void releaseWithStatusRefresh() {
        scrollTo(0, -mHeaderHeight);
        mHeaderProgressBar.setVisibility(VISIBLE);
        mHeaderArrow.setVisibility(GONE);
        mHeaderText.setText("正在刷新");
        updateStatus(Status.REFRESHING);
    }

    private void releaseWithStatusLoadMore() {
        scrollTo(0, mFooterHeight);
        mFooterText.setText("正在加载");
        mFooterProgressBar.setVisibility(VISIBLE);
        updateStatus(Status.LOADING);
    }
  /*修改header和footer的状态*/


}
