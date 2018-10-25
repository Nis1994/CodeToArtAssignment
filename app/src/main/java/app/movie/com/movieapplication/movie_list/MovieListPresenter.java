package app.movie.com.movieapplication.movie_list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;

import app.movie.com.movieapplication.models.MovieListData;
import app.movie.com.movieapplication.networking.NetworkServiceImpl;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;

/**
 * This class defines the calling of movie list api functionality.
 * @author Nisha
 */
public class MovieListPresenter {

    private NetworkServiceImpl mService;
    private MovieListView mMovieListView;
    private CompositeDisposable mCmpositeDisposable;
    @NonNull
    protected Scheduler mBackgroundScheduler;
    private Context mContext;
    @NonNull
    private Scheduler mMainScheduler;


    public MovieListPresenter(NetworkServiceImpl service, MovieListView view, @NonNull Scheduler backgroundScheduler, @NonNull Scheduler mainScheduler, Context mContext) {

        mService = service;
        mMovieListView = view;
        mBackgroundScheduler = backgroundScheduler;
        mMainScheduler = mainScheduler;
        this.mContext = mContext;
        mCmpositeDisposable = new CompositeDisposable();
    }

    public void requestMovieList(String apiKey) {

        mMovieListView.showWait();
        Disposable disposable = mService.requestMovieList(apiKey)
                .subscribeOn(mBackgroundScheduler)
                .observeOn(mMainScheduler)
                .onErrorResumeNext(new Function<Throwable, ObservableSource<? extends MovieListData>>() {
                    @Override
                    public ObservableSource<? extends MovieListData> apply(Throwable throwable) throws Exception {
                        return Observable.error(throwable);
                    }
                })
                .subscribeWith(new DisposableObserver<MovieListData>() {

                    @Override
                    public void onNext(MovieListData movieListData) {
                        mMovieListView.removeWait();
                        Log.d("MovieListData", "Response : " + new Gson().toJson(movieListData));
                        mMovieListView.onSuccessMovieListApi(movieListData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mMovieListView.removeWait();
                        Log.d("Error Message : ", "" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }

                });
        mCmpositeDisposable.add(disposable);
    }
}
