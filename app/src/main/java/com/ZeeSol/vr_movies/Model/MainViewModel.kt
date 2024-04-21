package com.ZeeSol.vr_movies.Model

import android.app.Application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ZeeSol.vr_movies.ApiService
import com.ZeeSol.vr_movies.Room.AppDatabase
import com.ZeeSol.vr_movies.Room.MovieEntity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// ViewModel Class
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val movieDao = AppDatabase.getDatabase(application).movieDao()
    private val apiService = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    val movies: LiveData<List<MovieEntity>> = movieDao.getAllMovies()
    private val _trailerUrl = MutableLiveData<String?>()
    private var savedTrailerUrl: String? = null
    val trailerUrl: LiveData<String?> = _trailerUrl
    fun fetchMoviesIfNeeded() {
        viewModelScope.launch {
            try {
                val apiData = apiService.getMovies()
                val movies = apiData.results.map { movie ->
                    MovieEntity(
                        id = movie.id,
                        title = movie.title,
                        releaseDate = movie.releaseDate,
                        overview = movie.overview,
                        posterPath = movie.posterPath,
                        genreIds = movie.genreIds,

                    )
                }
                movieDao.insertAll(movies)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d(TAG, "Failed to fetch movies: ${e.message}")
            }
        }
    }
    fun fetchMovieTrailer(movieId: Int,title:String) {
        viewModelScope.launch {
            try {
                val trailersResponse = apiService.getTrendingMovieTrailers(movieId)
                val trailerKey = trailersResponse.results?.firstOrNull()?.key
                if (trailerKey != null) {
                    val trailerUrl = "https://www.youtube.com/watch?v=$trailerKey"
                    _trailerUrl.value = trailerUrl
                    savedTrailerUrl = trailerUrl
                } else {
                    _trailerUrl.value = null // No trailers found
                }
            } catch (e: Exception) {
                // Handle error
                Log.e("DetailViewModel", "Error fetching movie trailer", e)
                _trailerUrl.value = null
            }
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}