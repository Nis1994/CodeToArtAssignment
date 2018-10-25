package app.movie.com.movieapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MovieListActivity extends AppCompatActivity implements MovieListView,View.OnClickListener{

    @BindView(R.id.recycler_view_movie_list)
    RecyclerView recycler_view_movie_list;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.imgInfo)
    ImageView imgInfo;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.txtToolbarTitle)
    TextView txtToolbarTitle;

    private MovieListAdapter mMovieListAdapter;
    ArrayList<Result> moviesList = new ArrayList<>();
    MovieListPresenter movieListPresenter;
    @Inject
    public NetworkServiceImpl service;
    private Deps deps;
    String apiKey = "2f8c528717c7e456632a1c2737ca1540";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        ButterKnife.bind(this);
        imgInfo.setOnClickListener(this);
        txtToolbarTitle.setText("Upcoming Movies");
        File cacheFile = new File(getCacheDir(), "responses");
        deps = DaggerDeps.builder().networkModule(new NetworkModule(cacheFile)).build();
        deps.inject(this);
        mMovieListAdapter = new MovieListAdapter(moviesList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_view_movie_list.setLayoutManager(mLayoutManager);
        recycler_view_movie_list.setItemAnimator(new DefaultItemAnimator());
        recycler_view_movie_list.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        Log.d("Before Presenter", "......Before Presenter......");
        movieListPresenter = new MovieListPresenter(service, this, Schedulers.io(), AndroidSchedulers.mainThread(), this);
        movieListPresenter.requestMovieList(apiKey);
        Log.d("After Presenter", "...........After Presenter........");

    }

    @Override
    public void showWait() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void removeWait() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onFailure(String appErrorMessage) {

    }

    @Override
    public void onSuccessMovieListApi(MovieListData movieListData) {

        Log.d("MovieListData", "Response : " + new Gson().toJson(movieListData));
        for (int i = 0; i < movieListData.getResults().size(); i++) {
            moviesList.add(movieListData.getResults().get(i));
        }
        recycler_view_movie_list.setAdapter(mMovieListAdapter);

    }

    @Override
    public void onClick(View view) {

        if (view.getId()==R.id.imgInfo)
        {
            Intent intent = new Intent(this,InformationActivity.class);
            startActivity(intent);
        }
    }
}
