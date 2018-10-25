package app.movie.com.movieapplication.movie_details;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;

import app.movie.com.movieapplication.models.MovieImagesResponse;
import app.movie.com.movieapplication.networking.NetworkServiceImpl;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;

/**
 * This class defines the calling of movie images api functionality.
 * @author Nisha
 */
public class MovieImagesPresenter {

    private NetworkServiceImpl mService;
    private MovieImagesView mMovieImagesView;
    private CompositeDisposable mCompositeDisposable;
    @NonNull
    protected Scheduler mBackgroundScheduler;
    private Context mContext;
    @NonNull
    private Scheduler mMainScheduler;

    public MovieImagesPresenter(NetworkServiceImpl service, MovieImagesView view, @NonNull Scheduler backgroundScheduler, @NonNull Scheduler mainScheduler, Context mContext) {

        mService = service;
        mMovieImagesView = view;
        mBackgroundScheduler = backgroundScheduler;
        mMainScheduler = mainScheduler;
        this.mContext = mContext;
        mCompositeDisposable = new CompositeDisposable();
    }

    public void requestMoviePosters(int movieId, String apiKey) {

        mMovieImagesView.showWait();
        Disposable disposable = mService.requestMoviePosters(movieId, apiKey)
                .subscribeOn(mBackgroundScheduler)
                .observeOn(mMainScheduler)
                .onErrorResumeNext(new Function<Throwable, ObservableSource<? extends MovieImagesResponse>>() {
                    @Override
                    public ObservableSource<? extends MovieImagesResponse> apply(Throwable throwable) throws Exception {
                        return Observable.error(throwable);
                    }
                })
                .subscribeWith(new DisposableObserver<MovieImagesResponse>() {

                    @Override
                    public void onNext(MovieImagesResponse movieImagesResponse) {
                        Log.d("MovieListData", "Response : " + new Gson().toJson(movieImagesResponse));
                        mMovieImagesView.onSuccessMoviePostersApi(movieImagesResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mMovieImagesView.removeWait();
                        Log.d("Error Message : ", "" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }

                });
        mCompositeDisposable.add(disposable);
    }
}
