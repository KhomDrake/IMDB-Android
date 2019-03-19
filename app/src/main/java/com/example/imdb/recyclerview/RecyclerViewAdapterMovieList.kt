package com.example.imdb.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imdb.Auxiliary
import com.example.imdb.R
import com.example.imdb.entity.Result

class RecyclerViewAdapterMovieList(
    private val informationMovies: List<Result>
) : RecyclerView.Adapter<RecyclerViewAdapterMovieList.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.information_movies, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(informationMovies[position])
    }

    override fun getItemCount() = informationMovies.count()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val name: TextView = itemView.findViewById(R.id.name)
        private val img: ImageView = itemView.findViewById(R.id.img)

        fun bind(result: Result){
            val title = "Title: " + result.originalTitle
            name.text = title

            val path = Auxiliary.getPath(result.posterPath)
            Glide.with(itemView).load(path).into(img)

        }

    }
}