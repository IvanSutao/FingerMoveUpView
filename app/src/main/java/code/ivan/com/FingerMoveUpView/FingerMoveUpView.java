package code.ivan.com.FingerMoveUpView;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.Toast;

import code.ivan.com.slideviewinorout.R;

/**
 * Copyright (C), 2018-2018, 912租房网
 * Author: 张一帆
 * Date: 2018/11/15 0015 上午 10:37
 * Description: 模拟链家 地图页上划吸附列表
 */

public class FingerMoveUpView extends RelativeLayout {

    private Context mContext;
        private RelativeLayout mTitleRl; //头部容器
    private RelativeLayout mContentRl; //内容容器
    private int mScreemHeight; // 屏幕高度，不包含状态栏
    /**
     * 内容容器最小高度
     */
    private int mMinHeight;
    /**
     * 内容容器中间高度
     */
    private int mMidHeight;
    /**
     * 内容容器最大高度
     */
    private int mMaxHeight;
    /**
     * 按下点的纵坐标
     */
    private int mDownY;
    private int mContentL; //内容容器 左边坐标
    private int mContentR; //内容容器 右边坐标
    private int mContentB; //内容容器 下边坐标
    private boolean mCanScroll = true; //标识是否可以滑动内容容器

    private Scroller mScroller;

    public FingerMoveUpView(Context context) {
        this(context, null);
    }

    public FingerMoveUpView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FingerMoveUpView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        this.init();
    }

    private void init() {
        inflate(mContext, R.layout.view_finger_move_up, this);
        mTitleRl = findViewById(R.id.rl_title);
        mContentRl = findViewById(R.id.rl_contennt);
        mScroller = new Scroller(mContext);
        mScreemHeight = getScreenHeight();
        mMinHeight = (int) (mScreemHeight * 0.6);
        mMidHeight = (int) (mScreemHeight * 0.7);
        mMaxHeight = (int) (mScreemHeight * 0.8);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mContentRl.getLayoutParams();
        params.height = mMinHeight;
        RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) mTitleRl.getLayoutParams();
        params2.height = (int) (mScreemHeight * 0.2);
        initData();
    }

    private void initData() {
        mContentRl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "hello", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mCanScroll;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mContentL == 0) {
            mContentL = mContentRl.getLeft();
            mContentR = mContentRl.getRight();
            mContentB = mContentRl.getBottom();
        }
        int y = (int) ev.getRawY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mCanScroll) {
                    return false;
                }
                int offY = y - mDownY;//向上为负数，向下为正数
                mDownY = y;
                int tempHeight = mContentRl.getHeight() - offY;
                if (tempHeight < mMinHeight) {
                    mContentRl.layout(mContentL, mScreemHeight - mMinHeight, mContentR, mContentB);
                    return true;
                }
                if (tempHeight > mMaxHeight) {
                    mContentRl.layout(mContentL, mScreemHeight - mMaxHeight, mContentR, mContentB);
                    return true;
                }
                mContentRl.layout(mContentL, mContentRl.getTop() + offY, mContentR, mContentB);
                break;
            case MotionEvent.ACTION_UP:
                if (mContentRl.getHeight() >= mMidHeight) {
                    mScroller.startScroll(0, mContentRl.getTop(), 0, mContentRl.getHeight() - mMaxHeight);
                    mCanScroll = false;
                    invalidate();
                }
                if (mContentRl.getHeight() < mMidHeight) {
                    mScroller.startScroll(0, mContentRl.getTop(), 0, mContentRl.getHeight() - mMinHeight);
                    mCanScroll = true;
                    invalidate();
                }
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        boolean isAnimFinish = mScroller.computeScrollOffset();
        if (isAnimFinish) {
            mContentRl.layout(mContentRl.getLeft(), mScroller.getCurrY(), mContentRl.getRight(), mContentRl.getBottom());
            invalidate();
        }

        if (!mCanScroll) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mContentRl.getLayoutParams();
            params.height = mMaxHeight;
            mTitleRl.setVisibility(VISIBLE); // 此方法会刷新整个页面，导致mContentRl重新刷新回到初始化时设置的高度。因此需要重新设置mContentRl的高度
        }
    }

    public int dp2px(float dpValue) {
        final float scale = mContext.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public int getScreenHeight() {
        //屏幕区域，不包含状态栏
        return mContext.getResources().getDisplayMetrics().heightPixels - getStatusBarHeightPixel((Activity) mContext);
    }

    /**
     * 获取状态栏高度
     *
     * @param context 上下文
     * @return 状态栏高度 单位像素
     */
    public int getStatusBarHeightPixel(Activity context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }
}
