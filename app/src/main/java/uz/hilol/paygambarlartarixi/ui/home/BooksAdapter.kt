package uz.hilol.paygambarlartarixi.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.hilol.paygambarlartarixi.data.remote.BookData
import uz.hilol.paygambarlartarixi.databinding.ItemBookBinding

class BooksAdapter :
    RecyclerView.Adapter<BooksAdapter.BookViewHolder>() {

    private val books = listOf(
        BookData("Book 1", "Image"),
        BookData("Book 2", "Image"),
        BookData("Book 3", "Image"),
        BookData("Book 4", "Image"),
        BookData("Book 5", "Image"),
    )

    inner class BookViewHolder(val binding: ItemBookBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    private var onItemClickListener: ((BookData) -> Unit)? = null


    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val item = books[position]
        with(holder.binding) {
            tvBookName.text = item.name
            book.setOnClickListener { onItemClickListener?.invoke(item) }
        }
    }

    override fun getItemCount(): Int = books.size


    fun setOnItemClickListener(listener: (BookData) -> Unit) {
        onItemClickListener = listener
    }
}
