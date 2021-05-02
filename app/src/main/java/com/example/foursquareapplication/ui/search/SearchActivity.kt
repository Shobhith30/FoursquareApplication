package com.example.foursquareapplication.ui.search

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.foursquareapplication.R
import com.example.foursquareapplication.databinding.ActivitySearchMainBinding
import com.example.foursquareapplication.ui.SearchFilterFragment


class SearchActivity : AppCompatActivity() {

    lateinit var searchBinding : ActivitySearchMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchBinding = ActivitySearchMainBinding.inflate(layoutInflater)
        setContentView(searchBinding.root)
        supportActionBar?.hide()

        val toolbar = findViewById<Toolbar>(R.id.toolbar_search)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

         toolbar.setNavigationOnClickListener {
         onBackPressed()
         return@setNavigationOnClickListener
    }


        searchBinding.searchPlace.setOnQueryTextFocusChangeListener { v, hasFocus ->

            if(hasFocus) {
                val fragmentSearchSuggestion = SearchSuggestionsFragment()
                val fragmentManager = supportFragmentManager
                val transaction = fragmentManager.beginTransaction()
                transaction.replace(R.id.search_fragment, fragmentSearchSuggestion)
                transaction.commit()
            }

        }
        searchBinding.nearMeSearch.setOnQueryTextFocusChangeListener { v, hasFocus ->

            if(hasFocus) {
                val fragmentSearchOptions = SearchOptionsFragment()
                val fragmentManager = supportFragmentManager
                val transaction = fragmentManager.beginTransaction()
                transaction.replace(R.id.search_fragment, fragmentSearchOptions)
                transaction.commit()
            }
        }


}

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_favourite, menu)
        return super.onCreateOptionsMenu(menu)
}

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.filter -> {
                val fragmentSearchFilter = SearchFilterFragment()
                val fragmentManager = supportFragmentManager
                val transaction = fragmentManager.beginTransaction()
                transaction.replace(R.id.search_fragment, fragmentSearchFilter)
                transaction.commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}