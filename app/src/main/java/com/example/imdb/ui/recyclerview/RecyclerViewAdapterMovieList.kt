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
import com.example.imdb.auxiliary.becomeInvisible
import com.example.imdb.auxiliary.becomeVisible
import com.example.imdb.data.entity.http.Movie
import com.example.imdb.ui.interfaces.IFavorite

class RecyclerViewAdapterMovieList(
    private val informationMovies: MutableList<Movie>,
    private val iFavorite: IFavorite,
    private val movieCategory: MovieCategory
) : RecyclerView.Adapter<RecyclerViewAdapterMovieList.ViewHolder>() {

    private val urlImg = "https://image.tmdb.org/t/p/w300"
    private val imgNotFound = "http://hotspottagger.com/locationimages/noimage.jpg"
    private val plus18 = "http://diarionews.com.br/wp-content/uploads/2018/02/18-logo-1.png"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.information_movies, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(informationMovies[position], urlImg, imgNotFound, iFavorite, movieCategory, plus18)
    }

    fun setMovies(movies: List<Movie>) {
        if(movies.isEmpty())
            return

        informationMovies.clear()
        informationMovies.addAll(movies)

        notifyDataSetChanged()
    }

    override fun getItemCount() = informationMovies.count()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val img: ImageView = itemView.findViewById(R.id.img)
        private val again: Button = itemView.findViewById(R.id.again)
        private val loading: ProgressBar = itemView.findViewById(R.id.loading)
        private val title: TextView = itemView.findViewById(R.id.title_movie)
        private val heartEmpty: ImageView = itemView.findViewById(R.id.heart_empty)
        private val heart: ImageView = itemView.findViewById(R.id.heart)

        private val listToMakeInvisible: List<View>
            get() = listOf(img, again, loading, title, heartEmpty, heart)

        fun bind(result: Movie, urlImg: String, imgNotFound: String,
                 iFavorite: IFavorite, movieCategory: MovieCategory, plus18: String) {

            listToMakeInvisible.forEach { it.visibility = View.INVISIBLE }

            if(result.error)
            {
                again.becomeVisible()
                again.setOnClickListener {
                    iFavorite.loadTryAgain(movieCategory)
                }
                return
            }

            if (result.loading) {
                loading.becomeVisible()
                return
            }

            if(result.favorite) showHeart() else showEmptyHeart()

            img.becomeVisible()

            var path = getPath(result.posterPath, urlImg, imgNotFound)

            if(path == imgNotFound) {
                title.becomeVisible()
                title.text = "${result.title}"
            }

            if(result.adult) path = plus18

            img.setOnClickListener { iFavorite.makeImageTransition(img, result.id, path) }

            heartEmpty.setOnClickListener {
                showHeart()
                iFavorite.favoriteMovie(result.id, true)
                iFavorite.updateVisualMovies()
            }

            heart.setOnClickListener {
                showEmptyHeart()
                iFavorite.favoriteMovie(result.id, false)
                iFavorite.updateVisualMovies()
            }

            Glide.with(itemView).load(path).into(img)
        }

        private fun showHeart() {
            heartEmpty.becomeInvisible()
            heart.becomeVisible()
        }

        private fun showEmptyHeart() {
            heartEmpty.becomeVisible()
            heart.becomeInvisible()
        }

        private fun getPath(path: String?, urlImg: String, imgNotFound: String) =
            if (path == "null" || path == "" || path == null) imgNotFound else urlImg + path
    }
}