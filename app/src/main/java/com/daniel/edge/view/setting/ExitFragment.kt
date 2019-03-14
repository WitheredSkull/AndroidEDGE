package com.daniel.edge.view.setting

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView

import com.daniel.edge.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_setting.*

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
