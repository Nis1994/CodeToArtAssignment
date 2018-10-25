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

public class MovieImagesPresenter {

    private NetworkServiceImpl service;
    private MovieImagesView view;
    private CompositeDisposable disposables;
    @NonNull
    protected Scheduler backgroundScheduler;
    private Context mContext;
    @NonNull
    private Scheduler mainScheduler;

    public MovieImagesPresenter(NetworkServiceImpl service, MovieImagesView view, @NonNull Scheduler backgroundScheduler, @NonNull Scheduler mainScheduler, Context mContext) {

        this.service = service;
        this.view = view;
        this.backgroundScheduler = backgroundScheduler;
        this.mainScheduler = mainScheduler;
        this.mContext = mContext;
        this.disposables = new CompositeDisposable();
    }

    public void requestMoviePosters(int movieId, String apiKey) {

        view.showWait();
        Disposable disposable = service.requestMoviePosters(movieId, apiKey)
                .subscribeOn(backgroundScheduler)
                .observeOn(mainScheduler)
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
                        view.onSuccessMoviePostersApi(movieImagesResponse);
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
