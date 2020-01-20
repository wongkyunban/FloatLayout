package com.wong.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class MyLayout extends ViewGroup {
    public MyLayout(Context context) {
        super(context);
    }

    public MyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return p;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }


    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        return p instanceof MarginLayoutParams;
    }

    /**
     *
     * 在onMeasure方法里进行子控件测量及ViewGroup自身的测量
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /*测量子控件的大小，计算出所有的childView的宽和高,如果不进行测量，那么子控件就会不显示*/
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 对子控件进行摆放
     *
     * @param changed
     * @param l       距父容器的左边距
     * @param t       距父容器的顶边距
     * @param r       距父容器的右边距
     * @param b       距父容器的底边距
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        /*用于记录子控件添加到同一行后的累计宽度，以此作为是否换行的依据*/
        int cumulateLayoutWidth = 0;
        /*累计每行最大的高度值，以此作为下一行与父容器的顶边距的值*/
        int cumulateLayoutHeight = 0;
        /*用于定位每个子控件的位置时用的临时变量*/
        int left, top, right, bottom;
        /*记录每行的最大高度的临时变量，在换行时使用*/
        int maxLineHeight = 0;
        /*ViewGroup容器里的子控件数*/
        int count = getChildCount();
        /*摆放ViewGroup容器里的子控件*/
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            /*子控件的测量宽度和高度，不要使用child.getWidth()和child.getHeight()*/
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams)child.getLayoutParams();
            Log.i("YYY",lp.leftMargin+"#"+lp.topMargin+"#"+lp.rightMargin+"#"+lp.bottomMargin);
            /*getWidth()是ViewGroup的宽度,如果累计的宽度再加一个子控件的宽度超过了父容器的宽度getWidth(),那么就要另起一行了*/
            if (cumulateLayoutWidth + lp.leftMargin+childWidth+lp.rightMargin < getWidth()) {
                left = cumulateLayoutWidth+lp.leftMargin;
                top = cumulateLayoutHeight+lp.topMargin;
                right = left + childWidth;
                bottom = top + childHeight;
            } else {
                cumulateLayoutWidth = 0;
                cumulateLayoutHeight = cumulateLayoutHeight + maxLineHeight;
                maxLineHeight = 0;
                left = cumulateLayoutWidth+lp.leftMargin;
                top = cumulateLayoutHeight+lp.topMargin;
                right = left + childWidth;
                bottom = top + childHeight;
            }
            /*累加宽度*/
            cumulateLayoutWidth = cumulateLayoutWidth + lp.leftMargin+childWidth+lp.rightMargin ;
            /*选出行高*/
            maxLineHeight = Math.max(maxLineHeight, lp.topMargin+childHeight+lp.bottomMargin);
            child.layout(left, top, right, bottom);
        }
    }
}
