package com.daniel.edgeDemo.view.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.daniel.edgeDemo.R
import com.daniel.edgeDemo.constant.App
import com.daniel.edgeDemo.dagger.component.DaggerAccountComponent
import com.daniel.edgeDemo.databinding.RegisterFragmentBinding
import com.daniel.edgeDemo.viewModel.AccountViewModel

class RegisterFragment : Fragment() {
    var mDataBinding: RegisterFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DaggerAccountComponent.builder().appComponent(App.appComponent).build().inject(this)
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.register_fragment, container, false)
        mDataBinding?.viewModel = getViewModel()
        mDataBinding?.lifecycleOwner = this
        return mDataBinding?.root
    }


    fun getViewModel(): AccountViewModel? {
        activity?.let {
            return (it as AccountActivity).mViewModel
        }
        return null
    }
}
