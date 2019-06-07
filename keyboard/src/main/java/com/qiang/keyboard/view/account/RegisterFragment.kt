package com.qiang.keyboard.view.account


import androidx.lifecycle.ViewModelProviders

import com.qiang.keyboard.R
import com.qiang.keyboard.databinding.FragmentRegisterBinding
import com.qiang.keyboard.view.base.BaseVMFragment
import com.qiang.keyboard.viewModel.AccountViewModel

class RegisterFragment : BaseVMFragment<FragmentRegisterBinding, AccountViewModel>() {
    override fun enableViewModel(): Boolean = true


    override fun initLayout(): Int {
        return R.layout.fragment_register
    }

    override fun initViewModel(): AccountViewModel? {
        return ViewModelProviders.of(activity!!).get(AccountViewModel::class.java)
    }

    override fun initData() {
    }

    override fun initListener() {
    }

    override fun onDestroyView() {
        getViewModel()?.closeCountDownTime()
        super.onDestroyView()
    }
}
