package com.example.cruduts

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cruduts.utils.OrganizationAdapter
import com.example.cruduts.model.Role

class StructureActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_structure)

        // Toolbar setup


        // RecyclerView setup
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = OrganizationAdapter(getOrganizationData())

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Library Hub"
    }

    // Inflate the menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // Handle menu item clicks
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_welcome-> {
                startActivity(Intent(this, LandingActivity::class.java))

                true
            }
            R.id.action_structure-> {
                true
            }
            R.id.action_crud -> {
                startActivity(Intent(this, MainActivity::class.java))
                true
            }
            R.id.action_exit -> {
                startActivity(Intent(this, LoginActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Mock data for organization structure
    private fun getOrganizationData(): List<Role> {
        return listOf(
            Role("NaN", "NaN", R.drawable.def),
            Role("Library Director", "Oversees all library operations", R.drawable.def),
            Role("Librarian", "Manages books and assists users", R.drawable.def),
            Role("Assistant", "Helps with daily tasks", R.drawable.def),
            Role("IT Support", "Maintains technical systems", R.drawable.def),
            Role("Volunteer", "Supports events and community outreach", R.drawable.def),
            Role("BookKeeper", "Helps with bookkeeping", R.drawable.def),
            Role("User", "Uses the library services", R.drawable.def),

            )
    }
}
