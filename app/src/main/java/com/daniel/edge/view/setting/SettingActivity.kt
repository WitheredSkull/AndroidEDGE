package com.daniel.edge.view.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import com.daniel.edge.R
import com.daniel.edge.utils.log.EdgeLog

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        Navigation.findNavController(this,R.id.fragment)
            .addOnDestinationChangedListener { controller, destination, arguments ->
                EdgeLog.show(javaClass,"导航","${destination.label}")
            }
    }
}
