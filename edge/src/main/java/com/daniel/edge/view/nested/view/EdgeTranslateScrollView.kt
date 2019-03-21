package com.daniel.edge.view.nested.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import android.view.animation.TranslateAnimation
import androidx.core.widget.NestedScrollView
import com.daniel.edge.R
import com.daniel.edge.utils.log.EdgeLog

/**
 * @constructor 上下平移回弹的ScrollView
 */
class EdgeTranslateScrollView : NestedScrollView {

    //子视图
    private lateinit var mChildView: View
    //记录点击的点（Finger MOVE时不符合条件也需要重新记录）
    private var mRecordPoint = 0
    //滑动阻力值
    var mSlideRatio = 0.25f
    // 用于记录mChildView的初始位置
    private var mTopRect = Rect()

    //布局填充完毕时给子视图赋值
    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount > 0) {
            mChildView = getChildAt(0)
        }
    }

    //分发事件时做平移处理
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        //存储子视图默认的位置信息
        if (mTopRect.isEmpty) {
            mTopRect.set(mChildView.left, mChildView.top, mChildView.right, mChildView.bottom)
        }
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                mRecordPoint = ev.y.toInt()
            }
            MotionEvent.ACTION_MOVE -> {
                //判断是否滑动到顶点或底部
                if (((!mChildView.canScrollVertically(1) && scrollY == height) ||
                            (!mChildView.canScrollVertically(-1) && mTopRect.top == 0) || mChildView.top != 0)
                ) {
                    //记录当前点的信息
                    var currentPoint = ev.y.toInt()
                    //获取手指移动距离
                    var moveSpace = currentPoint - mRecordPoint
                    // 用于计算产生阻力后的滑动距离
                    moveSpace = (moveSpace * mSlideRatio).toInt()
                    //移动View
                    mChildView.layout(
                        mTopRect.left,
                        mTopRect.top + moveSpace,
                        mTopRect.right,
                        mTopRect.bottom + moveSpace
                    )
                } else if (mChildView.top != mTopRect.top && mChildView.left != mTopRect.left) {
                    //如果不是顶部也不是底部并且View的位置发生变化时重新归位
                    mRecordPoint = ev.y.toInt()
                    mChildView.layout(
                        mTopRect.left,
                        mTopRect.top,
                        mTopRect.right,
                        mTopRect.bottom
                    )
                } else {
                    //如果上述两种情况皆未发生则需要重新给点击的点定位，防止无法正确计算手指移动距离
                    mRecordPoint = ev.y.toInt()
                }
            }
            MotionEvent.ACTION_UP -> {
                mRecordPoint = 0
                //返回当前方向的位置动画
                if (mChildView.top != mTopRect.top || mChildView.left != mTopRect.left) {
                    backOriginalPositionAnimation()
                    //重新归位（防止与动画产生冲突）
                    mChildView.layout(
                        mTopRect.left,
                        mTopRect.top,
                        mTopRect.right,
                        mTopRect.bottom
                    )
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    // 初始化上下回弹的动画效果
    fun backOriginalPositionAnimation() {
        //采用平移动画
        var animation = TranslateAnimation(
            0.0f, 0.0f,
            mChildView.top.toFloat(), mTopRect.top.toFloat()
        )
        animation.setDuration(400)
        animation.setFillAfter(true)
        //设置阻尼动画效果
        animation.setInterpolator(OvershootInterpolator())
        mChildView.setAnimation(animation);
    }

    fun init(attrs: AttributeSet?) {
        if (attrs != null) {
            var typedArray = context.obtainStyledAttributes(attrs, R.styleable.NestedScrollView)
            mSlideRatio = typedArray.getFloat(R.styleable.NestedScrollView_slideRatio, 0.25f)
        }
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }
}