package com.qiang.keyboard.expand

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.daniel.edge.management.activity.EdgeActivityManagement
import com.qiang.keyboard.R
import com.qiang.keyboard.view.SelectActivity
import com.qiang.keyboard.view.SettingsActivity

fun AppCompatActivity.createOptionsMenu(menu: Menu, isNeedBack: Boolean): Boolean {
    if (isNeedBack) {
        menuInflater.inflate(R.menu.menu_keyboard, menu)
    } else {
        menuInflater.inflate(R.menu.menu_keyboard_nonback, menu)
    }
    return true
}

fun AppCompatActivity.optionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
        android.R.id.home -> {
            this.onBackPressed()
            return true
        }
        R.id.action_menu -> {
            if (!this::javaClass.get().simpleName.contains("SelectActivity"))
            startActivity(Intent(this, SelectActivity::class.java))
            return true
        }
        R.id.action_setting -> {
            startActivity(Intent(this, SettingsActivity::class.java))
            return true
        }
        R.id.action_back -> {
            this.onBackPressed()
            return true
        }
        R.id.action_exit -> {
            EdgeActivityManagement.getInstance().exit()
            return true
        }
        else -> return false
    }
}