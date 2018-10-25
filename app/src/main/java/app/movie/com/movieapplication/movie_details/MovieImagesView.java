package app.movie.com.movieapplication.movie_details;

import app.movie.com.movieapplication.models.MovieImagesResponse;


/**
 * This interface defines the methods for implementing the functionality of movie images api.
 * @author Nisha
 */
public interface MovieImagesView {

    void showWait();

    void removeWait();

    void onSuccessMoviePostersApi(MovieImagesResponse movieImagesResponse);
}
