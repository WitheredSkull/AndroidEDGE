package com.qiang.keyboard.view

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import com.daniel.edge.management.activity.EdgeActivityManagement
import com.qiang.keyboard.R

class EditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        title = "当前模式： 文本发送"
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_edit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_forthwith -> {
                startActivity(Intent(this, KeyboardActivity::class.java))
            }
            R.id.action_exit -> {
                EdgeActivityManagement.getInstance().exit()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
