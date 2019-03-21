package com.daniel.edge.view.edgeDemo

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.daniel.edge.R
import com.daniel.edge.utils.toast.EdgeToastUtils
import com.daniel.edge.view.edgeDemo.model.NumberAdapter
import com.daniel.edge.window.dialog.IDialogCallback
import com.daniel.edge.window.dialog.bottomSheetDialog.EdgeBottomSheetDialogFragment
import com.daniel.edge.window.dialog.model.EdgeBottomSheetConfig
import kotlinx.android.synthetic.main.activity_scroll_view.*

class ScrollViewActivity : AppCompatActivity() {
    var list = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll_view)
        toolbar.setNavigationOnClickListener {
            var config = EdgeBottomSheetConfig(supportFragmentManager, R.layout.window_number_selector)
            config.iDialogCallback = object : IDialogCallback {
                override fun onDialogDisplay(v: View?, dialog: Dialog) {
                    v?.findViewById<Button>(R.id.confirm)?.setOnClickListener {
                        sv_translate.mSlideRatio = v?.findViewById<TextView>(R.id.et_number).text.toString().toFloat()
                        sv_scale.mSlideRatio = v?.findViewById<TextView>(R.id.et_number).text.toString().toFloat()
                        dialog.dismiss()
                    }
                }

                override fun onDialogDismiss() {
                }

            }
            EdgeBottomSheetDialogFragment.build(config)
                .show()
        }
        if (intent != null) {
            when (intent.getStringExtra("style")) {
                "EdgeTranslateScrollView" -> {
                    sv_translate.visibility = View.VISIBLE
                    sv_scale.visibility = View.GONE
                }
                "EdgeScaleScrollView" -> {
                    sv_translate.visibility = View.GONE
                    sv_scale.visibility = View.VISIBLE
                }
            }
        }
        setData()
    }

    fun setData() {
        for (index in 0..20) {
            list.add("这是第 $index 个选项")
        }
        rv.layoutManager = LinearLayoutManager(this)
        rv2.layoutManager = LinearLayoutManager(this)
        var adapter = NumberAdapter(this, list)
        rv.adapter = adapter
        rv2.adapter = adapter
    }
}
