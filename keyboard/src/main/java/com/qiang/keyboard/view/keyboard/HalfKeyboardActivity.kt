package com.qiang.keyboard.view.keyboard

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.qiang.keyboard.R

class HalfKeyboardActivity : BaseKeyboardActivity() {
    lateinit var mTVText: TextView
    lateinit var mLLLeft: LinearLayout
    lateinit var mLLRight: LinearLayout
    var mAwayType: AwayKeyboardType =
        AwayKeyboardType.Left

    override fun appendText(text: String) {
        mTVText.text = text
    }

    override fun initAction(): String {
        return if (mAwayType == AwayKeyboardType.Right) {
            getString(R.string.action_half_right)
        } else {
            getString(R.string.action_half_left)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        intent?.let {
            mAwayType = AwayKeyboardType.values()[it.getIntExtra(
                ACTION_AWAY, 0)]
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_half_keyboard)
        mTVText = findViewById(R.id.tv_message)
        mLLLeft = findViewById(R.id.ll_word_left)
        mLLRight = findViewById(R.id.ll_word_right)
        when (mAwayType) {
            AwayKeyboardType.Left -> {
                mLLLeft.visibility = View.VISIBLE
                mLLRight.visibility = View.GONE
            }
            AwayKeyboardType.Right -> {
                mLLLeft.visibility = View.GONE
                mLLRight.visibility = View.VISIBLE
            }
        }
    }

    enum class AwayKeyboardType {
        Left, Right
    }

    companion object {
        var ACTION_AWAY = "away"
    }
}
