package com.qiang.keyboard.view

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.daniel.edge.management.activity.EdgeActivityManagement
import com.daniel.edge.management.fragment.EdgeFragmentManager
import com.qiang.keyboard.R
import com.qiang.keyboard.SendNumberFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        EdgeFragmentManager.addFragments(R.id.fl_content, supportFragmentManager, SendTextFragment::class.java)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> return true
            R.id.action_exit -> {
                EdgeActivityManagement.getInstance().exit()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_text_send -> {
                EdgeFragmentManager.switchFragment(
                    R.id.fl_content,
                    supportFragmentManager,
                    SendTextFragment::class.java
                )
            }
            R.id.nav_accountant ->
                EdgeFragmentManager.switchFragment(
                    R.id.fl_content,
                    supportFragmentManager,
                    SendNumberFragment::class.java
                )

            R.id.nav_forthwith_send -> {
                startActivity(Intent(this, KeyboardActivity::class.java))
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
