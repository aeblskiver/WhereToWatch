package com.justin.apps.wheretowatch.filter

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import com.justin.apps.wheretowatch.R
import com.justin.apps.wheretowatch.model.Model
import com.justin.apps.wheretowatch.util.constants.AMAZON_INSTANT_DISPLAY_NAME
import com.justin.apps.wheretowatch.util.constants.AMAZON_PRIME_DISPLAY_NAME
import com.justin.apps.wheretowatch.util.constants.NETFLIX_DISPLAY_NAME
import kotlinx.android.synthetic.main.fragment_filter.*

class FilterDialogFragment : DialogFragment() {
    private val TAG = "FilterDialogFragment"
    private var isAmazonPrime = false
    private var isAmazonInstant = false
    private var isNetflix = false
    private val keyIsAmazonPrime = "isAmazonPrime"
    private val keyIsAmazonInstant = "isAmazonInstant"
    private val keyIsNetflix = "isNetflix"
    private lateinit var filterListener: FilterListener
    private val choiceSet: MutableSet<String> = mutableSetOf()
    private var PREF_PRIVATE_MODE = 0
    private val PREF_NAME = "where-to-watch"
    private lateinit var sharedPreferences: SharedPreferences

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context != null) {
            sharedPreferences = context.getSharedPreferences(PREF_NAME, PREF_PRIVATE_MODE)
            isAmazonPrime = sharedPreferences.getBoolean(keyIsAmazonPrime, false)
            isAmazonInstant = sharedPreferences.getBoolean(keyIsAmazonInstant, false)
            isNetflix = sharedPreferences.getBoolean(keyIsNetflix, false)

            if (isAmazonPrime) choiceSet.add(AMAZON_PRIME_DISPLAY_NAME)
            if (isAmazonInstant) choiceSet.add(AMAZON_INSTANT_DISPLAY_NAME)
            if (isNetflix) choiceSet.add(NETFLIX_DISPLAY_NAME)
        }
        filterListener = activity as FilterListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_filter, container, false)
    }

    override fun onPause() {

        val sharedPrefEditor = sharedPreferences.edit()
        sharedPrefEditor.putBoolean(keyIsAmazonPrime, cb_amazon_prime.isChecked)
        sharedPrefEditor.putBoolean(keyIsAmazonInstant, cb_amazon_instant.isChecked)
        sharedPrefEditor.putBoolean(keyIsNetflix, cb_netflix.isChecked)
        sharedPrefEditor.apply()
        super.onPause()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val checkBoxArray = arrayOf(cb_amazon_instant, cb_amazon_prime, cb_netflix)

        checkBoxArray.forEach {
            it.isChecked = isChecked(it.id)
            it.setOnCheckedChangeListener { button, isChecked ->
                when (isChecked) {
                    true -> {
                        choiceSet.add(getChoiceFromCheckbox(button))
                    }
                    false -> {
                        choiceSet.remove(getChoiceFromCheckbox(button))
                    }
                }
            }
        }

        action_reset_filter.setOnClickListener {
            Log.d(TAG, "Reset clicked")
            checkBoxArray.forEach { it.isChecked = false }
        }

        action_set_filter.setOnClickListener {
            Log.d(TAG, "Set filter clicked")


            filterListener.onFilterSet(choiceSet)
            dismiss()
        }
    }

    private fun isChecked(id: Int): Boolean {
        return when (id) {
            cb_amazon_prime.id -> isAmazonPrime
            cb_amazon_instant.id -> isAmazonInstant
            cb_netflix.id -> isNetflix
            else -> false
        }
    }


    private fun getChoiceFromCheckbox(button: CompoundButton): String {
        return when (button.id) {
            cb_amazon_instant.id -> AMAZON_INSTANT_DISPLAY_NAME
            cb_amazon_prime.id -> AMAZON_PRIME_DISPLAY_NAME
            else -> NETFLIX_DISPLAY_NAME
        }
    }

    interface FilterListener {
        fun onFilterSet(choiceSet: Set<String>)
        fun onFilterReset()
    }
}