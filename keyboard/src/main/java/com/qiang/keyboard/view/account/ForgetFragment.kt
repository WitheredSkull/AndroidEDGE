package com.qiang.keyboard.view.account

import androidx.lifecycle.ViewModelProviders
import com.qiang.keyboard.R
import com.qiang.keyboard.databinding.FragmentForgetBinding
import com.qiang.keyboard.view.base.BaseVMFragment
import com.qiang.keyboard.viewModel.AccountViewModel

class ForgetFragment : BaseVMFragment<FragmentForgetBinding, AccountViewModel>() {
    override fun initLayout(): Int {
        return R.layout.fragment_forget
    }

    override fun enableViewModel(): Boolean = true

    override fun initData() {
        if (arguments != null){
            val type = arguments!!.getInt("type",0)
            getViewModel()?.forgetType = type
            if (type == 1){
                getDataBinding().btForget.setText(R.string.forget_alter)
            }

        }
        getViewModel()?.pwd?.value = ""
    }

    override fun initListener() {
    }

    override fun initViewModel(): AccountViewModel? {
        return ViewModelProviders.of(activity!!).get(AccountViewModel::class.java)
    }

    override fun onDestroyView() {
        getViewModel()?.closeCountDownTime()
        super.onDestroyView()
    }
}
