package com.example.imdb.ui.recyclerview

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.MovieCategory
import com.example.imdb.R
import com.example.imdb.TAG_VINI
import com.example.imdb.data.entity.http.Review
import com.example.imdb.data.entity.http.Reviews
import com.example.imdb.ui.ActivityInteraction

class RecyclerViewAdapterReviews (
    private val informationReview: MutableList<Review>,
    private val activityInteraction: ActivityInteraction
) : RecyclerView.Adapter<RecyclerViewAdapterReviews.ViewHolderReview>() {

    private var idMovie: Int = -3000

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderReview {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.information_review, parent, false)
        return ViewHolderReview(view)
    }

    override fun onBindViewHolder(holder: ViewHolderReview, position: Int) {
        holder.bind(informationReview[position], activityInteraction)
    }

    override fun getItemCount() = informationReview.count()

    fun setReviews(reviews: Reviews) {
        idMovie = reviews.id
        informationReview.clear()
        informationReview.addAll(reviews.results)
        notifyDataSetChanged()
    }

    class ViewHolderReview(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val name: TextView = itemView.findViewById(R.id.name)
        private val content: TextView = itemView.findViewById(R.id.content)
        private val tryAgain: Button = itemView.findViewById(R.id.again)

        fun bind(review: Review, activityInteraction: ActivityInteraction) {

            tryAgain.setOnClickListener { activityInteraction.loadTryAgain(MovieCategory.None) }

            if(review.error) {
                content.visibility = View.INVISIBLE
                name.visibility = View.INVISIBLE
                tryAgain.visibility = View.VISIBLE
            } else {
                tryAgain.visibility = View.INVISIBLE
                content.visibility = View.VISIBLE
                name.visibility = View.VISIBLE
                name.text = "Autor: ${review.author}"
                content.text = if(review.content.isEmpty() || review.content.isBlank()) "Nenhum comentário"  else review.content
            }
        }
    }
}