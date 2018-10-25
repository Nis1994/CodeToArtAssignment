package app.movie.com.movieapplication.movie_details;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;

import app.movie.com.movieapplication.models.MovieDetailsResponse;
import app.movie.com.movieapplication.networking.NetworkServiceImpl;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;

/**
 * This class defines the calling of movie details api functionality.
 * @author Nisha
 */
public class MovieDetailsPresenter {

    private NetworkServiceImpl mService;
    private MovieDetailsView mMovieDetailsView;
    private CompositeDisposable mCompositeDisposable;
    @NonNull
    protected Scheduler mBackgroundScheduler;
    private Context mContext;
    @NonNull
    private Scheduler mMainScheduler;


    public MovieDetailsPresenter(NetworkServiceImpl service, MovieDetailsView view, @NonNull Scheduler backgroundScheduler, @NonNull Scheduler mainScheduler, Context mContext) {

        mService = service;
        mMovieDetailsView = view;
        mBackgroundScheduler = backgroundScheduler;
        mMainScheduler = mainScheduler;
        mContext = mContext;
        mCompositeDisposable = new CompositeDisposable();
    }

    public void requestMovieDetails(int movieId, String apiKey) {

        mMovieDetailsView.showWait();
        Disposable disposable = mService.requestMovieDetails(movieId, apiKey)
                .subscribeOn(mBackgroundScheduler)
                .observeOn(mMainScheduler)
                .onErrorResumeNext(new Function<Throwable, ObservableSource<? extends MovieDetailsResponse>>() {
                    @Override
                    public ObservableSource<? extends MovieDetailsResponse> apply(Throwable throwable) throws Exception {
                        return Observable.error(throwable);
                    }
                })
                .subscribeWith(new DisposableObserver<MovieDetailsResponse>() {

                    @Override
                    public void onNext(MovieDetailsResponse movieDetailsResponse) {
                        Log.d("MovieDetailsData", "Response : " + new Gson().toJson(movieDetailsResponse));
                        mMovieDetailsView.removeWait();
                        mMovieDetailsView.onSuccessMovieDetailsApi(movieDetailsResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mMovieDetailsView.removeWait();
                        Log.d("Error Message : ", "" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }

                });
        mCompositeDisposable.add(disposable);
    }
}
