package com.freisia.cataloguemoviedb.ui.list

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.freisia.cataloguemoviedb.App
import com.freisia.cataloguemoviedb.R
import com.freisia.cataloguemoviedb.databinding.FragmentListBinding
import com.freisia.cataloguemoviedb.domain.model.Movie
import com.freisia.cataloguemoviedb.ui.adapter.LoadingStateAdapter
import com.freisia.cataloguemoviedb.ui.adapter.MovieItemListener
import com.freisia.cataloguemoviedb.ui.adapter.MoviePagingAdapter
import com.freisia.cataloguemoviedb.utils.EspressoIdlingResource
import com.freisia.cataloguemoviedb.utils.hide
import com.freisia.cataloguemoviedb.utils.show
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListFragment : Fragment(), MovieItemListener, GenreItemListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val movieViewModel: ListViewModel by viewModels { viewModelFactory }
    private var movieAdapter: MoviePagingAdapter? = null
    private lateinit var binding: FragmentListBinding
    private lateinit var bottomSheetDialogFragment: GenreFilterDialogFragment

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).provideListComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EspressoIdlingResource.increment()
        setupRecyclerView()
        initObserver()
        bottomSheetDialogFragment = GenreFilterDialogFragment(this)
        binding.fab.setOnClickListener {
            bottomSheetDialogFragment.show(childFragmentManager,GenreFilterDialogFragment.GENRE_FILTER_TAG)
        }
    }

    // avoid memory leak
    override fun onDestroyView() {
        super.onDestroyView()
        movieAdapter = null
    }

    override fun onItemClicked(data: Movie) {
        navigateToMovieDetail(data)
    }

    override fun onItemGenreClick(genre: String?) {
        EspressoIdlingResource.increment()
        movieViewModel.setGenre(genre)
        movieViewModel.onClear()
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            movieViewModel.data.collectLatest {
                if(!EspressoIdlingResource.getEspressoIdlingResourceForMainActivity().isIdleNow){
                    EspressoIdlingResource.decrement()
                }
                movieAdapter?.submitData(it)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.layoutEmptyList.messageValue.text = getString(R.string.no_movie_found)
        movieAdapter = MoviePagingAdapter(this)
        movieAdapter?.addLoadStateListener { loadState ->
            when(loadState.source.refresh){
                is LoadState.NotLoading -> {
                    if(movieAdapter!!.itemCount < 1){
                        notFound()
                    } else found()
                }
                is LoadState.Loading -> loading()
                is LoadState.Error -> notFound()
            }
        }

        binding.rvMovieTv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = movieAdapter?.withLoadStateHeaderAndFooter(
                header = LoadingStateAdapter { movieAdapter?.retry() },
                footer = LoadingStateAdapter { movieAdapter?.retry() }
            )
            setHasFixedSize(true)
        }
    }

    private fun navigateToMovieDetail(movie: Movie) {
        val bundle = bundleOf("movie" to movie.id)
        findNavController().navigate(R.id.action_navigation_home_to_navigation_detail,bundle)
    }

    private fun loading() {
        binding.loading.show()
        binding.rvMovieTv.hide()
        binding.layoutEmptyList.layoutEmpty.hide()
    }

    private fun notFound() {
        binding.loading.hide()
        binding.rvMovieTv.hide()
        binding.layoutEmptyList.layoutEmpty.show()
    }

    private fun found() {
        binding.loading.hide()
        binding.rvMovieTv.show()
        binding.layoutEmptyList.layoutEmpty.hide()
    }

}