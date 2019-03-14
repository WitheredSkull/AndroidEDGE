package com.daniel.edge.view.account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.daniel.edge.R
import com.daniel.edge.constant.App
import com.daniel.edge.dagger.component.AccountComponent
import com.daniel.edge.dagger.component.DaggerAccountComponent
import com.daniel.edge.dagger.module.AccountModule
import com.daniel.edge.viewModel.AccountViewModel
import javax.inject.Inject

class AccountActivity : AppCompatActivity() {
    @Inject
    lateinit var mViewModel:AccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_activity)
        if (savedInstanceState == null) {
            val finalHost = NavHostFragment.create(R.navigation.account_nav)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, finalHost)
                .setPrimaryNavigationFragment(finalHost)
                .commitNow()
        }
        DaggerAccountComponent.builder().appComponent(App.appComponent).build().inject(this)
    }

}
