package com.daniel.edgeDemo.view.edgeDemo

import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.daniel.edgeDemo.R
import com.daniel.edgeDemo.databinding.ActivityDemoBinding
import com.daniel.edge.management.fragment.EdgeFragmentManager
import com.daniel.edge.management.permission.EdgePermissionManagement
import com.daniel.edge.management.permission.OnEdgePermissionCallBack
import com.daniel.edgeDemo.viewModel.DemoViewModel

class DemoActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
    OnEdgePermissionCallBack {
    override fun onRequestPermissionSuccess() {

    }

    override fun onRequestPermissionFailure(permissions: ArrayList<String>) {
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
        activityDemoBinding.navigation.setOnNavigationItemSelectedListener(this)
        viewModel = ViewModelProviders.of(this).get(DemoViewModel::class.java)
        activityDemoBinding.viewModel = viewModel
        activityDemoBinding.lifecycleOwner = this
        EdgeFragmentManager.addFragments(
            R.id.fragmentLayout,
            supportFragmentManager,
            DemoHomeFragment::class.java,
            DemoDashboardFragment::class.java,
            DemoNotificationsFragment::class.java
        )
        EdgePermissionManagement().requestPackageNeedPermission()
            .build(this)
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EdgePermissionManagement.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}
