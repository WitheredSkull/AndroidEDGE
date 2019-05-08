package com.qiang.keyboard.view.keyboard

import android.os.Bundle
import com.daniel.edge.utils.text.EdgeTextUtils
import com.daniel.edge.utils.toast.EdgeToastUtils
import com.qiang.keyboard.R
import com.qiang.keyboard.service.KeyboardReceiver
import com.qiang.keyboard.view.base.BaseKeyboardActivity
import kotlinx.android.synthetic.main.activity_calculate.*
import java.lang.Exception
import java.math.BigDecimal

class CalculateActivity : BaseKeyboardActivity() {

    var isNeedClear: Boolean = false
    var mNum1: Double = 0.0
    var mNum2: Double = 0.0
    var mResult: Double = 0.0
    var mSymbol: String = ""
    var mUpChart = ""
    override fun appendText(text: String) {

    }

    override fun onChart(text: String) {
        try {
            when (text) {
                "" -> {
                    if (tv_text.text.isNullOrEmpty()) {
                        tv_text.text = "0."
                    }
                }
                Symbol.PLUS, Symbol.MULTIPLY, Symbol.DIVIDE, Symbol.MINUS -> {
                    if (!mSymbol.isNullOrEmpty() && !mUpChart.isNullOrEmpty() && !mUpChart.equals(Symbol.PLUS)
                        && !mUpChart.equals(Symbol.MULTIPLY)
                        && !mUpChart.equals(Symbol.DIVIDE)
                        && !mUpChart.equals(Symbol.MINUS)
                    ) {
                        mNum1 = calculate(mSymbol, mNum1, tv_text.text.toString().toDouble())
                    } else {
                        mNum1 = tv_text.text.toString().toDouble()
                    }
                    mSymbol = text
                    isNeedClear = true
                    tv_text.text = EdgeTextUtils.subZeroAndDot(mNum1.toString())
                }
                "AC" -> {
                    clear()
                }
                "＝" -> {
                    mNum2 = tv_text.text.toString().toDouble()
                    mResult = calculate(mSymbol, mNum1, mNum2)
                    tv_text.text = EdgeTextUtils.subZeroAndDot(mResult.toString())
                    mSymbol = ""
                }
                "Back" -> {
                    if (tv_text.text.equals("Infinity")) {
                        clear()
                    }
                    if (tv_text.length() > 0) {
                        tv_text.text = EdgeTextUtils.subZeroAndDot(tv_text.text.substring(0, tv_text.length() - 1))
                    }
                }
                "Send" -> {
                    intent.action = KeyboardReceiver.KeyboardAction
                    intent.putExtra(KeyboardReceiver.Text, tv_text.text.toString())
                    intent.putExtra(KeyboardReceiver.IsLongClick, false)
                    intent.putExtra(KeyboardReceiver.Tag, "Send")
                    sendBroadcast(intent)
                }
                else -> {
                    if (!mSymbol.isNullOrEmpty() && isNeedClear) {
                        isNeedClear = false
                        tv_text.text = text
                    } else {
                        isNeedClear = false
                        tv_text.text = tv_text.text.toString() + text
                    }
                }
            }
            mUpChart = text
        } catch (e: Exception) {
            EdgeToastUtils.getInstance().show("计算有误")
            clear()
            e.printStackTrace()
        } finally {

        }
    }

    override fun initAction(): String {
        return getString(R.string.action_calculate)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculate)
    }

    fun calculate(symbol: String, num1: Double, num2: Double): Double {
        when (symbol) {
            Symbol.PLUS -> {
                return BigDecimal(num1).plus(BigDecimal(num2)).toDouble()
            }
            Symbol.MINUS -> {
                return BigDecimal(num1).minus(BigDecimal(num2)).toDouble()
            }
            Symbol.MULTIPLY -> {
                return BigDecimal(num1).multiply(BigDecimal(num2)).toDouble()
            }
            Symbol.DIVIDE -> {
                return BigDecimal(num1).divide(BigDecimal(num2), 12, BigDecimal.ROUND_CEILING).toDouble()
            }
            else -> return 0.0
        }
    }

    fun clear() {
        mNum1 = 0.0
        mNum2 = 0.0
        mSymbol = ""
        tv_text.text = ""
    }

    object Symbol {
        val DIVIDE = "÷"
        val MINUS = "－"
        val MULTIPLY = "×"
        val PLUS = "＋"
    }
}
