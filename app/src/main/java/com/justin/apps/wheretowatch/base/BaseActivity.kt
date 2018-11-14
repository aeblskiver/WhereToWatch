package com.justin.apps.wheretowatch.base

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.justin.apps.wheretowatch.R
import com.justin.apps.wheretowatch.search.SearchFragment
import kotlinx.android.synthetic.main.activity_main.*

abstract class BaseActivity : AppCompatActivity() {
    val BUNDLE_KEY_FRESH = "freshSearch"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var selectedFragment: Fragment = Fragment()
        when (item.itemId) {
            R.id.navigation_favorites -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_search -> {
                selectedFragment = SearchFragment()
                val bundle = Bundle()
                bundle.putBoolean(BUNDLE_KEY_FRESH, true)
                selectedFragment.arguments = bundle
                Log.d("base", "Search fragment selected")
                //return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_filter -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        Log.d("base", "Search fragment replacing")

        supportFragmentManager.beginTransaction().replace(R.id.container, selectedFragment).commit()

        true
    }
}