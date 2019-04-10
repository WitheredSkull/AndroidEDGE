package com.daniel.edge.view.nested.view

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.*
import androidx.core.widget.NestedScrollView
import com.daniel.edge.R
import com.daniel.edge.utils.log.EdgeLog

/**
 * @constructor 上下放大缩小的ScrollView
 */
class EdgeScaleScrollView : NestedScrollView {

    //子视图
    private lateinit var mChildView: View
    //记录点击的点（Finger MOVE时不符合条件也需要重新记录）
    private var mRecordPoint = 0
    //滑动阻力值
    var mSlideRatio = 0.25f
    //是否为向上滑动
    private var isUp = true

    //布局填充完毕时给子视图赋值
    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount > 0) {
            mChildView = getChildAt(0)
        }
    }

    //分发事件时做缩放处理
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                isUp = true
                mRecordPoint = ev.y.toInt()
            }
            MotionEvent.ACTION_MOVE -> {
                //判断是否滑动到顶点或底部
                if (((!mChildView.canScrollVertically(1) && scrollY == height) ||
                            (!mChildView.canScrollVertically(-1)) || mChildView.top != 0)
                ) {
                    //记录当前点的信息
                    var currentPoint = ev.y.toInt()
                    //计算手指移动距离以及结合滑动阻力后的距离
                    var moveSpace = ((currentPoint - mRecordPoint) * mSlideRatio).toInt()
                    //判断滑动方向，并且需要设置Y轴的中心点
                    if (moveSpace < 0) {
                        isUp = false
                        mChildView.pivotY = mChildView.height.toFloat()
                        moveSpace = -moveSpace
                    } else {
                        isUp = true
                        mChildView.pivotY = 0f
                    }
                    //开始缩放
                    mChildView.scaleY = (moveSpace.toFloat() + mChildView.height) / mChildView.height
                } else {
                    isUp = true
                    mRecordPoint = ev.y.toInt()
                }
            }
            MotionEvent.ACTION_UP -> {
                mRecordPoint = 0
                //返回当前方向的位置动画
                if (mChildView.scaleY != 0f) {
                    backOriginalPositionAnimation()
                    //重新归位（防止与动画产生冲突）
                    mChildView.scaleY = 1.0f
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    // 初始化上下回弹的动画效果
    fun backOriginalPositionAnimation() {
        //设置归位缩放动画，需要根据滑动方向设置主Y点
        var animation = ScaleAnimation(
            1f, 1f,
            mChildView.scaleY, 1.0f,
            0f, if (isUp) {
                0f
            } else {
                mChildView.height.toFloat()
            }
        )
        animation.setDuration(400)
//        animation.setFillAfter(true)
        //设置阻尼动画效果
        animation.setInterpolator(DecelerateInterpolator())
        mChildView.startAnimation(animation)
//        mChildView.startAnimation(animation)
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