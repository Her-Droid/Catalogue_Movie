package com.freisia.cataloguemoviedb.ui.adapter

import com.freisia.cataloguemoviedb.domain.model.Movie

interface MovieItemListener {

    fun onItemClicked(data: Movie)

}