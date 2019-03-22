package com.daniel.edgeDemo.view.setting


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation

import com.daniel.edgeDemo.R
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = ExitFragment.ViewHolder(inflater.inflate(R.layout.fragment_setting, container, false))
        view.button3.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_settingFragment_to_exitFragment)
        }
        return view.containerView
    }



}
