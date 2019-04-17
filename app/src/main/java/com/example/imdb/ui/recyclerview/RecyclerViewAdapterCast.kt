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
import com.example.imdb.auxiliary.becomeInvisible
import com.example.imdb.auxiliary.becomeVisible
import com.example.imdb.data.entity.http.Cast
import com.example.imdb.ui.interfaces.IActivityInteraction

class RecyclerViewAdapterCast(
    private val informationCast: MutableList<Cast>,
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
        holder.bind(informationCast[position], urlImg, imgNotFound, idMovie, IActivityInteraction)
    }

    fun setMovieCredit(cast: List<Cast>) {
        if(cast.isEmpty())
            return

        informationCast.clear()
        informationCast.addAll(cast)

        notifyDataSetChanged()
    }

    override fun getItemCount() = informationCast.count()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imgCast: ImageView = itemView.findViewById(R.id.img_cast)
        private val nameCast: TextView = itemView.findViewById(R.id.name_cast)
        private val tryAgain: Button = itemView.findViewById(R.id.again)

        fun bind(cast: Cast, urlImg: String, imgNotFound: String, idMovie: Int, IActivityInteraction: IActivityInteraction) {

            if(cast.error) {
                tryAgain.becomeVisible()
                nameCast.becomeInvisible()
                imgCast.becomeInvisible()
            } else {
                tryAgain.becomeInvisible()
                nameCast.becomeVisible()
                imgCast.becomeVisible()
                nameCast.text = "${cast.name}"
                val path = getPath(cast.profilePath, urlImg, imgNotFound)
                Glide.with(itemView).load(path).into(imgCast)
            }

            tryAgain.setOnClickListener {
                IActivityInteraction.loadTryAgain(MovieCategory.None)
            }
        }

        private fun getPath(path: String?, urlImg: String, imgNotFound: String) =
            if (path == "null" || path == "" || path == null) imgNotFound else urlImg + path
    }
}