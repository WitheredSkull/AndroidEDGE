package com.daniel.edge.view.slideLayout

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.RelativeLayout
import androidx.core.view.children
import com.daniel.edge.R

class EdgeSlideLinearLayout : RelativeLayout {

    var mDirection = EdgeSlideDirection.Left
    //本次的点击位置
    var mDownPoint = 0
    var mOnEdgeSlideListener: OnEdgeSlideListener? = null
    var mTopView: View? = null
    var mTopViewBottom = 0
    var mTopViewLeft = 0
    var mTopViewRight = 0
    var mTopViewTop = 0
    var mTotalSize = 0
    var mUpTopViewBottom = 0
    //上次顶层View的左边位置
    var mUpTopViewLeft = 0
    var mUpTopViewRight = 0
    var mUpTopViewTop = 0
    override fun onTouchEvent(event: MotionEvent): Boolean {
        //获取顶层View
        if (event.action == MotionEvent.ACTION_DOWN && mTopView == null) {
            initSlideView()
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                when (mDirection) {
                    EdgeSlideDirection.Top, EdgeSlideDirection.Bottom -> {
                        mDownPoint = event.y.toInt()
                    }
                    EdgeSlideDirection.Left, EdgeSlideDirection.Right -> {
                        mDownPoint = event.x.toInt()
                    }
                }
                mUpTopViewLeft = mTopView!!.left
                mUpTopViewRight = mTopView!!.right
                mUpTopViewTop = mTopView!!.top
                mUpTopViewBottom = mTopView!!.bottom
            }
            MotionEvent.ACTION_MOVE -> {
                when (mDirection) {
                    EdgeSlideDirection.Left, EdgeSlideDirection.Right -> {
                        var moveLeft = mUpTopViewLeft + event.x.toInt() - mDownPoint
                        var moveRight = moveLeft + mTopView!!.width
                        if ((moveLeft <= 0 && mDirection == EdgeSlideDirection.Right) || (moveRight >= width && mDirection == EdgeSlideDirection.Left)) {
                            mTopView!!.layout(
                                mTopViewLeft,
                                mTopViewTop,
                                mTopViewRight,
                                mTopViewBottom
                            )
                        } else {
                            mTopView!!.layout(
                                mUpTopViewLeft + event.x.toInt() - mDownPoint,
                                mTopView!!.top,
                                mUpTopViewLeft + event.x.toInt() - mDownPoint + mTopView!!.width,
                                mTopView!!.bottom
                            )
                        }
                    }
                    EdgeSlideDirection.Top, EdgeSlideDirection.Bottom -> {
                        var moveTop = mUpTopViewTop + event.y.toInt() - mDownPoint
                        var moveBottom = moveTop + mTopView!!.height
                        if ((moveTop <= 0 && mDirection == EdgeSlideDirection.Bottom) || (moveBottom >= height && mDirection == EdgeSlideDirection.Top)) {
                            mTopView!!.layout(
                                mTopViewLeft,
                                mTopViewTop,
                                mTopViewRight,
                                mTopViewBottom
                            )
                        } else {
                            mTopView!!.layout(
                                mTopView!!.left,
                                moveTop,
                                mTopView!!.right,
                                moveBottom
                            )
                        }
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                //移动了之后需要归位
                if (mDirection == EdgeSlideDirection.Left || mDirection == EdgeSlideDirection.Right) {
                    if (mTopView!!.left != mTopViewLeft) {
                        animal(mDownPoint - event.x.toInt())
                    }
                } else if (mDirection == EdgeSlideDirection.Top || mDirection == EdgeSlideDirection.Bottom) {
                    if (mTopView!!.top != mTopViewTop) {
                        animal(mDownPoint - event.y.toInt())
                    }
                } else {
                    //触发点击事件
                    performClick()
                }
                mDownPoint = 0
                mUpTopViewLeft = mTopView!!.left
                mUpTopViewRight = mTopView!!.right
                mUpTopViewTop = mTopView!!.top
                mUpTopViewBottom = mTopView!!.bottom
            }
        }
        return true
    }

    @SuppressLint("ObjectAnimatorBinding")
    fun animal(move: Int) {
        var interpolator = object : AccelerateInterpolator() {
            override fun getInterpolation(input: Float): Float {
                when (mDirection) {
                    EdgeSlideDirection.Left, EdgeSlideDirection.Right -> {
                        var topPoint = if (move >= mTotalSize / 2) {
                            mOnEdgeSlideListener?.open()
                            mTopViewLeft - mTotalSize
                        } else {
                            mOnEdgeSlideListener?.close()
                            mTopViewLeft - (move - (move * input)).toInt()
                        }
                        mTopView!!.layout(
                            topPoint,
                            mTopView!!.top,
                            topPoint + mTopView!!.width,
                            mTopView!!.bottom
                        )
                    }
                    EdgeSlideDirection.Top, EdgeSlideDirection.Bottom -> {
                        var topPoint = if (move >= mTotalSize / 2) {
                            mOnEdgeSlideListener?.open()
                            mTopViewTop - mTotalSize
                        } else {
                            mOnEdgeSlideListener?.close()
                            mTopViewTop - (move - (move * input)).toInt()
                        }
                        mTopView!!.layout(
                            mTopView!!.left,
                            topPoint,
                            mTopView!!.right,
                            topPoint + mTopView!!.height
                        )
                    }
                }
                return input
            }
        }
        var animator = ObjectAnimator.ofFloat(null, "translationY", 500f)
        animator.interpolator = interpolator
        animator.start()
    }

    //关闭
    fun close() {
        animal(0)
    }

    fun initSlideView() {
        mTopView = children.find {
            if (it.id == R.id.top) {
                mTopViewLeft = it.left
                mTopViewTop = it.top
                mTopViewRight = it.right
                mTopViewBottom = it.bottom
                true
            } else {
                false
            }
        }
        children.forEach {
            if (it.id != R.id.top) {
                if (mDirection == EdgeSlideDirection.Left || mDirection == EdgeSlideDirection.Right) {
                    mTotalSize += it.width
                } else if (mDirection == EdgeSlideDirection.Top || mDirection == EdgeSlideDirection.Bottom) {
                    mTotalSize += it.height
                }
            }
        }
    }

    //打开
    fun open() {
        animal(mTotalSize)
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}