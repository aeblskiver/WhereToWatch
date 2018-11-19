package com.justin.apps.wheretowatch.base

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.justin.apps.wheretowatch.R
import kotlinx.android.synthetic.main.activity_main.*

class BaseActivity : AppCompatActivity() {
    val BUNDLE_KEY_FRESH = "freshSearch"
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.fragment_host)

        setSupportActionBar(findViewById(R.id.toolbar_main))

        //supportFragmentManager.beginTransaction().add(R.id.container, WelcomeFragment()).commit()
        appBarConfiguration = AppBarConfiguration(navController.graph)
        bottomNavigationView.setupWithNavController(navController)
        toolbar_main.setupWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
//        val selectedFragment: Fragment = Fragment()
        when (item.itemId) {
            R.id.navigation_favorites -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.actionSearch -> {
//                selectedFragment = SearchFragment()
//                val bundle = Bundle()
//                bundle.putBoolean(BUNDLE_KEY_FRESH, true)
//                selectedFragment.arguments = bundle
                //return@OnNavigationItemSelectedListener true
                navController.navigate(R.id.actionSearch)
            }
            R.id.navigation_filter -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        Log.d("base", "Search fragment replacing")

        true
    }

    override fun onSupportNavigateUp() = findNavController(R.id.fragment_host).navigateUp()
}
