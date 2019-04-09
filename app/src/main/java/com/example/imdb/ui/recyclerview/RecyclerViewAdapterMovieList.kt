package com.example.imdb.ui.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imdb.MovieCategory
import com.example.imdb.R
import com.example.imdb.data.entity.http.Movie
import com.example.imdb.ui.mainactivity.RequestCategory

class RecyclerViewAdapterMovieList(
    private val informationMovies: MutableList<Movie>,
    private val requestCategory: RequestCategory,
    private val movieCategory: MovieCategory
) : RecyclerView.Adapter<RecyclerViewAdapterMovieList.ViewHolder>() {

    private val urlImg = "https://image.tmdb.org/t/p/w200"
    private val urlLoading = "https://upload.wikimedia.org/wikipedia/commons/3/3a/Gray_circles_rotate.gif"
    private val imgNotFound = "https://uae.microless.com/cdn/no_image.jpg"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.information_movies, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(informationMovies[position], urlImg, urlLoading, imgNotFound, requestCategory, movieCategory)
    }

    fun setMovies(movies: List<Movie>) {
        if(movies.isEmpty())
            return

        informationMovies.removeAll(informationMovies)

        informationMovies.addAll(movies)
        notifyDataSetChanged()
    }

    override fun getItemCount() = informationMovies.count()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val name: TextView = itemView.findViewById(R.id.name)
        private val img: ImageView = itemView.findViewById(R.id.img)
        private val again: Button = itemView.findViewById(R.id.again)

        fun bind(result: Movie, urlImg: String, urlLoading: String, imgNotFound: String,
                 requestCategory: RequestCategory, movieCategory: MovieCategory) {

            if(result.error)
            {
                img.visibility = View.INVISIBLE
                again.visibility = View.VISIBLE
                again.setOnClickListener {
                    requestCategory.loadCategory(movieCategory)
                }
                return
            }

            again.visibility = View.INVISIBLE
            img.visibility = View.VISIBLE

            if (result.loading) {
                Glide.with(itemView).load(urlLoading).into(img)
                return
            }

            val path = getPath(result.posterPath, urlImg, imgNotFound)

            img.setOnClickListener {
                requestCategory.makeTransition(img, result.id, path)
            }

            val title = "Title: " + result.originalTitle
            name.text = title


            Glide.with(itemView).load(path).into(img)
        }

        private fun getPath(path: String?, urlImg: String, imgNotFound: String) =
            if (path == "null" || path == "" || path == null) imgNotFound else urlImg + path
    }
}