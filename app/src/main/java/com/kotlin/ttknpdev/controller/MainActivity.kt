package com.kotlin.ttknpdev.controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.*
import com.google.android.material.navigation.NavigationView
import com.kotlin.ttknpdev.R
import com.kotlin.ttknpdev.fragments.*

class MainActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener {

    private val titles = arrayOf("HTTP METHOD GET (READ)","HTTP METHOD GET (READS)","HTTP METHOD POST (CREATE)","HTTP METHOD PUT (UPDATE)","HTTP METHOD DELETE (DELETE)","Main Activity")

    private lateinit var textViewTitle: TextView

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle // for toggle
    private lateinit var navigationView : NavigationView

    private lateinit var fragmentManager: FragmentManager
    private lateinit var fragmentTransaction: FragmentTransaction

    private fun initialMainActivityWidgets() {
        textViewTitle = findViewById(R.id.textViewTitle)
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close) // importance
        textViewTitle.text = titles[5]
    }

    private fun loadFragment(fragment: Fragment) { // argument is class Fragment. So any class inherit fragment clas can pass
        // create a FragmentManager
        fragmentManager = supportFragmentManager
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        fragmentTransaction = fragmentManager.beginTransaction()
        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.fragmentContainerView,fragment) // called your fragment id
        // save the changes
        fragmentTransaction.commit()
    }

    private fun removeFragments() {
        // Create and commit a new transaction
        fragmentManager.commit {
            setReorderingAllowed(true)
            // Replace whatever is in the fragment_container view with this fragment
            replace<Fragment>(R.id.fragmentContainerView)
        }
        textViewTitle.text = titles[5]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialMainActivityWidgets()
        /*
            pass the Open and Close toggle for the drawer layout listener
            to toggle the button
        */
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        // to make the Navigation drawer icon always appear on the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navigationView.setNavigationItemSelectedListener(this) // **** works like you inherit View.OnClickListener

    }

    /*
       override the onOptionsItemSelected() function to implement
       the item click listener callback to open and close the navigation drawer when the icon is clicked
    */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return  true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        // Log.d("CLICKED","${p0.title}") // get title like 2024-03-12 21:48:56.935 19392-19392 CLICKED  com.kotlin.ttknpdev  D  Reset
        when (p0.title) {
            "Read" -> {
                // Log.d("CLICKED","Read clicked")
                textViewTitle.text = titles[0]
                loadFragment(ReadFragment())
            }
            "Reads" -> {
                // Log.d("CLICKED","Reads clicked")
                textViewTitle.text = titles[1]
                loadFragment(ReadsFragment())
            }
            "Create" -> {
                // Log.d("CLICKED","Create clicked")
                textViewTitle.text = titles[2]
                loadFragment(CreateFragment())
            }
            "Update" -> {
                // Log.d("CLICKED","Update clicked")
                textViewTitle.text = titles[3]
                loadFragment(UpdateFragment())
            }
            "Delete" -> {
                // Log.d("CLICKED","Delete clicked")
                textViewTitle.text = titles[4]
                loadFragment(DeleteFragment())
            }
            "Reset" -> {
                // Log.d("CLICKED","Reset clicked")
                // Note !! if first case it is not fragment will close app Why??
                removeFragments()
            }
        }
        return true
    }

}