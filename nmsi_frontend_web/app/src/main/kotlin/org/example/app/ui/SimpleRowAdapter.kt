package org.example.app.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.example.app.R

/**
 * Simple RecyclerView adapter rendering (title, subtitle) rows.
 */
class SimpleRowAdapter(
    private var items: List<RowItem> = emptyList(),
) : RecyclerView.Adapter<SimpleRowAdapter.VH>() {

    data class RowItem(
        val title: String,
        val subtitle: String,
    )

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val subtitle: TextView = itemView.findViewById(R.id.subtitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_simple_row, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.title.text = item.title
        holder.subtitle.text = item.subtitle
    }

    override fun getItemCount(): Int = items.size

    // PUBLIC_INTERFACE
    fun submit(newItems: List<RowItem>) {
        /** Replace adapter data set. */
        items = newItems
        notifyDataSetChanged()
    }
}
