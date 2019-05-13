package com.example.imdb.ui.movies.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imdb.ui.MovieDbCategory
import com.example.imdb.R
import com.example.imdb.ui.becomeInvisible
import com.example.imdb.ui.becomeVisible
import com.example.imdb.data.entity.http.movie.CastMovie
import com.example.imdb.ui.interfaces.IActivityInteraction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RecyclerViewAdapterCast(
    private val informationCastMovie: MutableList<CastMovie>,
    private val idMovie: Int,
    private val IActivityInteraction: IActivityInteraction
) : RecyclerView.Adapter<RecyclerViewAdapterCast.ViewHolder>() {

    private val urlImg = "https://image.tmdb.org/t/p/w300"
    private val imgNotFound = "http://hotspottagger.com/locationimages/noimage.jpg"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.information_cast, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(informationCastMovie[position], urlImg, imgNotFound, idMovie, IActivityInteraction)
    }

    fun setMovieCredit(castMovie: List<CastMovie>) {
        if(castMovie.isEmpty())
            return

        informationCastMovie.clear()
        informationCastMovie.addAll(castMovie)

        notifyDataSetChanged()
    }

    override fun getItemCount() = informationCastMovie.count()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imgCast: ImageView = itemView.findViewById(R.id.img_cast)
        private val nameCast: TextView = itemView.findViewById(R.id.name_cast)
        private val tryAgain: Button = itemView.findViewById(R.id.again)

        fun bind(castMovie: CastMovie, urlImg: String, imgNotFound: String, idMovie: Int, IActivityInteraction: IActivityInteraction) {
            if(castMovie.error) {
                tryAgain.becomeVisible()
                nameCast.becomeInvisible()
                imgCast.becomeInvisible()
            } else {
                tryAgain.becomeInvisible()
                nameCast.becomeVisible()
                imgCast.becomeVisible()
                nameCast.text = "${castMovie.name}"
                val path = getPath(castMovie.profilePath, urlImg, imgNotFound)

                coroutineImage {
                    Glide.with(itemView).load(path).into(imgCast)
                }
            }

            tryAgain.setOnClickListener {
                IActivityInteraction.loadMovies(MovieDbCategory.None)
            }
        }

        private fun coroutineImage(response: suspend () -> Unit) {
            GlobalScope.launch(Dispatchers.Main) {
                response()
            }
        }

        private fun getPath(path: String?, urlImg: String, imgNotFound: String) =
            if (path == "null" || path == "" || path == null) imgNotFound else urlImg + path
    }
}
