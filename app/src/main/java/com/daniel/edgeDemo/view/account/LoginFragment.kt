package com.daniel.edgeDemo.view.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.daniel.edgeDemo.R
import com.daniel.edgeDemo.constant.App
import com.daniel.edgeDemo.dagger.component.DaggerAccountComponent
import com.daniel.edgeDemo.databinding.LoginFragmentBinding
import com.daniel.edge.utils.toast.EdgeToastUtils
import com.daniel.edgeDemo.viewModel.AccountViewModel

class LoginFragment : Fragment() {

    private var databinding: LoginFragmentBinding? = null
    private lateinit var mView:View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mView = inflater.inflate( R.layout.login_fragment,null)
        DaggerAccountComponent.builder().appComponent(App.appComponent).build().inject(this)
        databinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.login_fragment, null, false)
//        databinding!!.viewModel = getViewModel()
//        databinding!!.lifecycleOwner = this
        databinding!!.login.setOnClickListener {
            EdgeToastUtils.getInstance().show("点击")
            Navigation.findNavController(it).navigate(R.id.action_loginFragment_to_registerFragment)
        }
//        mView.findViewById<View>(R.id.login).setOnClickListener {
//
//            EdgeToastUtils.getInstance().show("点击")
//        }
//        mView.findViewById<SlideView>(R.id.v).setOnClickListener {
//            EdgeToastUtils.getInstance().show("点击")
//        }
//        mView.findViewById<EdgeSlideRelativeLayout>(R.id.sl).setOnClickListener {
//            (it as EdgeSlideRelativeLayout).close()
//        }
//        return mView
        return databinding!!.root
    }

    fun getViewModel(): AccountViewModel? {
        activity?.let {
            return (it as AccountActivity).mViewModel
        }
        return null
    }
}
