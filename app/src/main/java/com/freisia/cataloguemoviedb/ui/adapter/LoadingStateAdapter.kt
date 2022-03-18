package com.freisia.cataloguemoviedb.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.freisia.cataloguemoviedb.R
import com.freisia.cataloguemoviedb.databinding.LoadStateFooterBinding

class LoadingStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<LoadingStateAdapter.LoadStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadStateViewHolder {
        val inflater =
            LayoutInflater.from(parent.context).inflate(R.layout.load_state_footer, parent, false)
        return LoadStateViewHolder(inflater, retry)
    }

    inner class LoadStateViewHolder(
        itemView: View,
        retry: () -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        val binding = LoadStateFooterBinding.bind(itemView)

        init {
            binding.retryButton.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.errorMsg.text = loadState.error.localizedMessage
            }
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.retryButton.isVisible = loadState !is LoadState.Loading
            binding.errorMsg.isVisible = loadState !is LoadState.Loading
        }
    }

}