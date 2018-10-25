package app.movie.com.movieapplication;

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

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MovieDetailsActivity extends AppCompatActivity implements MovieDetailsView, MovieImagesView ,View.OnClickListener{

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

    private int dotscount;
    private ImageView[] dots;

    MovieDetailsPresenter movieDetailsPresenter;
    MovieImagesPresenter movieImagesPresenter;
    @Inject
    public NetworkServiceImpl service;
    private Deps deps;
    String apiKey = "2f8c528717c7e456632a1c2737ca1540";
    int mMovieId;
    private ArrayList<String> images;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        imgInfo.setOnClickListener(this);
        Intent intent = getIntent();
        mMovieId = intent.getIntExtra("MovieId", 0);
        File cacheFile = new File(getCacheDir(), "responses");
        deps = DaggerDeps.builder().networkModule(new NetworkModule(cacheFile)).build();
        deps.inject(this);
        images = new ArrayList<>();
        movieDetailsPresenter = new MovieDetailsPresenter(service, this, Schedulers.io(), AndroidSchedulers.mainThread(), this);
        movieDetailsPresenter.requestMovieDetails(mMovieId, apiKey);
        movieImagesPresenter = new MovieImagesPresenter(service, this, Schedulers.io(), AndroidSchedulers.mainThread(), this);
        movieImagesPresenter.requestMoviePosters(mMovieId, apiKey);
        viewPagerAdapter = new ViewPagerAdapter(this, images);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void showWait() {
        mainLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void removeWait() {
        mainLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void onFailure(String appErrorMessage) {
    }

    @Override
    public void onSuccessMoviePostersApi(MovieImagesResponse movieImagesResponse) {
        for (int i = 0; i <= 4; i++) {
            images.add(movieImagesResponse.getPosters().get(i).getFilePath());
        }
        viewPager.setAdapter(viewPagerAdapter);
        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);

            linearLayout.addView(dots[i], params);
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

    }

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
