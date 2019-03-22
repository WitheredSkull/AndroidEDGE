package com.daniel.edgeDemo.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.daniel.edgeDemo.R
import com.daniel.edgeDemo.constant.App
import com.daniel.edgeDemo.dagger.component.DaggerHomeComponent
import com.daniel.edgeDemo.databinding.MainFragmentBinding
import com.daniel.edgeDemo.viewModel.MainViewModel
import javax.inject.Inject

class MainFragment : Fragment() {

    private lateinit var databinding:MainFragmentBinding
    @Inject
    lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        databinding = DataBindingUtil.inflate(inflater,R.layout.main_fragment, container, false)
        DaggerHomeComponent.builder().appComponent(App.appComponent).build().inject(this)
        databinding.viewModel = viewModel
        databinding.lifecycleOwner = this
        return databinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    companion object {
        fun newInstance() = MainFragment()
    }

}
