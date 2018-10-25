package app.movie.com.movieapplication.movie_details;

import app.movie.com.movieapplication.models.MovieDetailsResponse;

/**
 * This interface defines the methods for implementing the functionality of movie details api.
 * @author Nisha
 */
public interface MovieDetailsView {
    void showWait();

    void removeWait();

    void onFailure(String appErrorMessage);

    void onSuccessMovieDetailsApi(MovieDetailsResponse movieDetailsResponse);
}
