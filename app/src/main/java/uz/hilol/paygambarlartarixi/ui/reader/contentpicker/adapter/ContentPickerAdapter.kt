package uz.hilol.paygambarlartarixi.ui.reader.contentpicker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uz.hilol.paygambarlartarixi.R

class
ContentPickerAdapter(
    private val context: Context,
    private var list: List<String> = arrayListOf(),
    private val listener: (index: Int) -> Unit
) : RecyclerView.Adapter<ContentPickerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_content, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun setItems(list: List<String>) {
        this.list = list
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = itemView.findViewById(R.id.title_tv)

        init {
            itemView.setOnClickListener {
                listener(bindingAdapterPosition)
            }
        }

        fun onBind(text: String) {
            title.text = text
        }
    }
}