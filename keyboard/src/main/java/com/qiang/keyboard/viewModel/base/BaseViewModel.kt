package com.qiang.keyboard.viewModel.base

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import com.qiang.keyboard.constant.App

open class BaseViewModel(application: Application) : AndroidViewModel(application) {
    fun getContext() = getApplication<Application>()
    fun startActivity(intent:Intent) = getContext().startActivity(intent)
}