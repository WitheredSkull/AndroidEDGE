package com.daniel.edge.view.slideLayout.view

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.RelativeLayout
import androidx.annotation.Nullable
import androidx.core.view.children
import com.daniel.edge.R
import com.daniel.edge.view.slideLayout.model.EdgeSlideDirection
import com.daniel.edge.view.slideLayout.model.OnEdgeSlideListener

class EdgeSlideRelativeLayout : RelativeLayout {

    var mDirection = EdgeSlideDirection.Left
    //本次的点击位置
    var mDownPoint = 0
    var mOnEdgeSlideListener: OnEdgeSlideListener? = null
    var mTopViewId = R.id.top
    var mTopView: View? = null
    var mTopViewBottom = 0
    var mTopViewLeft = 0
    var mTopViewRight = 0
    var mTopViewTop = 0
    var mTotalSize = 0
    //上次顶层View的位置记录
    var mUpTopViewBottom = 0
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
                //移动了之后需要归位,这里获取到是开启还是关闭
                if (mTopView!!.left != mTopViewLeft || mTopView!!.top != mTopViewTop) {
                    var isOpen = when (mDirection) {
                        EdgeSlideDirection.Left -> mTopView!!.right <= mTopView!!.width - mTotalSize / 2
                        EdgeSlideDirection.Top -> mTopView!!.bottom <= mTopView!!.height - mTotalSize / 2
                        EdgeSlideDirection.Right -> mTopView!!.left >= mTotalSize / 2
                        EdgeSlideDirection.Bottom -> mTopView!!.top >= mTotalSize / 2
                    }
                    animal(isOpen)
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
    fun animal(isOpen: Boolean) {
        var interpolator = object : AccelerateInterpolator() {
            override fun getInterpolation(input: Float): Float {
                when (mDirection) {
                    EdgeSlideDirection.Left, EdgeSlideDirection.Right -> {
                        var leftPoint = if (isOpen) {
                            if (input == 1f) {
                                mOnEdgeSlideListener?.open()
                            }
                            if (mDirection == EdgeSlideDirection.Left) {
                                var point = mTopView!!.right
                                (mTopViewLeft - (mTopView!!.width - point) - (mTotalSize - (mTopView!!.width - point)) * input).toInt()
                            } else {
                                var point = mTopView!!.left
                                (mTopViewLeft + point + ((mTotalSize - point) * input)).toInt()
                            }
                        } else {
                            if (input == 1f) {
                                mOnEdgeSlideListener?.close()
                            }
                            var point = mTopView!!.right
                            (mTopViewLeft - ((mTopView!!.width - point) - (mTopView!!.width - point) * input)).toInt()
                        }
                        mTopView!!.layout(
                            leftPoint,
                            mTopView!!.top,
                            leftPoint + mTopView!!.width,
                            mTopView!!.bottom
                        )
                    }
                    EdgeSlideDirection.Top, EdgeSlideDirection.Bottom -> {
                        var topPoint = if (isOpen) {
                            if (input == 1f) {
                                mOnEdgeSlideListener?.open()
                            }
                            if (mDirection == EdgeSlideDirection.Top) {
                                var point = mTopView!!.bottom
                                (mTopViewTop - (mTopView!!.height - point) - (mTotalSize - (mTopView!!.height - point)) * input).toInt()
                            } else {
                                var point = mTopView!!.top
                                (mTopViewTop + point + ((mTotalSize - point) * input)).toInt()
                            }
                        } else {
                            if (input == 1f) {
                                mOnEdgeSlideListener?.close()
                            }
                            var point = mTopView!!.bottom
                            (mTopViewTop - ((mTopView!!.height - point) - (mTopView!!.height - point) * input)).toInt()
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
        animal(false)
    }

    fun initSlideView() {
        mTopView = children.find {
            if (it.id == mTopViewId) {
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
            if (it.id != mTopViewId) {
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
        animal(true)
    }

    fun init(@Nullable attrs: AttributeSet?) {
        if (attrs != null) {
            var type = context.obtainStyledAttributes(attrs, R.styleable.EdgeSlideRelativeLayout)
            var direction = type.getInt(R.styleable.EdgeSlideRelativeLayout_direction, 0)
            mDirection = EdgeSlideDirection.values().get(direction)
            mTopViewId = type.getResourceId(R.styleable.EdgeSlideRelativeLayout_topViewId, R.id.top)
        }
    }

    constructor(context: Context?) : super(context) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }
}