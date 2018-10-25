package app.movie.com.movieapplication.movie_details;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import app.movie.com.movieapplication.networking.DaggerDeps;
import app.movie.com.movieapplication.networking.Deps;
import app.movie.com.movieapplication.information.InformationActivity;
import app.movie.com.movieapplication.models.MovieDetailsResponse;
import app.movie.com.movieapplication.models.MovieImagesResponse;
import app.movie.com.movieapplication.networking.NetworkModule;
import app.movie.com.movieapplication.networking.NetworkServiceImpl;
import app.movie.com.movieapplication.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * This class defines the functionality of displaying detailed information of movie.
 * @author Nisha
 */
public class MovieDetailsActivity extends AppCompatActivity implements MovieDetailsView, MovieImagesView,View.OnClickListener{

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.SliderDots)
    LinearLayout linearLayout;

    @BindView(R.id.txtMovieName)
    TextView txtMovieName;

    @BindView(R.id.txtOverview)
    TextView txtOverview;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.movieRatingBar)
    RatingBar movieRatingBar;

    @BindView(R.id.mainLayout)
    LinearLayout mainLayout;

    @BindView(R.id.txtToolbarTitle)
    TextView txtToolbarTitle;

    @BindView(R.id.imgInfo)
    ImageView imgInfo;

    @Inject
    public NetworkServiceImpl mService;

    private int mDotscount;
    private ImageView[] mDots;

    private MovieDetailsPresenter mMovieDetailsPresenter;
    private MovieImagesPresenter mMovieImagesPresenter;

    private Deps mDeps;
    String mApiKey = "2f8c528717c7e456632a1c2737ca1540";
    int mMovieId;
    private ArrayList<String> mMoviePostersList;
    private MoviePostersViewPagerAdapter mViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        imgInfo.setOnClickListener(this);
        Intent intent = getIntent();
        mMovieId = intent.getIntExtra("MovieId", 0);
        File cacheFile = new File(getCacheDir(), "responses");
        mDeps = DaggerDeps.builder().networkModule(new NetworkModule(cacheFile)).build();
        mDeps.inject(this);
        mMoviePostersList = new ArrayList<>();
        mMovieDetailsPresenter = new MovieDetailsPresenter(mService, this, Schedulers.io(), AndroidSchedulers.mainThread(), this);
        mMovieDetailsPresenter.requestMovieDetails(mMovieId, mApiKey);
        mMovieImagesPresenter = new MovieImagesPresenter(mService, this, Schedulers.io(), AndroidSchedulers.mainThread(), this);
        mMovieImagesPresenter.requestMoviePosters(mMovieId, mApiKey);
        mViewPagerAdapter = new MoviePostersViewPagerAdapter(this, mMoviePostersList);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < mDotscount; i++) {
                    mDots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                }
                mDots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    /**
     * This method shows progress bar on network call.
     */
    @Override
    public void showWait() {
        mainLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * This method hides progress bar on completion of network call.
     */
    @Override
    public void removeWait() {
        mainLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    /**
     * This method shows Error message on failure of network call.
     * @param appErrorMessage
     */
    @Override
    public void onFailure(String appErrorMessage) {
        Toast.makeText(this, "appErrorMessage", Toast.LENGTH_SHORT).show();
    }

    /**
     * This method shows the successful api response of the images api and evaluate the response.
     * @param movieImagesResponse
     */
    @Override
    public void onSuccessMoviePostersApi(MovieImagesResponse movieImagesResponse) {
        for (int i = 0; i <= 4; i++) {
            mMoviePostersList.add(movieImagesResponse.getPosters().get(i).getFilePath());
        }
        viewPager.setAdapter(mViewPagerAdapter);
        mDotscount = mViewPagerAdapter.getCount();
        mDots = new ImageView[mDotscount];

        for (int i = 0; i < mDotscount; i++) {

            mDots[i] = new ImageView(this);
            mDots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);

            linearLayout.addView(mDots[i], params);
        }

        mDots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

    }

    /**
     * This method defines the successful api response for the movie detail api.
     * @param movieDetailsResponse
     */
    @Override
    public void onSuccessMovieDetailsApi(MovieDetailsResponse movieDetailsResponse) {

        txtMovieName.setText(movieDetailsResponse.getTitle());
        txtToolbarTitle.setText(movieDetailsResponse.getTitle());
        txtOverview.setText(movieDetailsResponse.getOverview());
        float popularity = movieDetailsResponse.getPopularity().floatValue();
        float ratingValue = popularity/100;
        movieRatingBar.setRating(ratingValue);
        movieRatingBar.setClickable(false);
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
