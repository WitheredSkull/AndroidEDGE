package com.daniel.edge.view.edgeDemo

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.telephony.TelephonyManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView

import com.daniel.edge.R
import com.daniel.edge.databinding.FragmentDemoHomeBinding
import com.daniel.edge.utils.log.EdgeLog
import com.daniel.edge.viewModel.HomeViewModel

class DemoHomeFragment : Fragment() {
    lateinit var dataBinding:FragmentDemoHomeBinding
    lateinit var viewModel:HomeViewModel

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var tm = context!!.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        EdgeLog.show(javaClass,"本机号码","${tm.line1Number} 123")
        dataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_demo_home, container, false)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        dataBinding.viewModel = viewModel
        dataBinding.lifecycleOwner = this
        return dataBinding.root
    }
}
