package com.wong.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class MySecondLayout extends ViewGroup {
    public MySecondLayout(Context context) {
        super(context);
    }

    public MySecondLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySecondLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MySecondLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MySecondLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MySecondLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }


    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return p;
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return super.checkLayoutParams(p);
    }


    /**
     * 在onMeasure方法里进行子控件测量及ViewGroup自身的测量
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int layoutWidth = 0;
        int layoutHeight = 0;
        // 计算出所有的childView的宽和高
        /*测量子控件的大小，计算出所有的childView的宽和高,如果不进行测量，那么子控件就会不显示*/
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int cWidth = 0;
        int cHeight = 0;
        int count = getChildCount();

        if (widthMode == MeasureSpec.EXACTLY) {
            //如果布局容器的宽度模式是确定的（具体的size或者match_parent），直接使用父窗体建议的宽度
            layoutWidth = sizeWidth;
        } else {
            //如果是未指定或者wrap_content，我们都按照包裹内容做，宽度方向上只需要拿到所有子控件中宽度做大的作为布局宽度
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                cWidth = child.getMeasuredWidth();
                //获取子控件最大宽度
                layoutWidth = cWidth > layoutWidth ? cWidth : layoutWidth;
            }
        }
        //高度很宽度处理思想一样
        if (heightMode == MeasureSpec.EXACTLY) {
            layoutHeight = sizeHeight;
        } else {
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                cHeight = child.getMeasuredHeight();
                layoutHeight = cHeight > layoutHeight ? cHeight : layoutHeight;
            }
        }

        // 测量并保存layout的宽高
        setMeasuredDimension(layoutWidth, layoutHeight);
    }

    /**
     * 对子控件进行摆放
     *
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        /*用于定位每个子控件的位置时用的临时变量*/
        int left = 0;
        int top = 0;
        /*ViewGroup容器里的子控件数*/
        int count = getChildCount();
        /*子控件的测量宽度和高度，不要使用child.getWidth()和child.getHeight()*/
        int childMeasureWidth = 0;
        int childMeasureHeight = 0;
        MySecondLayoutParams params = null;
        /*摆放ViewGroup容器里的子控件*/
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            // 注意此处不能使用getWidth和getHeight，这两个方法必须在onLayout执行完，才能正确获取宽高
            childMeasureWidth = child.getMeasuredWidth();
            childMeasureHeight = child.getMeasuredHeight();

            params = (MySecondLayoutParams) child.getLayoutParams();
            switch (params.position) {
                case MySecondLayoutParams.POSITION_MIDDLE:    // 中间
                    left = (getWidth() - childMeasureWidth) / 2;
                    top = (getHeight() - childMeasureHeight) / 2;
                    break;
                case MySecondLayoutParams.POSITION_LEFT:      // 左上方
                    left = 0;
                    top = 0;
                    break;
                case MySecondLayoutParams.POSITION_RIGHT:     // 右上方
                    left = getWidth() - childMeasureWidth;
                    top = 0;
                    break;
                case MySecondLayoutParams.POSITION_BOTTOM:    // 左下角
                    left = 0;
                    top = getHeight() - childMeasureHeight;
                    break;
                case MySecondLayoutParams.POSITION_RIGHTANDBOTTOM:// 右下角
                    left = getWidth() - childMeasureWidth;
                    top = getHeight() - childMeasureHeight;
                    break;
                default:
                    break;
            }

            // 确定子控件的位置，四个参数分别代表（左上右下）点的坐标值
            child.layout(left, top, left + childMeasureWidth, top + childMeasureHeight);
        }

    }

    public static class MySecondLayoutParams extends ViewGroup.MarginLayoutParams {
        public static final int POSITION_MIDDLE = 0; // 中间
        public static final int POSITION_LEFT = 1; // 左上方
        public static final int POSITION_RIGHT = 2; // 右上方
        public static final int POSITION_BOTTOM = 3; // 左下角
        public static final int POSITION_RIGHTANDBOTTOM = 4; // 右下角

        public int position = POSITION_LEFT;  // 默认我们的位置就是左上角

        public MySecondLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.MySecondLayout_Layout);
            //获取设置在子控件上的位置属性
            position = a.getInt(R.styleable.MySecondLayout_Layout_layout_position, position);

            a.recycle();
        }

        public MySecondLayoutParams(int width, int height) {
            super(width, height);
        }

        public MySecondLayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public MySecondLayoutParams(LayoutParams source) {
            super(source);
        }

    }

}
