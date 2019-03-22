package com.daniel.edgeDemo.view.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.daniel.edgeDemo.R
import kotlinx.android.extensions.LayoutContainer

class ExitFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = ViewHolder(inflater.inflate(R.layout.fragment_exit, container, false))
        return view.containerView
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),LayoutContainer {

    }
}
