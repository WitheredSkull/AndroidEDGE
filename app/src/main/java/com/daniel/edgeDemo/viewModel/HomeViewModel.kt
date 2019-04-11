package com.daniel.edgeDemo.viewModel

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel
import com.daniel.edge.config.Edge
import com.daniel.edgeDemo.view.edgeDemo.ScrollViewActivity

class HomeViewModel : ViewModel() {
    fun scaleScrollView(view: View) {
        var intent = Intent(Edge.CONTEXT, ScrollViewActivity::class.java)
        intent.putExtra("style", "EdgeScaleScrollView")
        Edge.CONTEXT.startActivity(intent)
    }

    fun translateScrollView(view: View) {
        var intent = Intent(Edge.CONTEXT, ScrollViewActivity::class.java)
        intent.putExtra("style", "EdgeTranslateScrollView")
        Edge.CONTEXT.startActivity(intent)
    }
}