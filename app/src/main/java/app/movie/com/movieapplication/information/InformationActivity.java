package app.movie.com.movieapplication.information;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import app.movie.com.movieapplication.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This class shows the information about developer.
 * @author Nisha
 */
public class InformationActivity extends AppCompatActivity {

    @BindView(R.id.txtToolbarTitle)
    TextView txtToolbarTitle;

    @BindView(R.id.imgInfo)
    ImageView imgInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        ButterKnife.bind(this);
        txtToolbarTitle.setText("Information");
        imgInfo.setVisibility(View.GONE);

    }
}
