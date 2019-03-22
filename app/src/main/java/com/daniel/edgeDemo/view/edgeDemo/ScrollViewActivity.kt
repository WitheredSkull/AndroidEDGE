package com.daniel.edgeDemo.view.edgeDemo

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.daniel.edgeDemo.R
import com.daniel.edge.utils.toast.EdgeToastUtils
import com.daniel.edgeDemo.view.edgeDemo.model.NumberAdapter
import com.daniel.edge.window.dialog.IDialogCallback
import com.daniel.edge.window.dialog.bottomSheetDialog.EdgeBottomSheetDialogFragment
import com.daniel.edge.window.dialog.bottomSheetDialog.model.OnEdgeDialogClickListener
import kotlinx.android.synthetic.main.activity_scroll_view.*

class ScrollViewActivity : AppCompatActivity() {
    var list = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll_view)
        toolbar.setNavigationOnClickListener {
            EdgeBottomSheetDialogFragment.build(supportFragmentManager, R.layout.window_number_selector)
                .setDimAmount(0f)
                .addOnClick(object :OnEdgeDialogClickListener{
                    override fun onClick(view: View, dialog: Dialog) {
                        sv_translate.mSlideRatio = view.findViewById<TextView>(R.id.et_number).text.toString().toFloat()
                        sv_scale.mSlideRatio = view.findViewById<TextView>(R.id.et_number).text.toString().toFloat()
                        dialog.dismiss()
                    }

                },R.id.confirm)
                .setDialogCallback(object : IDialogCallback {
                    override fun onDialogDismiss() {

                    }

                    override fun onDialogDisplay(v: View?, dialog: Dialog) {

                    }
                }).show()
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
