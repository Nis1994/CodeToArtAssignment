package app.movie.com.movieapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;

public class MovieDetailsPresenter {

    private NetworkServiceImpl service;
    private MovieDetailsView view;
    private CompositeDisposable disposables;
    @NonNull
    protected Scheduler backgroundScheduler;
    private Context mContext;
    @NonNull
    private Scheduler mainScheduler;


    public MovieDetailsPresenter(NetworkServiceImpl service, MovieDetailsView view, @NonNull Scheduler backgroundScheduler, @NonNull Scheduler mainScheduler, Context mContext) {

        this.service = service;
        this.view = view;
        this.backgroundScheduler = backgroundScheduler;
        this.mainScheduler = mainScheduler;
        this.mContext = mContext;
        this.disposables = new CompositeDisposable();
    }

    public void requestMovieDetails(int movieId, String apiKey) {

        view.showWait();
        Disposable disposable = service.requestMovieDetails(movieId, apiKey)
                .subscribeOn(backgroundScheduler)
                .observeOn(mainScheduler)
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
                        view.removeWait();
                        view.onSuccessMovieDetailsApi(movieDetailsResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.removeWait();
                        Log.d("Error Message : ", "" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }

                });
        disposables.add(disposable);
    }
}
