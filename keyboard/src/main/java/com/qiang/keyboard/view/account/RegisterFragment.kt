package com.qiang.keyboard.view.account


import androidx.lifecycle.ViewModelProviders
import com.daniel.edge.utils.log.EdgeLog

import com.qiang.keyboard.R
import com.qiang.keyboard.databinding.FragmentRegisterBinding
import com.qiang.keyboard.view.base.BaseFragment
import com.qiang.keyboard.viewModel.AccountViewModel

class RegisterFragment : BaseFragment<FragmentRegisterBinding, AccountViewModel>() {
    override fun initLayout(): Int {
        return R.layout.fragment_register
    }

    override fun initViewModel(dataBinding: FragmentRegisterBinding) {
        setViewModel(ViewModelProviders.of(activity!!).get(AccountViewModel::class.java)).apply {
            getDataBinding().viewModel = this
        }
    }

    override fun initData() {
    }

}
