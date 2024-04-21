package com.ZeeSol.vr_movies;


import com.ZeeSol.vr_movies.Model.ApiData
import com.ZeeSol.vr_movies.Model.Movie
import com.ZeeSol.vr_movies.Model.VideoListDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {

    @GET("3/movie/upcoming?api_key=501358f1084f9aaf5c7b6b9181c49363")
    suspend fun getMovies(): ApiData

    @GET("3/movie/{movieId}?api_key=501358f1084f9aaf5c7b6b9181c49363")
    suspend fun getMovieDetails(
        @Path("movieId") movieId: Int
    ): Movie
    @GET("3/movie/{movieId}/videos?api_key=AIzaSyB_lX5ifVoT5NJKAIJ859J7N5i3inUiSqE")
    suspend fun getMovieTrailers(
        @Path("movieId") movieId: Int
    ): Movie


    @GET("3/movie/{movieId}/videos?api_key=501358f1084f9aaf5c7b6b9181c49363")
    suspend fun getTrendingMovieTrailers(
        @Path("movieId") movieId: Int
    ): VideoListDTO


    @GET("3/movie/{movieId}/images?api_key=501358f1084f9aaf5c7b6b9181c49363")
    suspend fun getImagesDetails(
        @Path("movieId") movieId: Int
    ): Movie
}
