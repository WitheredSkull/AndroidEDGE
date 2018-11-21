package com.shuanglu.edge.View.ToolBar.View;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shuanglu.edge.R;
import com.shuanglu.edge.View.ToolBar.Model.ToolBarMenu;
import com.shuanglu.edge.View.ToolBar.Model.ToolBarValue;

// Create Time 2018/10/26
// Create Author Daniel
public class EdgeToolBarView extends ConstraintLayout {
    private ConstraintLayout mLayout;
    private TextView mTvBack, mTvTitle, mTvMore;
    private View mVLine;
    private Drawable mDrawableBack, mDrawableTitle, mDrawableMore;
    private String mTextBack, mTextTitle, mTextMore;
    private int mColorText, mColorLine;
    private float mHeightLine;

    public EdgeToolBarView(Context context) {
        super(context);
        initView();
        initDefaultValue();
        initValue();
        initListener();
    }

    public EdgeToolBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        initDefaultValue();
        initStyleable(attrs);
        initValue();
        initListener();
    }

    public EdgeToolBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initDefaultValue();
        initStyleable(attrs);
        initValue();
        initListener();
    }


    private void initDefaultValue() {
        if (ToolBarValue.BACKGROUND_COLOR != 0)
            mLayout.setBackgroundColor(ToolBarValue.BACKGROUND_COLOR);
//        if (ToolBarValue.IMG_BACK != 0)
//            mDrawableBack = AppCompatResources.getDrawable(getContext(), ToolBarValue.IMG_BACK);
//        if (ToolBarValue.IMG_TITLE != 0)
//            mDrawableTitle = AppCompatResources.getDrawable(getContext(), ToolBarValue.IMG_TITLE);
//        if (ToolBarValue.IMG_MORE != 0)
//            mDrawableMore = AppCompatResources.getDrawable(getContext(), ToolBarValue.IMG_MORE);
        if (ToolBarValue.TEXT_COLOR != 0)
            mColorText = ToolBarValue.TEXT_COLOR;
        if (ToolBarValue.LINE_COLOR != 0)
            mColorLine = ToolBarValue.LINE_COLOR;
        if (ToolBarValue.LINE_HEIGHT != 0)
            mHeightLine = ToolBarValue.LINE_HEIGHT;
    }


    private void initView() {
        inflate(getContext(), R.layout.layout_tool_bar, this);
        mLayout = findViewById(R.id.layout);
        mTvBack = findViewById(R.id.v_back);
        mTvTitle = findViewById(R.id.v_title);
        mTvMore = findViewById(R.id.v_more);
        mVLine = findViewById(R.id.v_line);
    }

    private void initStyleable(AttributeSet attrs) {
        @SuppressLint("Recycle") TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.EdgeToolBarView);
        mDrawableBack = typedArray.getDrawable(R.styleable.EdgeToolBarView_img_back);
        mDrawableTitle = typedArray.getDrawable(R.styleable.EdgeToolBarView_img_title);
        mDrawableMore = typedArray.getDrawable(R.styleable.EdgeToolBarView_img_more);
        mTextBack = typedArray.getString(R.styleable.EdgeToolBarView_text_back);
        mTextTitle = typedArray.getString(R.styleable.EdgeToolBarView_text_title);
        mTextMore = typedArray.getString(R.styleable.EdgeToolBarView_text_more);
        mColorText = typedArray.getColor(R.styleable.EdgeToolBarView_color_text, mColorText);
        mColorLine = typedArray.getColor(R.styleable.EdgeToolBarView_color_line, mColorLine);
        mHeightLine = typedArray.getDimension(R.styleable.EdgeToolBarView_height_line, mHeightLine);
    }

    private void initValue() {
        mTvBack.setText(mTextBack);
        mTvTitle.setText(mTextTitle);
        mTvMore.setText(mTextMore);
        if (mDrawableBack != null)
            mTvBack.setCompoundDrawablesRelativeWithIntrinsicBounds(mDrawableBack, null, null, null);
        if (mDrawableTitle != null)
            mTvTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(mDrawableTitle, null, null, null);
        if (mDrawableMore != null)
            mTvMore.setCompoundDrawablesRelativeWithIntrinsicBounds(mDrawableMore, null, null, null);
        if (mColorText != 0) {
            mTvBack.setTextColor(mColorText);
            mTvTitle.setTextColor(mColorText);
            mTvMore.setTextColor(mColorText);
        }
        if (mColorLine != 0) {
            mVLine.setBackgroundColor(mColorLine);
        }
        if (mHeightLine != 0) {
            ViewGroup.LayoutParams layoutParams = mVLine.getLayoutParams();
            layoutParams.height = (int) mHeightLine;
            mVLine.setLayoutParams(layoutParams);
        }
    }

    private void initListener() {
        try {
            mTvBack.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity) getContext()).finish();
                }
            });
        } catch (Exception e) {
            //错误
            e.printStackTrace();
        }
    }

    //设置点击事件
    public void setMenuOnClickListener(ToolBarMenu menu, OnClickListener onClickListener) {
        switch (menu) {
            case Back:
                mTvBack.setOnClickListener(onClickListener);
                break;
            case Title:
                mTvTitle.setOnClickListener(onClickListener);
                break;
            case More:
                mTvMore.setOnClickListener(onClickListener);
                break;
            default:
                break;
        }
    }

    //获取顶部三个可操控的View
    public TextView getMenuView(ToolBarMenu menu) {
        switch (menu) {
            case Back:
                return mTvBack;
            case Title:
                return mTvTitle;
            case More:
                return mTvMore;
            default:
                return null;
        }
    }

    public View getLine() {
        return mVLine;
    }
}
