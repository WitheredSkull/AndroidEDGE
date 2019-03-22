package com.daniel.edgeDemo.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.daniel.edge.utils.log.EdgeLog

class SlideView : View {
    121321
    var initLeft = 0
    var initTop = 0
    var initRight = 0
    var initBottom = 0
    var isInitPoint = false

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (!isInitPoint){
            EdgeLog.show(javaClass, "初始值", "$left + $top + $right + $bottom")
            initLeft = left
            initTop = top
            initRight = right
            initBottom = bottom
            isInitPoint = true
        }
    }

    var moveDistance = 0f
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                moveDistance += event.x
                EdgeLog.show(javaClass, "移动", "X = ${event.x}, Y = ${event.y}, Width = ${width}")
                layout(left + event.x.toInt() - width / 2, top, right + event.x.toInt() - width / 2, bottom)
            }
            MotionEvent.ACTION_UP -> {
                EdgeLog.show(javaClass, "抬起", "$moveDistance")
                layout(initLeft,initTop,initRight,initBottom)
                if (moveDistance.toInt() == 0) {
                    performClick()
                }
                moveDistance = 0f
            }
        }
        return true
    }
}