package com.shuanglu.edge.View.Banner.TextBanner.View

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import com.shuanglu.edge.View.Banner.TextBanner.Model.TextBannerAdapter
import android.widget.FrameLayout


/**
 * 创建人 Daniel
 * 时间   2018/11/12.
 * 简介   xxx
 */
class TextBannerView<T>(context: Context, attributeSet: AttributeSet) : FrameLayout(context, attributeSet) {

    var mWidth: Int = 0
    var mHeight: Int = 0
    var adapter: TextBannerAdapter<T>? = null
    var position: Int = 0
    var mHandler = Handler()
    var runable: Runnable = object : Runnable {
        override fun run() {
            if (position < adapter!!.list.size - 1) {
                ++position
            } else {
                position = 0
            }
            if (childCount > 0) {
                var view0 = getChildAt(0)
                var view1 = adapter!!.getItem(adapter!!.list.get(position), position)
                addView(view1)
                var animator0 = ObjectAnimator.ofFloat(view0, "translationY", 0f, -mHeight.toFloat())
                animator0.duration = 1000
                animator0.start()
                var animator1 = ObjectAnimator.ofFloat(view1, "translationY", mHeight.toFloat(), 0f)
                animator1.duration = 1500
                animator1.start()
                animator1.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {

                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        removeView(view0)
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }

                    override fun onAnimationStart(animation: Animator?) {
                    }
                })
            } else {
                addView(adapter!!.getItem(adapter!!.list.get(position), position))
            }
            mHandler.postDelayed(this, 2000)
        }
    }

    fun startRoll() {
        mHandler.postDelayed(runable, 3000)
    }

    fun stopRoll() {
        mHandler.removeCallbacks(runable)
    }

    fun setOnItemClick() {
        adapter
    }

//    override fun roll(arg: T) {
//        if (childCount > 0) {
//            var view0 = getChildAt(0)
//            var view1 = adapter!!.getItem(adapter!!.list.get(task!!.position), task!!.position)
//            addView(view1)
//
//            var animator0 = ObjectAnimator.ofFloat(view0, "translationY", 0f, -mHeight.toFloat())
////            var aniamtion0 = TranslateAnimation(0f, 0f, 0f, -mHeight.toFloat())
//            animator0.duration = 1000
//            animator0.start()
//            var animator1 = ObjectAnimator.ofFloat(view1, "translationY", mHeight.toFloat(), 0f)
////            var animation1 = TranslateAnimation(0f, 0f, 0f, -300f)
////            var animation1 = TranslateAnimation(0f, 0f, mHeight.toFloat(), 0f - view.measuredHeight / 2 )
//            animator1.duration = 1500
////            view1.animation = animation1
//            animator1.start()
//            animator1.addListener(object : Animator.AnimatorListener {
//                override fun onAnimationRepeat(animation: Animator?) {
//
//                }
//
//                override fun onAnimationEnd(animation: Animator?) {
//                    removeView(view0)
//                }
//
//                override fun onAnimationCancel(animation: Animator?) {
//                }
//
//                override fun onAnimationStart(animation: Animator?) {
//                }
//            })
//        } else {
//            addView(adapter!!.getItem(adapter!!.list.get(task!!.position), task!!.position))
//        }
//    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        mWidth = getWidth()
        mHeight = getHeight()
    }
}