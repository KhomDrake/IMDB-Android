package com.example.imdb.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imdb.R
import com.example.imdb.data.entity.Movie

class RecyclerViewAdapterMovieList(
    private val informationMovies: MutableList<Movie>
) : RecyclerView.Adapter<RecyclerViewAdapterMovieList.ViewHolder>() {

    private val urlImg = "https://image.tmdb.org/t/p/w200"
    private val urlLoading = "https://upload.wikimedia.org/wikipedia/commons/3/3a/Gray_circles_rotate.gif"
    private val imgNotFound = "https://uae.microless.com/cdn/no_image.jpg"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.information_movies, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(informationMovies[position], urlImg, urlLoading, imgNotFound)
    }

    fun setMovies(movies: List<Movie>) {
        if(movies.isEmpty())
            return

        informationMovies.removeAll(informationMovies)
        for (movie in movies) {
            informationMovies.add(informationMovies.count(), movie)
        }
        notifyDataSetChanged()
    }

    override fun getItemCount() = informationMovies.count()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val name: TextView = itemView.findViewById(R.id.name)
        private val img: ImageView = itemView.findViewById(R.id.img)

        fun bind(result: Movie, urlImg: String, urlLoading: String, imgNotFound: String) {
            if (result.loading) {
                Glide.with(itemView).load(urlLoading).into(img)
                return
            }

            val title = "Title: " + result.originalTitle
            name.text = title

            val path = getPath(result.posterPath, urlImg, imgNotFound)
            Glide.with(itemView).load(path).into(img)
        }

        private fun getPath(path: String?, urlImg: String, imgNotFound: String) =
            if (path == "null" || path == "" || path == null) imgNotFound else urlImg + path
    }
}