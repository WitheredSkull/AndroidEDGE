package com.daniel.edge.view

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.get
import com.daniel.edge.R
import com.daniel.edge.databinding.MainFragmentBinding
import com.daniel.edge.viewModel.MainViewModel

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var databinding:MainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        databinding = DataBindingUtil.inflate(inflater,R.layout.main_fragment, container, false)
        databinding.viewModel = ViewModelProviders.of(this).get()
        databinding.lifecycleOwner = this
        return databinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

}
