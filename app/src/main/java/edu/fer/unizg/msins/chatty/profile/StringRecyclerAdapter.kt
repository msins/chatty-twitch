package edu.fer.unizg.msins.chatty.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.fer.unizg.msins.chatty.R
import kotlinx.android.synthetic.main.string_item.view.*

class StringRecyclerAdapter(private val values: MutableList<String>) :
    RecyclerView.Adapter<StringRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.string_item, parent, false))
    }

    override fun getItemCount(): Int {
        return values.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.value.text = values[position]
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var value: TextView = view.string_item
    }

    fun updateData(newValues: List<String>) {
        values.clear()
        values.addAll(newValues)
        notifyDataSetChanged()
    }
}