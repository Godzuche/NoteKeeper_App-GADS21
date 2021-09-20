package com.example.notekeeper

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notekeeper.databinding.ActivityNoteListBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

class NoteListActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val tag = this::class.simpleName
    private lateinit var binding: ActivityNoteListBinding

    private val noteRecyclerAdapter by lazy {  RecyclerAdapter(this, DataManager.notes) }

    private val noteLayoutManager by lazy { LinearLayoutManager(this) }

    private val courseLayoutManager by lazy { GridLayoutManager(this, 2) }
    private val courseRecyclerAdapter by lazy { CourseRecyclerAdapter(this, DataManager.courses.values.toList()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.appBarNoteList.noteListTopAppBar)

        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.appBarNoteList.noteListTopAppBar,
            R.string.open_drawer,
            R.string.close_drawer
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener (this)

        binding.appBarNoteList.fab.setOnClickListener {
            val activityIntent = Intent(this, EditNoteActivity::class.java)
            startActivity(activityIntent)
        }

        displayNotes()
        Log.d(tag, "onCreate")
    }

    private fun displayNotes() {
        binding.appBarNoteList.contentNoteList.listItems.layoutManager = noteLayoutManager
        binding.appBarNoteList.contentNoteList.listItems.adapter = noteRecyclerAdapter

        binding.navView.menu.findItem(R.id.nav_notes).isChecked = true
    }

    private fun displayCourses() {
        binding.appBarNoteList.contentNoteList.listItems.layoutManager = courseLayoutManager
        binding.appBarNoteList.contentNoteList.listItems.adapter = courseRecyclerAdapter

        binding.navView.menu.findItem(R.id.nav_courses).isChecked = true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_note_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings0 -> true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.appBarNoteList.contentNoteList.listItems.adapter?.notifyDataSetChanged()
        Log.d(tag, "onResume")
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_notes -> {
                displayNotes()
            }
            R.id.nav_courses -> {
                displayCourses()
            }
            R.id.nav_share -> {
                handleSelection("Don't you think you've shared enough")
            }
            R.id.nav_send -> {
                handleSelection("Send")
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun handleSelection(message: String) {
        Snackbar.make(binding.appBarNoteList.contentNoteList.listItems, message, Snackbar.LENGTH_LONG).show()
    }

}