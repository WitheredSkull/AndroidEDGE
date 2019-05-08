package com.qiang.keyboard.view.account

import androidx.lifecycle.ViewModelProviders
import com.daniel.edge.utils.log.EdgeLog

import com.qiang.keyboard.R
import com.qiang.keyboard.databinding.FragmentLoginBinding
import com.qiang.keyboard.view.base.BaseFragment
import com.qiang.keyboard.viewModel.AccountViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

class LoginFragment : BaseFragment<FragmentLoginBinding,AccountViewModel>() {
    override fun initLayout(): Int {
        return R.layout.fragment_login
    }

    override fun initViewModel(dataBinding: FragmentLoginBinding) {
        setViewModel(ViewModelProviders.of(activity!!).get(AccountViewModel::class.java)).apply {
            getDataBinding().viewModel = this
        }
    }

    override fun initData() {

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
