package app.movie.com.movieapplication.main;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import app.movie.com.movieapplication.movie_list.MovieListActivity;
import app.movie.com.movieapplication.R;

/**
 * This class defines the functionality of launching screen.
 * @author Nisha
 */
public class MainActivity extends AppCompatActivity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* New Handler to start the MovieListActivity
         * and close this Splash-Screen after 2 seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the MovieListActivity. */
                Intent mainIntent = new Intent(MainActivity.this,MovieListActivity.class);
                MainActivity.this.startActivity(mainIntent);
                MainActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
