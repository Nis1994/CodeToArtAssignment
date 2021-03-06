package app.movie.com.movieapplication.networking;

import app.movie.com.movieapplication.models.MovieDetailsResponse;
import app.movie.com.movieapplication.models.MovieImagesResponse;
import app.movie.com.movieapplication.models.MovieListData;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NetworkService {

    @GET("movie/upcoming")
    Observable<MovieListData> requestMovieList(@Query("api_key") String apiKey);

    @GET("movie/{movie-id}")
    Observable<MovieDetailsResponse> requestMovieDetails(@Path("movie-id") int movieId, @Query("api_key") String apiKey);

    @GET("movie/{movie-id}/images")
    Observable<MovieImagesResponse> requestMoviePosters(@Path("movie-id") int movieId, @Query("api_key") String apiKey);

}
