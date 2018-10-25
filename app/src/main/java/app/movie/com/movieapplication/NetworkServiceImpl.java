package app.movie.com.movieapplication;

import io.reactivex.Observable;


public class NetworkServiceImpl implements NetworkService {

    private final NetworkService networkService;

    public NetworkServiceImpl(NetworkService networkService) {
        this.networkService = networkService;
    }

    @Override
    public Observable<MovieListData> requestMovieList(String apiKey) {
        return networkService.requestMovieList(apiKey);
    }

    @Override
    public Observable<MovieDetailsResponse> requestMovieDetails(int movieId, String apiKey) {
        return networkService.requestMovieDetails(movieId, apiKey);
    }

    @Override
    public Observable<MovieImagesResponse> requestMoviePosters(int movieId, String apiKey) {
        return networkService.requestMoviePosters(movieId, apiKey);
    }
}
