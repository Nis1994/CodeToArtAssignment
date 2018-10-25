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

public class MovieListPresenter {

    private NetworkServiceImpl service;
    private MovieListView view;
    private CompositeDisposable disposables;
    @NonNull
    protected Scheduler backgroundScheduler;
    private Context mContext;
    @NonNull
    private Scheduler mainScheduler;


    public MovieListPresenter(NetworkServiceImpl service, MovieListView view, @NonNull Scheduler backgroundScheduler, @NonNull Scheduler mainScheduler, Context mContext) {

        this.service = service;
        this.view = view;
        this.backgroundScheduler = backgroundScheduler;
        this.mainScheduler = mainScheduler;
        this.mContext = mContext;
        this.disposables = new CompositeDisposable();
    }

    public void requestMovieList(String apiKey) {

        view.showWait();
        Disposable disposable = service.requestMovieList(apiKey)
                .subscribeOn(backgroundScheduler)
                .observeOn(mainScheduler)
                .onErrorResumeNext(new Function<Throwable, ObservableSource<? extends MovieListData>>() {
                    @Override
                    public ObservableSource<? extends MovieListData> apply(Throwable throwable) throws Exception {
                        return Observable.error(throwable);
                    }
                })
                .subscribeWith(new DisposableObserver<MovieListData>() {

                    @Override
                    public void onNext(MovieListData movieListData) {
                        view.removeWait();
                        Log.d("MovieListData","Response : "+new Gson().toJson(movieListData));
                        view.onSuccessMovieListApi(movieListData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.removeWait();
                        Log.d("Error Message : ",""+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }

                });
        disposables.add(disposable);
    }
}
