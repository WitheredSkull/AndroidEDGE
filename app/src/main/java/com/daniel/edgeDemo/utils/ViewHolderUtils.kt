package com.daniel.edgeDemo.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

class ViewHolderUtils(override val containerView:View): RecyclerView.ViewHolder(containerView),LayoutContainer {
}