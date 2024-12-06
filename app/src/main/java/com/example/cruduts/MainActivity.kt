package com.example.cruduts

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cruduts.model.Book
import com.example.cruduts.model.DBHelper
import com.example.cruduts.utils.BookAdapter
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var bookAdapter: BookAdapter
    private val books = mutableListOf<Book>()

    // UI Components
    private lateinit var etTitle: EditText
    private lateinit var etAuthor: EditText
    private lateinit var cbRomance: CheckBox
    private lateinit var cbSciFi: CheckBox
    private lateinit var cbBiography: CheckBox
    private lateinit var rgType: RadioGroup
    private lateinit var rbFiction: RadioButton
    private lateinit var rbNonFiction: RadioButton
    private lateinit var etPrice: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnSave: Button
    private lateinit var searchView: androidx.appcompat.widget.SearchView

    private var selectedBookId: Int? = null // To track the book being updated

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize DBHelper
        dbHelper = DBHelper(this)

        // Bind UI components
        initializeUIComponents()

        // Setup RecyclerView
        setupRecyclerView()

        // Load initial data
        loadBooks()

        // Set up the Save button listener
        btnSave.setOnClickListener { saveBook() }

        // Set up search functionality
        setupSearchView()
    }

    private fun initializeUIComponents() {
        etTitle = findViewById(R.id.etTitle)
        etAuthor = findViewById(R.id.etAuthor)
        cbRomance = findViewById(R.id.cbRomance)
        cbSciFi = findViewById(R.id.cbSciFi)
        cbBiography = findViewById(R.id.cbBiography)
        rgType = findViewById(R.id.rgType)
        rbFiction = findViewById(R.id.rbFiction)
        rbNonFiction = findViewById(R.id.rbNonFiction)
        etPrice = findViewById(R.id.etPrice)
        recyclerView = findViewById(R.id.recyclerView)
        btnSave = findViewById(R.id.btnSave)
        searchView = findViewById(R.id.searchView)
    }

    private fun setupRecyclerView() {
        bookAdapter = BookAdapter(
            books
        ) { book ->
            // Handle both Edit and Delete actions via a dialog or options
            showBookOptionsDialog(book)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = bookAdapter
    }

    private fun showBookOptionsDialog(book: Book) {
        val options = arrayOf("Edit", "Delete")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose an action for ${book.title}")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> onEditBook(book) // Edit selected
                    1 -> onDeleteBook(book) // Delete selected
                }
            }
            .show()
    }


    private fun saveBook() {
        val title = etTitle.text.toString().trim()
        val author = etAuthor.text.toString().trim()
        val price = etPrice.text.toString().toDoubleOrNull()

        val genres = listOfNotNull(
            cbRomance.takeIf { it.isChecked }?.text.toString(),
            cbSciFi.takeIf { it.isChecked }?.text.toString(),
            cbBiography.takeIf { it.isChecked }?.text.toString()
        )
        val genre = genres.joinToString(", ")

        val type = when (rgType.checkedRadioButtonId) {
            R.id.rbFiction -> "Fiction"
            R.id.rbNonFiction -> "Non-Fiction"
            else -> null
        }

        if (!validateInput(title, author, genre, price, type)) return

        if (selectedBookId == null) {
            addNewBook(title, author, genre, type!!, price!!)
        } else {
            updateExistingBook(title, author, genre, type!!, price!!)
        }
    }

    private fun validateInput(
        title: String,
        author: String,
        genre: String,
        price: Double?,
        type: String?
    ): Boolean {
        return if (title.isEmpty() || author.isEmpty() || genre.isEmpty() || price == null || type == null) {
            Snackbar.make(recyclerView, "Please fill in all fields!", Snackbar.LENGTH_SHORT).show()
            false
        } else true
    }

    private fun addNewBook(title: String, author: String, genre: String, type: String, price: Double) {
        val id = dbHelper.insertBook(title, author, genre, type, price)
        if (id != -1L) {
            Snackbar.make(recyclerView, "Book added successfully!", Snackbar.LENGTH_SHORT).show()
            loadBooks()
            clearForm()
        } else {
            Snackbar.make(recyclerView, "Error adding book!", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun updateExistingBook(title: String, author: String, genre: String, type: String, price: Double) {
        val updatedRows = dbHelper.updateBook(selectedBookId!!, title, author, genre, type, price)
        if (updatedRows > 0) {
            Snackbar.make(recyclerView, "Book updated successfully!", Snackbar.LENGTH_SHORT).show()
            loadBooks()
            clearForm()
        } else {
            Snackbar.make(recyclerView, "Error updating book!", Snackbar.LENGTH_SHORT).show()
        }
        selectedBookId = null
    }

    private fun loadBooks() {
        books.clear()
        books.addAll(dbHelper.getAllBooks())
        bookAdapter.notifyDataSetChanged()
    }

    private fun clearForm() {
        etTitle.text.clear()
        etAuthor.text.clear()
        cbRomance.isChecked = false
        cbSciFi.isChecked = false
        cbBiography.isChecked = false
        rgType.clearCheck()
        etPrice.text.clear()
    }

    private fun onEditBook(book: Book) {
        selectedBookId = book.id
        etTitle.setText(book.title)
        etAuthor.setText(book.author)
        etPrice.setText(book.price.toString())

        cbRomance.isChecked = "Romance" in book.genre
        cbSciFi.isChecked = "Science Fiction" in book.genre
        cbBiography.isChecked = "Biography" in book.genre

        if (book.type == "Fiction") rbFiction.isChecked = true
        else rbNonFiction.isChecked = true
    }

    private fun onDeleteBook(book: Book) {
        val deletedRows = dbHelper.deleteBook(book.id)
        if (deletedRows > 0) {
            // Remove the book from the list
            books.remove(book)

            // Notify the adapter about the change
            bookAdapter.notifyDataSetChanged()

            Toast.makeText(this, "Book deleted successfully!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Error deleting book!", Toast.LENGTH_SHORT).show()
        }
    }


    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { filterBooks(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { filterBooks(it) }
                return true
            }
        })
    }

    private fun filterBooks(query: String) {
        val filteredBooks = books.filter {
            it.title.contains(query, true) || it.author.contains(query, true) || it.genre.contains(query, true)
        }
        bookAdapter.updateBooks(filteredBooks)
    }
}
