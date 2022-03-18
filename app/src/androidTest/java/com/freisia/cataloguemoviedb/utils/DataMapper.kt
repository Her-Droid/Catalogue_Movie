package com.freisia.cataloguemoviedb.utils

import com.freisia.cataloguemoviedb.data.source.response.*
import com.freisia.cataloguemoviedb.domain.model.*

fun List<ResultMoviesResponse>.toListMovieDomain(): List<Movie> {
    return this.map {
        Movie(
            id = it.id,
            title = it.title,
            overview = it.overview,
            image = it.image,
            voteAverage = it.voteAverage,
            voteCount = it.voteCount
        )
    }
}

fun MovieResponse.toMovieDetailDomain(): MovieDetail {
    return MovieDetail(
        id = this.id,
        title = this.title,
        overview = this.overview,
        image = this.image,
        backdropImage = this.backdropImage,
        voteAverage = this.voteAverage,
        voteCount = this.voteCount,
        status = this.status,
        genre = this.genre.map {
            Genre(id = it.id, name = it.name)
        }
    )
}

fun List<GenreResponse>.toListGenreDomain() : List<Genre> {
    return this.map{
        Genre(
            id = it.id,
            name = it.name
        )
    }
}

fun List<ResultReviewResponse>.toListReviewDomain(): List<Review> {
    return this.map{
        Review(
            id = it.id,
            author = it.author,
            image = it.authorDetails.image,
            rating = it.authorDetails.rating,
            content = it.content,
            updateDate = it.updateDate
        )
    }
}

fun List<VideoResultResponse>.toListVideoTrailerDomain(): List<VideoTrailer>{
    return this.map{
        VideoTrailer(
            id = it.id,
            name = it.name,
            key = it.key,
            site = it.site,
            type = it.type
        )
    }
}