package com.example.imdb.ui.movies.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imdb.ui.TheMovieDbCategory
import com.example.imdb.R
import com.example.imdb.ui.becomeInvisible
import com.example.imdb.ui.becomeVisible
import com.example.imdb.data.entity.http.movie.Movie
import com.example.imdb.ui.interfaces.IFavorite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RecyclerViewAdapterMovieList(
    private val informationMovies: MutableList<Movie>,
    private val iFavorite: IFavorite,
    val theMovieDbCategory: TheMovieDbCategory
) : RecyclerView.Adapter<RecyclerViewAdapterMovieList.ViewHolder>() {

    private val urlImg = "https://image.tmdb.org/t/p/w300"
    private val imgNotFound = "http://hotspottagger.com/locationimages/noimage.jpg"
    private val plus18 = "http://diarionews.com.br/wp-content/uploads/2018/02/18-logo-1.png"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.information_entertainment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(informationMovies[position], urlImg, imgNotFound, iFavorite, theMovieDbCategory, plus18)
    }

    fun setMovies(movies: List<Movie>) {
        if(movies.isEmpty()) return

        removeLoading()

        informationMovies.clear()
        informationMovies.addAll(movies)

        coroutineImage { notifyDataSetChanged() }
    }

    private fun removeLoading() {
        informationMovies.forEach {
            if(it.loading) informationMovies.remove(it)
        }
    }

    fun addMovies(movies: List<Movie>) {
        if(movies.isEmpty()) return

        removeLoading()
        informationMovies.addAll(movies)

        coroutineImage { notifyDataSetChanged() }
    }

    fun getMoviePosition(idMovie: Int) : Int {
        var position = -1
        for (i in 0 until informationMovies.count()) {
            if(informationMovies[i].id == idMovie) position = i
        }
        return position
    }

    fun favoriteMovie(position: Int, toFavorite: Boolean) {
        if(position < 0)
            return
        informationMovies[position].favorite = toFavorite
        notifyItemChanged(position)
    }

    fun removeMovie(position: Int) {
        informationMovies.removeAt(position)
        notifyDataSetChanged()
    }

    fun hasLoading() : Boolean {
        informationMovies.forEach { if(it.loading) return true }
        return false
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
                 iFavorite: IFavorite, theMovieDbCategory: TheMovieDbCategory, plus18: String) {

            listToMakeInvisible.forEach { it.visibility = View.INVISIBLE }

            if(result.error)
            {
                again.becomeVisible()
                again.setOnClickListener { iFavorite.loadMovies(theMovieDbCategory) }
                return
            }

            if (result.loading) {
                loading.becomeVisible()
                return
            }


            var path = getPath(result.posterPath, urlImg, imgNotFound)

            if(path == imgNotFound) {
                title.becomeVisible()
                title.text = "${result.title}"
            }

            if(result.adult) path = plus18

            img.setOnClickListener { iFavorite.makeImageTransition(img, result.id, path) }

            coroutineImage {
                if(result.favorite) showHeart() else showEmptyHeart()
                heartEmpty.setOnClickListener {
                    showHeart()
                    iFavorite.favoriteMovie(result.id, true)
                    iFavorite.updateVisualMovie(result.id, true)
                }

                heart.setOnClickListener {
                    showEmptyHeart()
                    iFavorite.favoriteMovie(result.id, false)
                    iFavorite.updateVisualMovie(result.id, false)
                }

                Glide.with(itemView).load(path).into(img)
                img.becomeVisible()
            }
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
            if (path == null) imgNotFound else urlImg + path
    }

    fun hasNoFavorites() = informationMovies.isEmpty()

    companion object {
        fun coroutineImage(response: suspend () -> Unit) = GlobalScope.launch(Dispatchers.Main) { response() }
    }
}


