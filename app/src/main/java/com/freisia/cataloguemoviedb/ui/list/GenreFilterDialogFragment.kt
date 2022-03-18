package com.freisia.cataloguemoviedb.ui.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.freisia.cataloguemoviedb.App
import com.freisia.cataloguemoviedb.R
import com.freisia.cataloguemoviedb.data.Resource
import com.freisia.cataloguemoviedb.databinding.DialogFragmentGenreFilterBinding
import com.freisia.cataloguemoviedb.domain.model.Genre
import com.freisia.cataloguemoviedb.utils.EspressoIdlingResource
import com.freisia.cataloguemoviedb.utils.hide
import com.freisia.cataloguemoviedb.utils.show
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class GenreFilterDialogFragment(private val itemListener: GenreItemListener): BottomSheetDialogFragment() {

    companion object {
        const val GENRE_FILTER_TAG = "GENRE_FILTER_TAG"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val movieViewModel: ListViewModel by viewModels { viewModelFactory }
    private lateinit var binding: DialogFragmentGenreFilterBinding
    private var idGenre: String = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).provideListComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogFragmentGenreFilterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setNavigationOnClickListener {
            dismiss()
        }
        binding.toolbar.title = "Filter"
        getData()
        binding.button.setOnClickListener {
            val listChips = binding.genreChipGroup.checkedChipIds
            idGenre = listChips.map {
                it.toString()
            }.joinToString {
                it
            }
            itemListener.onItemGenreClick(idGenre)
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if(dialog != null){
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width,height)
        }
        val dialogs = dialog as BottomSheetDialog
        dialogs.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getData(){
        viewLifecycleOwner.lifecycleScope.launch {
            movieViewModel.genreData.collectLatest { state ->
                if(!EspressoIdlingResource.getEspressoIdlingResourceForMainActivity().isIdleNow){
                    EspressoIdlingResource.decrement()
                }
                when (state) {
                    is Resource.Loading -> {
                        binding.loading.show()
                    }
                    is Resource.Success -> {
                        binding.loading.hide()
                        state.data?.let {
                            setGenresChip(it)
                        }
                    }
                    is Resource.Error -> {
                        binding.loading.hide()
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setGenresChip(genres: List<Genre>) {
        if (binding.genreChipGroup.childCount == 0) {
            genres.forEach {
                val inflater = LayoutInflater.from(binding.genreChipGroup.context)
                val layoutRes = R.layout.item_chip_genre
                val parent = binding.genreChipGroup
                val chip = (inflater.inflate(layoutRes, parent, false) as Chip).apply {
                    text = it.name
                    id = it.id
                    tag = it.name
                }
                binding.genreChipGroup.addView(chip)
            }
        }
    }
}