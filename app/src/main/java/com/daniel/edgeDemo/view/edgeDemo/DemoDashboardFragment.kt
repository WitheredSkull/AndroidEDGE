package com.daniel.edgeDemo.view.edgeDemo


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.daniel.edge.view.webView.EdgeWebViewUtils

import com.daniel.edgeDemo.R
import com.daniel.edgeDemo.utils.ViewHolderUtils
import kotlinx.android.synthetic.main.fragment_demo_dashboard.*


/**
 * A simple [Fragment] subclass.
 *
 */
class DemoDashboardFragment : Fragment() {
    lateinit var mView: View
    lateinit var mViewHolder: ViewHolderUtils
    lateinit var mWebViewUtils: EdgeWebViewUtils

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_demo_dashboard, container, false)
        mViewHolder = ViewHolderUtils(mView)
        mWebViewUtils = EdgeWebViewUtils.Build(activity!!, mViewHolder.webView, R.id.webView)
//        mWebViewUtils.initDefaultChromeClient()
        mWebViewUtils.initDefaultChromeClient()
            .initDefaultClient()
            .initDefaultSetting()
            .load("https://www.baidu.com/")
            .build()
        return mView
    }

    fun onBack(): Boolean {
        if (mWebViewUtils.webView.canGoBack()) {
            mWebViewUtils.webView.goBack()
            return true
        } else {
            return false
        }
    }

}
