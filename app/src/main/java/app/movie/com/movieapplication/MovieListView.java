package app.movie.com.movieapplication;

public interface MovieListView {
    void showWait();

    void removeWait();

    void onFailure(String appErrorMessage);

    void onSuccessMovieListApi(MovieListData movieListData);

}
