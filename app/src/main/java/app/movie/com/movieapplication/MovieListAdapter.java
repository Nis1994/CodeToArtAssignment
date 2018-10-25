package app.movie.com.movieapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MyViewHolder> {

    Activity activity;
    private ArrayList<Result> moviesList;
    String imageUrl = "https://image.tmdb.org/t/p/w500";

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imgMovie)
        ImageView imgMovie;

        @BindView(R.id.imgForward)
        ImageView imgForward;

        @BindView(R.id.txtAdultStatus)
        TextView txtAdultStatus;

        @BindView(R.id.txtDate)
        TextView txtDate;

        @BindView(R.id.txtMovieName)
        TextView txtMovieName;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public MovieListAdapter(ArrayList<Result> moviesList, Activity activity) {
        this.moviesList = moviesList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Result movie = moviesList.get(position);
        holder.txtMovieName.setText(movie.getTitle());
        holder.txtDate.setText(movie.getReleaseDate());
        String url = imageUrl + movie.getPosterPath();
        System.out.println("Image Url : " + url);

        Picasso.get().load(url)
                .into(holder.imgMovie);
        if (movie.getAdult()) {
            holder.txtAdultStatus.setText("(A)");
        } else {
            holder.txtAdultStatus.setText("(U/A)");
        }

        holder.imgForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent movieDetailIntent = new Intent(activity, MovieDetailsActivity.class);
                movieDetailIntent.putExtra("MovieId", movie.getId());
                activity.startActivity(movieDetailIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}