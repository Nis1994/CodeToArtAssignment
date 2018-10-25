package app.movie.com.movieapplication.movie_list;

import app.movie.com.movieapplication.models.MovieListData;

/**
 * This interface defines the methods for implementing the functionality of movie list api.
 * @author Nisha
 */
public interface MovieListView {
    void showWait();

    void removeWait();

    void onFailure(String appErrorMessage);

    void onSuccessMovieListApi(MovieListData movieListData);

}
