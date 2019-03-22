package com.daniel.edgeDemo.view.account

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.daniel.edgeDemo.R
import com.daniel.edgeDemo.constant.App
import com.daniel.edgeDemo.dagger.component.DaggerAccountComponent
import com.daniel.edgeDemo.viewModel.AccountViewModel
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
