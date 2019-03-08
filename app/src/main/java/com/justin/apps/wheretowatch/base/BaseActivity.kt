package com.justin.apps.wheretowatch.base

import android.arch.lifecycle.ViewModelProviders
import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.justin.apps.wheretowatch.R
import com.justin.apps.wheretowatch.WelcomeFragment
import com.justin.apps.wheretowatch.filter.FilterDialogFragment
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
class BaseActivity : AppCompatActivity(), FavoriteClickListener, FilterDialogFragment.FilterListener {
    override fun onFilterSet(choiceSet: Set<FilterDialogFragment.Choice>) {
        Log.d(TAG, "Activity passed on filter set")
        Log.d(TAG, "Set of : ${choiceSet.toString()}")
    }

    override fun onFilterReset() {
        Log.d(TAG, "Activity passed on filter reset")
    }

    val TAG = "BaseActivity"
    val BUNDLE_KEY_FRESH = "freshSearch"
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var appBarLayout: AppBarLayout
    private lateinit var navController: NavController
    lateinit var sharedViewModel: SharedViewModel
    lateinit var bottomNav: BottomNavigationView

    /**
     *  Set up toolbar and navigation here
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNav = findViewById(R.id.bottomNavigationView)
        appBarLayout = toolbar_layout

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
                when (navController.currentDestination?.id) {
                    R.id.welcomeFragment -> navController.navigate(R.id.action_welcomeFragment_to_favoriteFragment)
                    R.id.actionSearch -> navController.navigate(R.id.action_actionSearch_to_actionFavorite)
                }
            }
            R.id.actionSearch -> {
                when (navController.currentDestination?.id) {
                    R.id.welcomeFragment -> navController.navigate(R.id.action_welcomeFragment_to_searchFragment)
                    R.id.actionFavorite -> navController.navigate(R.id.action_actionFavorite_to_actionSearch)
                }
            }
            R.id.navigation_filter -> {
                val dialog = FilterDialogFragment()
                dialog.show(supportFragmentManager, "Filter Dialog")
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

    fun showAppBarLayout() {
        appBarLayout.setExpanded(true)
    }



    override fun onSupportNavigateUp() = findNavController(R.id.fragment_host).navigateUp()

}
