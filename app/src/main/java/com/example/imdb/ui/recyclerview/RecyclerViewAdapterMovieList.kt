package com.example.imdb.ui.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imdb.MovieCategory
import com.example.imdb.R
import com.example.imdb.data.entity.http.Movie
import com.example.imdb.ui.RequestCategory

class RecyclerViewAdapterMovieList(
    private val informationMovies: MutableList<Movie>,
    private val requestCategory: RequestCategory,
    private val movieCategory: MovieCategory
) : RecyclerView.Adapter<RecyclerViewAdapterMovieList.ViewHolder>() {

    private val urlImg = "https://image.tmdb.org/t/p/w300"
    private val imgNotFound = "http://hotspottagger.com/locationimages/noimage.jpg"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.information_movies, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(informationMovies[position], urlImg, imgNotFound, requestCategory, movieCategory)
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

        private val img: ImageView = itemView.findViewById(R.id.img)
        private val again: Button = itemView.findViewById(R.id.again)
        private val loading: ProgressBar = itemView.findViewById(R.id.loading)
        private val title: TextView = itemView.findViewById(R.id.title_movie)
        private val listToMakeInvisible: List<View>
            get() = listOf(img, again, loading, title)

        fun bind(result: Movie, urlImg: String, imgNotFound: String,
                 requestCategory: RequestCategory, movieCategory: MovieCategory) {

            listToMakeInvisible.forEach { it.visibility = View.INVISIBLE }

            if(result.error)
            {
                again.visibility = View.VISIBLE
                again.setOnClickListener {
                    requestCategory.loadCategory(movieCategory)
                }
                return
            }

            if (result.loading) {
                loading.visibility = View.VISIBLE
                return
            }

            img.visibility = View.VISIBLE

            val path = getPath(result.posterPath, urlImg, imgNotFound)

            if(path == imgNotFound) {
                title.visibility = View.VISIBLE
                title.text = "${result.title}"
            }

            img.setOnClickListener {
                requestCategory.makeTransition(img, result.id, path)
            }
            Glide.with(itemView).load(path).into(img)
        }

        private fun getPath(path: String?, urlImg: String, imgNotFound: String) =
            if (path == "null" || path == "" || path == null) imgNotFound else urlImg + path
    }
}