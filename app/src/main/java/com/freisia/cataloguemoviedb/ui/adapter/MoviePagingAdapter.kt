package com.freisia.cataloguemoviedb.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.freisia.cataloguemoviedb.BuildConfig
import com.freisia.cataloguemoviedb.R
import com.freisia.cataloguemoviedb.databinding.ItemMovieBinding
import com.freisia.cataloguemoviedb.domain.model.Movie

class MoviePagingAdapter(private val itemListener: MovieItemListener) :
    PagingDataAdapter<Movie, MoviePagingAdapter.MovieViewHolder>(MOVIE_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class MovieViewHolder(private val itemBinding: ItemMovieBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(movie: Movie) {
            Glide.with(itemBinding.container.context)
                .load(
                    if (movie.image == null) R.drawable.cover_placeholder else
                        BuildConfig.BASE_IMAGE_URL + movie.image
                )
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.cover_placeholder)
                        .transform(RoundedCorners(20))
                )
                .into(itemBinding.imgCover)

            with(itemBinding) {
                tvOverview.text =
                    if (movie.overview != "") movie.overview else itemBinding.container.context.getString(
                        R.string.no_overview
                    )
                tvTitle.text = movie.title
                ratingBar.rating = movie.voteAverage.div(2).toFloat()
                tvVoteCount.text =
                    itemBinding.container.context.getString(R.string.vote_count, movie.voteCount)
            }
            itemView.setOnClickListener { itemListener.onItemClicked(movie) }
        }
    }

    companion object {
        private val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                // id is unique
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem == newItem

        }
    }

}