package com.example.investcalculator

import android.os.Bundle
import android.view.View
import androidx.fragment.app.*
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager.widget.ViewPager
import com.example.investcalculator.fragments.ApiFragment
import com.example.investcalculator.fragments.BuyFragment
import com.example.investcalculator.fragments.DBFragment
import com.example.investcalculator.fragments.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

private const val NUM_PAGES = 4

class MainActivity : FragmentActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navController: NavController
    private lateinit var navControllerView: FragmentContainerView
    private lateinit var mPager: ViewPager

    private inner class ScreenSlidePagerAdapter(fm: FragmentManager) :
        FragmentStatePagerAdapter(fm) {
        override fun getCount(): Int = NUM_PAGES
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> ApiFragment()
                1 -> BuyFragment()
                2 -> DBFragment()
                3 -> SettingsFragment()
                else -> ApiFragment()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ModelPreferencesManager.with(application)
        val settingsData = ModelPreferencesManager.get<Settings>("KEY_Settings")


        bottomNavigationView = findViewById(R.id.bottom_navigation)
        mPager = findViewById(R.id.pager)
        navControllerView = findViewById(R.id.fragmentContainer)
        if (settingsData?.compactMode == "0") {

            navController = findNavController(R.id.fragmentContainer)
            bottomNavigationView.setupWithNavController(navController)
        } else {

            mPager.visibility = View.VISIBLE
            bottomNavigationView.visibility = View.GONE
            navControllerView.visibility = View.GONE


            val pagerAdapter = ScreenSlidePagerAdapter(supportFragmentManager)
            mPager.adapter = pagerAdapter
        }


    }

    override fun onBackPressed() {
        if (mPager.currentItem == 0) {
            super.onBackPressed()
        } else {
            mPager.currentItem = mPager.currentItem - 1
        }
    }
}