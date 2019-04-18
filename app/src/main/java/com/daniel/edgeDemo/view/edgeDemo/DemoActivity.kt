package com.daniel.edgeDemo.view.edgeDemo

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.daniel.edge.management.download.DownloadManager
import com.daniel.edge.management.download.model.DownloadThreadModel
import com.daniel.edge.management.file.EdgeFileManagement
import com.daniel.edgeDemo.R
import com.daniel.edgeDemo.databinding.ActivityDemoBinding
import com.daniel.edge.management.fragment.EdgeFragmentManager
import com.daniel.edge.management.permission.EdgePermissionManagement
import com.daniel.edge.management.permission.OnEdgePermissionCallBack
import com.daniel.edge.model.database.table.DownloadThreadTable
import com.daniel.edge.utils.log.EdgeLog
import com.daniel.edgeDemo.viewModel.DemoViewModel
import java.io.File

class DemoActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
    OnEdgePermissionCallBack {
    override fun onRequestPermissionSuccess() {
        activityDemoBinding.navigation.setOnNavigationItemSelectedListener(this)
        EdgeFragmentManager.addFragments(
            R.id.fragmentLayout,
            supportFragmentManager,
            DemoHomeFragment::class.java,
            DemoDashboardFragment::class.java,
            DemoNotificationsFragment::class.java
        )
        Thread(object : Runnable {
            override fun run() {
                DownloadManager.getInstance().down(
                    "https://alissl.ucdl.pp.uc.cn/fs01/union_pack/Wandoujia_778701_web_seo_baidu_homepage.apk",
                    File(EdgeFileManagement.getEdgeExternalPath()), 3
                ).allStart()
            }
        }).start()
    }

    override fun onRequestPermissionFailure(permissions: ArrayList<String>) {
        onRequestPermissionSuccess()
    }

    lateinit var activityDemoBinding: ActivityDemoBinding
    lateinit var viewModel: DemoViewModel
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_home -> {
                setTitle("DemoHomeFragment")
                EdgeFragmentManager.switchFragment(
                    R.id.fragmentLayout,
                    supportFragmentManager,
                    DemoHomeFragment::class.java
                )
                return true
            }
            R.id.navigation_dashboard -> {
                setTitle("DemoDashboardFragment")
                EdgeFragmentManager.switchFragment(
                    R.id.fragmentLayout,
                    supportFragmentManager,
                    DemoDashboardFragment::class.java
                )
                return true
            }
            R.id.navigation_notifications -> {
                setTitle("DemoNotificationsFragment")
                EdgeFragmentManager.switchFragment(
                    R.id.fragmentLayout,
                    supportFragmentManager,
                    DemoNotificationsFragment::class.java
                )
                return true
            }
        }
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDemoBinding = DataBindingUtil.setContentView(this, R.layout.activity_demo)
        viewModel = ViewModelProviders.of(this).get(DemoViewModel::class.java)
        activityDemoBinding.viewModel = viewModel
        activityDemoBinding.lifecycleOwner = this
        EdgePermissionManagement()
            .setCallBack(this)
            .requestPackageNeedPermission()
            .build()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        var isBack = false
        supportFragmentManager.fragments.forEach {
            if (it is DemoDashboardFragment) {
                if (it.onBack()) {
                    isBack = true
                }
            }
        }
        if (!isBack)
            super.onBackPressed()
    }
}
