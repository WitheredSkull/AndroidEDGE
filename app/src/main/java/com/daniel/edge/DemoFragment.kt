package com.daniel.edge

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * @Author:      Daniel
 * @Date:        2018/11/29 17:18
 * @Description:
 *
 */
class DemoFragment:Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return View.inflate(context,R.layout.activity_main,null)
    }
}