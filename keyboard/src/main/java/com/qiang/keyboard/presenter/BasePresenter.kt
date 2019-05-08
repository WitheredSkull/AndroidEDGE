package com.qiang.keyboard.presenter

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.qiang.keyboard.view.base.BaseCallback
import java.lang.ref.WeakReference

abstract class BasePresenter<C : BaseCallback?>(activity: AppCompatActivity) {
    private var mActivity = WeakReference(activity)
    private var mCallback:C? = null

    fun onCreate() {
    }

    fun setCallback(callback: C?) {
        this.mCallback = callback
    }

    fun getCallback() = mCallback

    fun startActivity(intent:Intent) {
        getActivity()?.startActivity(intent)
    }

    fun getActivity() = mActivity.get()

    fun onDestroy() {

    }
}