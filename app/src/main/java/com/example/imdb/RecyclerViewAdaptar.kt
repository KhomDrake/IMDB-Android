package com.example.imdb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecyclerViewAdapter(
    var informationMovies: MutableList<String>
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.information_movies, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = informationMovies[position]
//        Glide.with(holder.itemView).load(repositoryInformation[position].urlImg).into(holder.img)
    }

    override fun getItemCount() = informationMovies.count()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name: TextView
        val img: ImageView

        init {
            name = itemView.findViewById(R.id.name)
            img = itemView.findViewById(R.id.img)
        }
    }

    fun addRepositoryInformation(item: String) {
        val i = itemCount
        informationMovies.add(i, item)
    }
}