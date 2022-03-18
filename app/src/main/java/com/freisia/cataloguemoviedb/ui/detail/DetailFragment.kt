package com.freisia.cataloguemoviedb.ui.detail

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.freisia.cataloguemoviedb.App
import com.freisia.cataloguemoviedb.BuildConfig
import com.freisia.cataloguemoviedb.R
import com.freisia.cataloguemoviedb.data.Resource
import com.freisia.cataloguemoviedb.databinding.FragmentDetailBinding
import com.freisia.cataloguemoviedb.domain.model.Genre
import com.freisia.cataloguemoviedb.domain.model.MovieDetail
import com.freisia.cataloguemoviedb.ui.adapter.LoadingStateAdapter
import com.freisia.cataloguemoviedb.ui.adapter.ReviewPagingAdapter
import com.freisia.cataloguemoviedb.ui.adapter.VideoTrailerAdapter
import com.freisia.cataloguemoviedb.utils.autoplayer.AutoPlayerManager
import com.freisia.cataloguemoviedb.utils.hide
import com.freisia.cataloguemoviedb.utils.show
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.chip.Chip
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

class DetailFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: DetailViewModel by viewModels { viewModelFactory }
    private var reviewPagingAdapter: ReviewPagingAdapter? = null
    private var videoTrailerAdapter: VideoTrailerAdapter? = null
    private lateinit var binding: FragmentDetailBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).provideDetailComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments?.getInt("movie")
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireActivity(), R.color.overlay_dark_30)
        setAppBarScrollListener()
        getData(args as Int)
        setupRecyclerViewReview()
        initGetReviews(args)
        setupRecyclerViewTrailer()
        initGetTrailer(args)
        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireActivity(), R.color.dark_blue)
        reviewPagingAdapter = null
        videoTrailerAdapter = null
    }

    private fun setupRecyclerViewTrailer(){
        binding.layoutEmptyTrailer.messageValue.text = getString(R.string.no_trailer_found)
        videoTrailerAdapter = VideoTrailerAdapter()
        videoTrailerAdapter?.addLoadStateListener { loadState ->
            when(loadState.source.refresh){
                is LoadState.NotLoading -> {
                    if(videoTrailerAdapter!!.itemCount < 1){
                        notFoundVideoTrailer()
                    } else foundVideoTrailer()
                }
                is LoadState.Loading -> loadingVideoTrailer()
                is LoadState.Error -> notFoundVideoTrailer()
            }
        }
        val autoPlayManager = AutoPlayerManager(this)
        autoPlayManager.autoPlayerId = R.id.auto_player
        autoPlayManager.useController = true
        autoPlayManager.attachRecyclerView(binding.rvTrailer)
        autoPlayManager.setup()
        binding.rvTrailer.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            isNestedScrollingEnabled = false
            adapter = videoTrailerAdapter?.withLoadStateHeaderAndFooter(
                header = LoadingStateAdapter{ videoTrailerAdapter?.retry() },
                footer = LoadingStateAdapter{ videoTrailerAdapter?.retry() }
            )
            setHasFixedSize(true)
            setFocusable(false)
            setItemViewCacheSize(10)
        }
    }

    private fun initGetTrailer(id:Int){
        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.getVideoTrailerData(id).collectLatest {
                videoTrailerAdapter?.submitData(it)
            }
        }
    }

    private fun setupRecyclerViewReview(){
        binding.layoutEmptyReview.messageValue.text = getString(R.string.no_review)
        reviewPagingAdapter = ReviewPagingAdapter()
        reviewPagingAdapter?.addLoadStateListener { loadState ->
            when(loadState.source.refresh){
                is LoadState.NotLoading -> {
                    if(reviewPagingAdapter!!.itemCount < 1){
                        notFoundReview()
                    } else foundReview()
                }
                is LoadState.Loading -> loadingReview()
                is LoadState.Error -> notFoundReview()
            }
        }
        binding.rvReview.apply {
            layoutManager = LinearLayoutManager(context)
            isNestedScrollingEnabled = false
            adapter = reviewPagingAdapter?.withLoadStateHeaderAndFooter(
                header = LoadingStateAdapter{ reviewPagingAdapter?.retry() },
                footer = LoadingStateAdapter{ reviewPagingAdapter?.retry() }
            )
            setHasFixedSize(true)
            setFocusable(false)
            setItemViewCacheSize(10)
        }
    }

    private fun initGetReviews(id:Int){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getReviewData(id).collectLatest {
                reviewPagingAdapter?.submitData(it)
            }
        }
    }

    private fun getData(id: Int) {
        viewModel.getDetailMovie(id)
        viewModel.movieDetail.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Loading -> {
                    binding.loading.show()
                }
                is Resource.Success -> {
                    binding.loading.hide()
                    state.data?.let {
                        renderMovieView(it)
                    }
                }
                is Resource.Error -> {
                    binding.loading.hide()
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun renderMovieView(movie: MovieDetail) {
        Glide.with(requireContext())
            .load(
                if (movie.backdropImage == null) R.drawable.cover_placeholder else
                    BuildConfig.BASE_IMAGE_URL + movie.backdropImage
            )
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.cover_placeholder)
                    .transform(CenterCrop())
            )
            .into(binding.imgBackdrop)

        binding.toolbar.title = movie.title
        when(Build.MANUFACTURER.uppercase()){
            "OPPO" -> {
                binding.layoutRating.rating.setProgress(movie.voteAverage.times(10).toFloat(), true)
                binding.layoutRating.rating.progressColor = when (movie.voteAverage.times(10).toInt()) {
                    in 0..39 -> ContextCompat.getColor(binding.layoutRating.rating.context, R.color.red)
                    in 40..69 -> ContextCompat.getColor(binding.layoutRating.rating.context, R.color.gold)
                    else -> ContextCompat.getColor(binding.layoutRating.rating.context, R.color.green)
                }
                binding.layoutRating.textRating.text = getString(R.string.placeholder_rating,movie.voteAverage.times(10).toInt())
                binding.layoutRating.textRating.setTextColor(binding.layoutRating.rating.progressColor)
            }
            else -> {
                binding.layoutRating.textRating.hide()
                binding.layoutRating.rating.setProgress(movie.voteAverage.times(10).toFloat(), true)
                binding.layoutRating.rating.progressColor = when (movie.voteAverage.times(10).toInt()) {
                    in 0..39 -> ContextCompat.getColor(binding.layoutRating.rating.context, R.color.red)
                    in 40..69 -> ContextCompat.getColor(binding.layoutRating.rating.context, R.color.gold)
                    else -> ContextCompat.getColor(binding.layoutRating.rating.context, R.color.green)
                }
                binding.layoutRating.rating.textColor = binding.layoutRating.rating.progressColor
            }
        }
        Log.i("DetailFragment",binding.layoutRating.rating.progress.toString())
        binding.status.text = movie.status
        binding.overview.text =
            if (movie.overview == "") getString(R.string.no_overview) else movie.overview
        setGenresChip(movie.genre)
    }

    private fun setGenresChip(genres: List<Genre>) {
        if (binding.genreChipGroup.childCount == 0) {
            genres.forEach {
                val inflater = LayoutInflater.from(binding.genreChipGroup.context)
                val layoutRes = R.layout.item_genre
                val parent = binding.genreChipGroup
                val chip = (inflater.inflate(layoutRes, parent, false) as Chip).apply {
                    text = it.name
                    isCheckable = false
                    isClickable = false
                }
                binding.genreChipGroup.addView(chip)
            }
        }
    }

    private fun setAppBarScrollListener() {
        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, i ->
            if (abs(i) - appBarLayout.totalScrollRange == 0) {
                requireActivity().window.statusBarColor =
                    ContextCompat.getColor(requireActivity(), R.color.dark_blue)
            } else {
                requireActivity().window.statusBarColor =
                    ContextCompat.getColor(requireActivity(), R.color.overlay_dark_30)
            }
        })
    }

    private fun loadingReview() {
        binding.loadingReview.show()
        binding.rvReview.hide()
        binding.layoutEmptyReview.layoutEmpty.hide()
    }

    private fun notFoundReview() {
        binding.loadingReview.hide()
        binding.rvReview.hide()
        binding.layoutEmptyReview.layoutEmpty.show()
    }

    private fun foundReview() {
        binding.loadingReview.hide()
        binding.rvReview.show()
        binding.layoutEmptyReview.layoutEmpty.hide()
    }

    private fun loadingVideoTrailer() {
        binding.loadingTrailer.show()
        binding.rvTrailer.hide()
        binding.layoutEmptyTrailer.layoutEmpty.hide()
    }

    private fun notFoundVideoTrailer() {
        binding.loadingTrailer.hide()
        binding.rvTrailer.hide()
        binding.layoutEmptyTrailer.layoutEmpty.show()
    }

    private fun foundVideoTrailer() {
        binding.loadingTrailer.hide()
        binding.rvTrailer.show()
        binding.layoutEmptyTrailer.layoutEmpty.hide()
    }

}