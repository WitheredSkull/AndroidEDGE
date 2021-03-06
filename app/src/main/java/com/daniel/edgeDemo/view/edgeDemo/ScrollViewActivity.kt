package com.daniel.edgeDemo.view.edgeDemo

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.daniel.edgeDemo.R
import com.daniel.edgeDemo.view.edgeDemo.model.NumberAdapter
import com.daniel.edge.window.dialog.IEdgeDialogCallback
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
                .addOnClick(object : OnEdgeDialogClickListener {
                    override fun onClick(parent: View, view: View, dialog: EdgeBottomSheetDialogFragment) {
                        sv_translate.mSlideRatio =
                            parent?.findViewById<TextView>(R.id.et_number)?.text.toString().toFloat()
                        sv_scale.mSlideRatio = parent?.findViewById<TextView>(R.id.et_number)?.text.toString().toFloat()
                        dialog.dismiss()
                    }
                }, R.id.confirm)
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
        for (index in 0..5) {
            list.add("这是第 $index 个选项")
        }
        rv.layoutManager = LinearLayoutManager(this)
        rv2.layoutManager = LinearLayoutManager(this)
        var adapter = NumberAdapter(this, list)
        rv.adapter = adapter
        rv2.adapter = adapter
    }
}
