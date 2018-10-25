package app.movie.com.movieapplication;

public interface MovieDetailsView {
    void showWait();

    void removeWait();

    void onFailure(String appErrorMessage);

    void onSuccessMovieDetailsApi(MovieDetailsResponse movieDetailsResponse);
}
