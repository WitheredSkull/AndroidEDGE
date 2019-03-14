package com.daniel.edge.view.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.daniel.edge.R
import com.daniel.edge.databinding.LoginFragmentBinding
import com.daniel.edge.utils.toast.EdgeToastUtils
import com.daniel.edge.view.SlideView
import com.daniel.edge.view.slideLayout.EdgeSlideLinearLayout
import com.daniel.edge.viewModel.AccountViewModel

class LoginFragment : Fragment() {

    private var databinding: LoginFragmentBinding? = null
    private lateinit var mView:View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mView = inflater.inflate( R.layout.login_fragment,null)
//        DaggerAccountComponent.builder().appComponent(App.appComponent).build().inject(this)
//        databinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.login_fragment, null, false)
////        databinding!!.viewModel = getViewModel()
////        databinding!!.lifecycleOwner = this
//        databinding!!.login.setOnClickListener {
//            EdgeToastUtils.getInstance().show("点击")
//            Navigation.findNavController(it).navigate(R.id.action_loginFragment_to_registerFragment)
//        }
        mView.findViewById<View>(R.id.login).setOnClickListener {

            EdgeToastUtils.getInstance().show("点击")
        }
        mView.findViewById<SlideView>(R.id.v).setOnClickListener {
            EdgeToastUtils.getInstance().show("点击")
        }
        mView.findViewById<EdgeSlideLinearLayout>(R.id.sl).setOnClickListener {
            (it as EdgeSlideLinearLayout).close()
        }
        return mView
    }

    fun getViewModel(): AccountViewModel? {
        activity?.let {
            return (it as AccountActivity).mViewModel
        }
        return null
    }
}
