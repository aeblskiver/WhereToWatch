package com.justin.apps.wheretowatch.base

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.justin.apps.wheretowatch.R
import com.justin.apps.wheretowatch.model.Model
import com.justin.apps.wheretowatch.repository.MediaRepository
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 *  Activity that forms the base of the app. Uses Navigation components to swap out fragments.
 *  Sets up Bottom Navigation View and top Toolbar.
 */
class BaseActivity : AppCompatActivity(), FavoriteClickListener {
    val TAG = "BaseActivity"
    val BUNDLE_KEY_FRESH = "freshSearch"
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    lateinit var sharedViewModel: SharedViewModel

    /**
     *  Set up toolbar and navigation here
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar_main))

        sharedViewModel = ViewModelProviders.of(this,
            SharedViewModelFactory(MediaRepository)).get(SharedViewModel::class.java)
        setupNavigation()
    }

    /**
     *  Finds the navigation controller and configures navigation graph.
     *  Sets up toolbar to work with nav controller.
     */
    private fun setupNavigation() {
        navController = findNavController(R.id.fragment_host)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        bottomNavigationView.setupWithNavController(navController)
        toolbar_main.setupWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    /**
     *  Listens for selections in Bottom Navigation View and changes into the proper fragment.
     */
    private val mOnNavigationItemSelectedListener
            = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.actionFavorite -> {
                navController.navigate(R.id.actionFavorite)
            }
            R.id.actionSearch -> {
                navController.navigate(R.id.actionSearch)
            }
            R.id.navigation_filter -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        true
    }

    /**
     *  Base Activity listens for clicks in the RecyclerView cards to either insert/remove items in database.
     */
    override fun onClick(media: Model.Media?, favorite: Boolean) {
        GlobalScope.launch(Dispatchers.IO) {
            sharedViewModel.insert(media)
        }
    }


    override fun onSupportNavigateUp() = findNavController(R.id.fragment_host).navigateUp()
}
