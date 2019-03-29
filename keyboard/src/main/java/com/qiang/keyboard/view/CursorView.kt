package com.qiang.keyboard.view

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.text.TextUtils
import android.util.AttributeSet
import android.util.EventLog
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.PopupWindow
import android.widget.TextView
import com.daniel.edge.utils.log.EdgeLog
import com.daniel.edge.utils.text.EdgeTextUtils
import com.qiang.keyboard.presenter.KeyboardController
import com.qiang.keyboard.service.KeyboardReceiver
import com.qiang.keyboard.service.KeyboardReceiverFunction
import com.qiang.keyboard.R
import com.qiang.keyboard.model.EventBusFunction
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.Exception

class CursorView : TextView {

    var mClickPoint = 0f
    var mHeight = 0
    var mPaint: Paint
    var mPopTextView: TextView? = null
    var mTag = ""
    var mTextLayout: View? = null
    var mTextLeftTop: String? = null
    var mTextLeftTopHeight = 0
    var mTextLeftTopWidth = 0
    var mTextPopup: PopupWindow? = null
    var mWidth = 0
    override fun onFinishInflate() {
        super.onFinishInflate()
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        View.VISIBLE
        //0打开
        //8 关闭
        when (visibility) {
            View.VISIBLE -> {
                EventBus.getDefault().register(this)
            }
            View.GONE, View.INVISIBLE -> {
                EventBus.getDefault().unregister(this)
            }
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mWidth = width
        mHeight = height
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mTextLayout = View.inflate(context, R.layout.dialog_keyboard, null)
                mTextPopup = PopupWindow(
                    mTextLayout,
                    mWidth,
                    mHeight,
                    false
                )
                mTextPopup?.isFocusable = false
                mTextPopup?.isTouchable = false
                mTextPopup?.isOutsideTouchable = true
                val location = IntArray(2)
                getLocationOnScreen(location)
                mTextLayout?.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                mPopTextView = mTextLayout?.findViewById<TextView>(R.id.tv_tip_text)
                mPopTextView?.setText(text)
                var popHeight = mTextLayout!!.measuredHeight
                var popWidth = mTextLayout!!.measuredWidth
                mTextPopup?.showAtLocation(
                    this,
                    Gravity.NO_GRAVITY,
                    (location[0]) - popWidth / 2,
                    location[1] - popHeight
                )
            }
            MotionEvent.ACTION_UP -> {
                mTextPopup?.let {
                    if (it.isShowing) {
                        it.dismiss()
                    }
                }
            }
        }

        if (!TextUtils.isEmpty(mTextLeftTop)) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    mClickPoint = event.y
                    return super.onTouchEvent(event)
                }
                MotionEvent.ACTION_MOVE -> {
                    if (mClickPoint - event.y >= mHeight / 2) {
                        mPopTextView?.setText(mTextLeftTop)
                    } else {
                        mPopTextView?.setText(text)
                    }
                    return super.onTouchEvent(event)
                }
                MotionEvent.ACTION_UP -> {
                    if (mClickPoint - event.y >= mHeight / 2) {
                        mClickPoint = 0f
                        //确认是上滑
                        performSlideUp()
                        return false
                    } else {
                        mClickPoint = 0f
                        return super.onTouchEvent(event)
                    }
                }
                else -> return super.onTouchEvent(event)
            }
        } else {
            return super.onTouchEvent(event)
        }
    }

    override fun performClick(): Boolean {
        val intent = Intent()
        intent.action = KeyboardReceiver.KeyboardAction
        intent.putExtra(KeyboardReceiver.Text, getInputText())
        setFunction(intent)
        intent.putExtra(KeyboardReceiver.IsLongClick, false)
        intent.putExtra(KeyboardReceiver.Tag, mTag)
        context?.sendBroadcast(intent)
        return super.performClick()
    }

    override fun performLongClick(): Boolean {
        val intent = Intent()
        intent.action = KeyboardReceiver.KeyboardAction
        intent.putExtra(KeyboardReceiver.Text, getInputText())
        setFunction(intent)
        intent.putExtra(KeyboardReceiver.IsLongClick, true)
        intent.putExtra(KeyboardReceiver.Tag, mTag)
        context?.sendBroadcast(intent)
        return super.performLongClick()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!TextUtils.isEmpty(mTextLeftTop)) {
            canvas.drawText(
                mTextLeftTop,
                mWidth.toFloat() / 2 + mTextLeftTopWidth / 2,
                mHeight.toFloat() / 2 - mTextLeftTopHeight / 2,
                mPaint
            )
        }
        mPaint.style = Paint.Style.STROKE
        mPaint.color = resources.getColor(R.color.colorPrimary)
        canvas.drawRect(Rect(0, 0, mWidth, mHeight), mPaint)
        var path = Path()
        path.moveTo(mWidth.toFloat() - 10, 0f)
        path.lineTo(mWidth.toFloat() - 10, mHeight.toFloat() - 10)
        path.lineTo(0f, mHeight.toFloat() - 10)
        canvas.drawPath(path,mPaint)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        //恢复状态
        KeyboardController.getTagMap().remove(mTag)
    }

    fun getInputText(): String {
        if (KeyboardController.getInstance(mTag).isCapital && text.length == 1 && EdgeTextUtils.isLowerWord(text.toString())) {
            return text.toString().toUpperCase()
        }
        return text.toString()
    }

    fun init(context: Context?, attrs: AttributeSet?) {
        if (context != null && attrs != null) {
            var typeds = context.obtainStyledAttributes(attrs, R.styleable.CursorView)
            mTextLeftTop = typeds.getString(R.styleable.CursorView_text_left_top)
            mTag = typeds.getString(R.styleable.CursorView_cursor_tag)
            if (TextUtils.isEmpty(mTag)) {
                throw Exception("CursorView没有设置标签")
            }
            if (!TextUtils.isEmpty(mTextLeftTop)) {
                var rect = Rect()
                mPaint.getTextBounds("w", 0, "w".length, rect)
                mTextLeftTopHeight = rect.height()
                mTextLeftTopWidth = rect.width()
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefresh(refresh: EventBusFunction.Refresh) {
        if (!refresh.tag.equals(mTag)) {
            return
        }
        if (KeyboardController.getInstance(mTag).isShift && text.equals("Shift")) {
            setBackgroundColor(resources.getColor(R.color.colorPrimary))
        } else {
            setBackgroundColor(resources.getColor(R.color.white))
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReception(function: EventBusFunction.SwitchButton) {
        if (!function.tag.equals(mTag)) {
            return
        }
        val textLeftTop = mTextLeftTop
        if (!TextUtils.isEmpty(textLeftTop)) {
            mTextLeftTop = text.toString()
            setText(textLeftTop)
        } else if (text.length == 1 && EdgeTextUtils.isUpperWord(text.toString())) {
            if (mTextLeftTop != null && EdgeTextUtils.isUpperWord(mTextLeftTop!!)) {
                mTextLeftTop = mTextLeftTop!!.toUpperCase()
            }
            setText(text.toString().toLowerCase())
        } else if (text.length == 1 && EdgeTextUtils.isLowerWord(text.toString())) {
            if (mTextLeftTop != null && EdgeTextUtils.isLowerWord(mTextLeftTop!!)) {
                mTextLeftTop = mTextLeftTop!!.toLowerCase()
            }
            setText(text.toString().toUpperCase())
        }
    }

    fun performSlideUp(): Boolean {
        val intent = Intent()
        intent.action = KeyboardReceiver.KeyboardAction
        intent.putExtra(KeyboardReceiver.Text, mTextLeftTop)
        setFunction(intent)
        intent.putExtra(KeyboardReceiver.IsLongClick, false)
        intent.putExtra(KeyboardReceiver.Tag, mTag)
        context?.sendBroadcast(intent)
        return super.performClick()
    }

    fun setFunction(intent: Intent) {
        when (text) {
            "←" ->
                intent.putExtra(KeyboardReceiver.Function, KeyboardReceiverFunction.Delete.ordinal)
            "space" ->
                intent.putExtra(KeyboardReceiver.Function, KeyboardReceiverFunction.Space.ordinal)
            "123" -> intent.putExtra(KeyboardReceiver.Function, KeyboardReceiverFunction.Number.ordinal).apply {
                KeyboardController.getInstance(mTag)
                    .isNumber = !KeyboardController.getInstance(mTag).isNumber
                if (KeyboardController.getInstance(mTag).isNumber) {
                    setBackgroundColor(resources.getColor(R.color.colorPrimary))
                } else {
                    setBackgroundColor(resources.getColor(R.color.white))
                }
            }
            "Enter" ->
                intent.putExtra(KeyboardReceiver.Function, KeyboardReceiverFunction.Enter.ordinal)
            "Caps" -> {
                intent.putExtra(KeyboardReceiver.Function, KeyboardReceiverFunction.Cap.ordinal)
                KeyboardController.getInstance(mTag)
                    .isCapital = !KeyboardController.getInstance(mTag).isCapital
                if (KeyboardController.getInstance(mTag).isCapital) {
                    setBackgroundColor(resources.getColor(R.color.colorPrimary))
                } else {
                    setBackgroundColor(resources.getColor(R.color.white))
                }
            }
            "Shift" -> {
                intent.putExtra(KeyboardReceiver.Function, KeyboardReceiverFunction.Shift.ordinal)
                KeyboardController.getInstance(mTag).isShift = !KeyboardController.getInstance(mTag).isShift
                EventBus.getDefault()
                    .post(EventBusFunction.SwitchButton(mTag, KeyboardController.getInstance(mTag).isShift))
                EventBus.getDefault().post(EventBusFunction.Refresh(mTag))
            }
            else ->
                intent.putExtra(KeyboardReceiver.Function, KeyboardReceiverFunction.Input.ordinal)
        }
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.color = textColors.defaultColor
        mPaint.textSize = textSize
        //禁止父控件拦截点击事件
        setOnClickListener { }
        //禁止父控件拦截长按事件
        setOnLongClickListener {
            true
        }
    }
}