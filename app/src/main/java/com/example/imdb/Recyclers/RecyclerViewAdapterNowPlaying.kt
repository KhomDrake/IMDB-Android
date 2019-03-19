package com.example.imdb.Recyclers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imdb.R
import com.example.imdb.entity.Result

class RecyclerViewAdapterNowPlaying(
    var informationMovies: MutableList<Result>
) : RecyclerView.Adapter<RecyclerViewAdapterNowPlaying.ViewHolder>() {

    private val urlImg = "https://image.tmdb.org/t/p/w200"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.information_movies, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val title = "Title: " + informationMovies[position].originalTitle
        holder.name.text = title

        var path = informationMovies[position].posterPath

        if(path == null || path == "")
            path = "https://uae.microless.com/cdn/no_image.jpg"
        else
            path = urlImg + path

        Glide.with(holder.itemView).load(path).into(holder.img)
    }

    override fun getItemCount() = informationMovies.count()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name: TextView = itemView.findViewById(R.id.name)
        val img: ImageView = itemView.findViewById(R.id.img)

    }
}