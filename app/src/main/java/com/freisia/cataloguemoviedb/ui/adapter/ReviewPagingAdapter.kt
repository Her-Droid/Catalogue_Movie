package com.freisia.cataloguemoviedb.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.freisia.cataloguemoviedb.BuildConfig
import com.freisia.cataloguemoviedb.R
import com.freisia.cataloguemoviedb.databinding.ItemReviewBinding
import com.freisia.cataloguemoviedb.domain.model.Review
import com.freisia.cataloguemoviedb.utils.DateConverter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ReviewPagingAdapter : PagingDataAdapter<Review,ReviewPagingAdapter.ReviewViewHolder>(
    REVIEW_COMPARATOR) {

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    inner class ReviewViewHolder(private val itemReviewBinding: ItemReviewBinding) :
        RecyclerView.ViewHolder(itemReviewBinding.root){

        fun bind(review: Review){
            Glide.with(itemReviewBinding.container.context)
                .load(
                    when {
                        review.image == null -> R.drawable.cover_placeholder
                        review.image.length <= 32 -> BuildConfig.BASE_IMAGE_URL + review.image
                        else -> review.image.drop(1)
                    }
                ).into(itemReviewBinding.imgCover)
            with(itemReviewBinding){
                tvTitle.text = review.author
                tvOverview.text =
                    if (review.content != "") review.content else itemReviewBinding.container.context.getString(
                        R.string.no_review
                    )
                ratingBar.rating = review.rating.div(2)
                val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS",
                    Locale.ENGLISH
               )
                val date: Date =
                    dateFormat.parse(review.updateDate) as Date
                tvUpdated.text = DateConverter.timeAgo(date)
            }
        }
    }

    companion object{
        private val REVIEW_COMPARATOR = object : DiffUtil.ItemCallback<Review>(){
            override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
                return oldItem == newItem
            }
        }
    }


}