package app.movie.com.movieapplication.movie_details;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.movie.com.movieapplication.R;

public class MoviePostersViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<String> mMoviePostersList;
    String mImageUrl = "https://image.tmdb.org/t/p/w500";


    public MoviePostersViewPagerAdapter(Context context, ArrayList<String> images) {
        mContext = context;
        mMoviePostersList = images;
    }

    @Override
    public int getCount() {
        return mMoviePostersList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mLayoutInflater.inflate(R.layout.dots_layout, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        String url = mImageUrl + mMoviePostersList.get(position);
        System.out.println("Image Url : " + url);
        Picasso.get().load(url)
                .into(imageView);

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }
}