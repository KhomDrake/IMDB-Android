package com.example.imdb.ui.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.R
import com.example.imdb.data.entity.http.Cast

class RecyclerViewAdapterCast(
    private val informationCast: MutableList<Cast>,
    private val idMovie: Int
) : RecyclerView.Adapter<RecyclerViewAdapterCast.ViewHolder>() {

    private val urlImg = "https://image.tmdb.org/t/p/w300"
    private val imgNotFound = "http://hotspottagger.com/locationimages/noimage.jpg"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.information_movies, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(informationCast[position], urlImg, imgNotFound, idMovie)
    }

    fun setMovieCredit(cast: List<Cast>) {
        if(cast.isEmpty())
            return

        notifyDataSetChanged()
    }

    override fun getItemCount() = informationCast.count()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(cast: Cast, urlImg: String, imgNotFound: String, idMovie: Int) {

        }

        private fun getPath(path: String?, urlImg: String, imgNotFound: String) =
            if (path == "null" || path == "" || path == null) imgNotFound else urlImg + path
    }
}