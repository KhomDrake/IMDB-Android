package com.example.imdb.ui.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.R
import com.example.imdb.data.entity.Review
import com.example.imdb.data.entity.Reviews

class RecyclerViewAdapterReviews (
    private val informationReview: MutableList<Review>
) : RecyclerView.Adapter<RecyclerViewAdapterReviews.ViewHolderReview>() {

    private var idMovie: Int = -3000

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderReview {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.information_review, parent, false)
        return ViewHolderReview(view)
    }

    override fun onBindViewHolder(holder: ViewHolderReview, position: Int) {
        holder.bind(informationReview[position])
    }

    override fun getItemCount() = informationReview.count()

    fun setReviews(reviews: Reviews) {
        idMovie = reviews.id
        informationReview.removeAll(informationReview)
        informationReview.addAll(reviews.results)
        notifyDataSetChanged()
    }

    class ViewHolderReview(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val name: TextView = itemView.findViewById(R.id.name)
        private val content: TextView = itemView.findViewById(R.id.content)

        fun bind(review: Review) {
            name.text = "Autor: ${review.author}"
            content.text = review.content
        }
    }
}