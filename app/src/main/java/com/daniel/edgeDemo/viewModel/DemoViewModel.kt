package com.daniel.edgeDemo.viewModel

import android.view.MenuItem
import androidx.lifecycle.ViewModel
import com.daniel.edge.utils.log.EdgeLog
import com.google.android.material.bottomnavigation.BottomNavigationView

class DemoViewModel: ViewModel(),BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        return false
    }

    var onNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener = this

}