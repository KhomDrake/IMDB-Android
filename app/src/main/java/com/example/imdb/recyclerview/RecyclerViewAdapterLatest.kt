package com.example.imdb.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imdb.R
import com.example.imdb.Auxiliary.getPath
import com.example.imdb.entity.Latest

class RecyclerViewAdapterLatest(
    private val informationMovies: List<Latest>
) : RecyclerView.Adapter<RecyclerViewAdapterLatest.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.information_movies, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val title = "Title: " + informationMovies[position].original_title
        holder.name.text = title

        val path = getPath(informationMovies[position].posterPath)
        Glide.with(holder.itemView).load(path).into(holder.img)
    }

    override fun getItemCount() = informationMovies.count()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name: TextView = itemView.findViewById(R.id.name)
        val img: ImageView = itemView.findViewById(R.id.img)

    }
}