package com.example.cruduts

import android.database.Cursor
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.cruduts.model.DBHelper

class MainActivity : AppCompatActivity() {

    private lateinit var myDb: DBHelper
    private lateinit var editName: EditText
    private lateinit var editSurname: EditText
    private lateinit var edittitle: EditText
    private lateinit var editTextId: EditText
    private lateinit var btnAddData: Button
    private lateinit var btnViewAll: Button
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myDb = DBHelper(this)

        editName = findViewById(R.id.editText_name)
        editSurname = findViewById(R.id.editText_surname)
        edittitle = findViewById(R.id.editText_title)
        editTextId = findViewById(R.id.editTextId)

        btnAddData = findViewById(R.id.button_add)
        btnViewAll = findViewById(R.id.button_view)
        btnUpdate = findViewById(R.id.button_update)
        btnDelete = findViewById(R.id.button_delete)

        addData()
        viewAll()
        updateData()
        deleteData()
    }

    // Fungsi hapus
    private fun deleteData() {
        btnDelete.setOnClickListener {
            val deletedRows = myDb.deleteData(editTextId.text.toString())
            if (deletedRows > 0)
                Toast.makeText(this@MainActivity, "Data Deleted", Toast.LENGTH_LONG).show()
            else
                Toast.makeText(this@MainActivity, "Data Failed to Delete!", Toast.LENGTH_LONG).show()
        }
    }

    // Fungsi update
    private fun updateData() {
        btnUpdate.setOnClickListener {
            val isUpdate = myDb.updateData(
                editTextId.text.toString(),
                editName.text.toString(),
                editSurname.text.toString(),
                "pusy"
            )
            if (isUpdate)
                Toast.makeText(this@MainActivity, "Data Updated", Toast.LENGTH_LONG).show()
            else
                Toast.makeText(this@MainActivity, "Data Failed to Update", Toast.LENGTH_LONG).show()
        }
    }

    // Fungsi tambah
    private fun addData() {
        btnAddData.setOnClickListener {
            val isInserted = myDb.insertData(
                editName.text.toString(),
                editSurname.text.toString(),
                edittitle.text.toString()
            )
            if (isInserted)
                Toast.makeText(this@MainActivity, "Data Inserted", Toast.LENGTH_LONG).show()
            else
                Toast.makeText(this@MainActivity, "Data Not Inserted", Toast.LENGTH_LONG).show()
        }
    }

    // Fungsi menampilkan data
    private fun viewAll() {
        btnViewAll.setOnClickListener {
            val res: Cursor = myDb.getAllData()
            if (res.count == 0) {
                // Show message
                showMessage("Error", "Nothing Found")
                return@setOnClickListener
            }

            val buffer = StringBuffer()
            while (res.moveToNext()) {
                buffer.append("Id :${res.getString(0)}\n")
                buffer.append("Name :${res.getString(1)}\n")
                buffer.append("Surname :${res.getString(2)}\n")
                buffer.append("title :${res.getString(3)}\n\n")
            }

            // Show all data
            showMessage("Data", buffer.toString())
        }
    }

    // Membuat alert dialog
    private fun showMessage(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.show()
    }
}
