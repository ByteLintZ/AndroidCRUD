package com.example.cruduts.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cruduts.R
import com.example.cruduts.model.Book


class BookAdapter(
    private var books: List<Book>,
    private val onBookClick: (Book) -> Unit
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvAuthor: TextView = itemView.findViewById(R.id.tvAuthor)
        private val tvGenre: TextView = itemView.findViewById(R.id.tvGenre)
        private val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        private val tvType: TextView = itemView.findViewById(R.id.tvType)

        fun bind(book: Book) {
            tvTitle.text = book.title
            tvAuthor.text = book.author
            tvGenre.text = "Genre: ${book.genre}"
            tvPrice.text = "$${book.price}"
            tvType.text = book.type

            // Set click listener for item
            itemView.setOnClickListener { onBookClick(book) }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.bind(book)
        holder.itemView.setOnClickListener { onBookClick(book) } // Handle clicks
    }

    override fun getItemCount(): Int = books.size

    // Helper method to update the list dynamically
    fun updateBooks(newBooks: List<Book>) {
        books = newBooks
        notifyDataSetChanged()
    }
}
