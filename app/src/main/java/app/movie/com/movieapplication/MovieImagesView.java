package app.movie.com.movieapplication;

public interface MovieImagesView {

    void showWait();

    void removeWait();

    void onFailure(String appErrorMessage);

    void onSuccessMoviePostersApi(MovieImagesResponse movieImagesResponse);
}
