package com.example.foursquareapplication

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Layout
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.example.foursquareapplication.databinding.ActivityHomeBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout

class HomeActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slide_menu)

        supportActionBar?.hide()

        val toolbar = findViewById<Toolbar>(R.id.toolbar_home)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.menu_icon)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val drawer = findViewById<DrawerLayout>(R.id.drawer)
        toggle = ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val tabs = findViewById<TabLayout>(R.id.tab_home)
        val pager = findViewById<ViewPager>(R.id.home_pager)

        tabs.addTab(tabs.newTab().setText("Near you"))
        tabs.addTab(tabs.newTab().setText("Toppick"))
        tabs.addTab(tabs.newTab().setText("Popular"))
        tabs.addTab(tabs.newTab().setText("Lunch"))
        tabs.addTab(tabs.newTab().setText("Coffee"))
        tabs.tabGravity = TabLayout.GRAVITY_FILL


        val adapter = HomeTabAdapter(supportFragmentManager, tabs.tabCount)
        pager.adapter = adapter

        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                pager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}