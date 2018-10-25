package app.movie.com.movieapplication.movie_list;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import app.movie.com.movieapplication.networking.DaggerDeps;
import app.movie.com.movieapplication.networking.Deps;
import app.movie.com.movieapplication.information.InformationActivity;
import app.movie.com.movieapplication.models.MovieListData;
import app.movie.com.movieapplication.networking.NetworkModule;
import app.movie.com.movieapplication.networking.NetworkServiceImpl;
import app.movie.com.movieapplication.R;
import app.movie.com.movieapplication.models.Result;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * This class defines the functionality of displaying list of upcoming movies.
 * @author Nisha
 */
public class MovieListActivity extends AppCompatActivity implements MovieListView, View.OnClickListener {

    @BindView(R.id.recycler_view_movie_list)
    RecyclerView recycler_view_movie_list;

    @BindView(R.id.imgInfo)
    ImageView imgInfo;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.txtToolbarTitle)
    TextView txtToolbarTitle;

    private MovieListAdapter mMovieListAdapter;
    private ArrayList<Result> mMoviesList = new ArrayList<>();
    private MovieListPresenter mMovieListPresenter;
    @Inject
    public NetworkServiceImpl mService;
    private Deps mDeps;
    private String mApiKey = "2f8c528717c7e456632a1c2737ca1540";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        ButterKnife.bind(this);
        imgInfo.setOnClickListener(this);
        txtToolbarTitle.setText("Upcoming Movies");
        File cacheFile = new File(getCacheDir(), "responses");
        mDeps = DaggerDeps.builder().networkModule(new NetworkModule(cacheFile)).build();
        mDeps.inject(this);
        mMovieListAdapter = new MovieListAdapter(mMoviesList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_view_movie_list.setLayoutManager(mLayoutManager);
        recycler_view_movie_list.setItemAnimator(new DefaultItemAnimator());
        recycler_view_movie_list.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        Log.d("Before Presenter", "......Before Presenter......");
        mMovieListPresenter = new MovieListPresenter(mService, this, Schedulers.io(), AndroidSchedulers.mainThread(), this);
        mMovieListPresenter.requestMovieList(mApiKey);
        Log.d("After Presenter", "...........After Presenter........");

    }

    /**
     * This method shows progress bar on network call.
     */
    @Override
    public void showWait() {
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * This method hides progress bar on completion of network call.
     */
    @Override
    public void removeWait() {
        progressBar.setVisibility(View.GONE);
    }

    /**
     * This method shows Error message on failure of network call.
     *
     * @param appErrorMessage
     */
    @Override
    public void onFailure(String appErrorMessage) {

    }

    /**
     * This method shows the successful api response of the movie list api and evaluate the response.
     *
     * @param movieListData
     */
    @Override
    public void onSuccessMovieListApi(MovieListData movieListData) {

        Log.d("MovieListData", "Response : " + new Gson().toJson(movieListData));
        for (int i = 0; i < movieListData.getResults().size(); i++) {
            mMoviesList.add(movieListData.getResults().get(i));
        }
        recycler_view_movie_list.setAdapter(mMovieListAdapter);

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.imgInfo) {
            Intent infoIntent = new Intent(this, InformationActivity.class);
            startActivity(infoIntent);
        }
    }
}
